BeginPackage["Common`"]

verbose::usage = "Print verbose expression value in a VerboseBlock.";
VerboseBlock::usage = "Begin a verbose block.";
Dispatcher::usage = "Simulate an object oriented dispatcher.";
Let::usage = "Consecuitive bindings for With scoping construct.";
Unload::usage = "Unload a package.";
Reload::usage = "Reload a package.";

Begin["`Private`"]

verbose[val_] := val;

SetAttributes[VerboseBlock, HoldAll];
VerboseBlock[expr_] := Block[{verbose},
  verbose[Null] = Null;
  verbose[val_] := (Print[val]; val);
  expr
];

SetAttributes[clean, HoldFirst];
clean[s_Symbol] := SymbolName@Unevaluated[s] // StringDelete@RegularExpression["\\$.*$"];

Dispatcher[dispatch_] := (
  SetAttributes[dispatch, HoldFirst];
  dispatch[s_Symbol] := dispatch[Evaluate@clean[s]];
  dispatch[s_Symbol[args___]] := dispatch[Evaluate@clean[s], args];
  dispatch
);

SetAttributes[Let, HoldAll];
Let /: Verbatim[SetDelayed][lhs_, rhs:Let[{__}, _]] :=
  Block[{With}, Attributes[With] = {HoldAll};
    lhs := Evaluate[rhs]];
Let[{}, expr_] := expr;
Let[{head_}, expr_] := With[{head}, expr];
Let[{head_, tail__}, expr_] :=
  Block[{With}, Attributes[With] = {HoldAll};
    With[{head}, Evaluate[Let[{tail}, expr]]]];

Attributes[Unload] = {Listable};
Unload::nonun = "The System` and Global` contexts cannot be removed.";
Unload["System`" | "Global`"] := Message[Unload::nonun];
Unload[pkg_String] := (
  Scan[(Unprotect[#]; Quiet@Remove[#])&, {pkg<>"*", pkg<>"Private`*"}];
  Unprotect[$Packages];
  $Packages = DeleteCases[$Packages, pkg];
  Protect[$Packages];
  $ContextPath = DeleteCases[$ContextPath, pkg];
);

Attributes[Reload] = {Listable};
Reload[pkg_String] := (Unload[pkg]; Get[pkg]);

End[]

EndPackage[]