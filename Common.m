BeginPackage["Common`"]

verbose::usage = "Print verbose expression value in a VerboseBlock.";
VerboseBlock::usage = "Begin a verbose block.";
Dispatcher::usage = "Simulate an object oriented dispatcher.";
Let::usage = "Consecuitive bindings for With scoping construct.";

Begin["`Private`"]

verbose[val_] := val;

SetAttributes[VerboseBlock, HoldAll]
VerboseBlock[expr_] := Block[{verbose},
  verbose[Null] = Null;
  verbose[val_] := (Print[val]; val);
  expr
];

Dispatcher /: Dot[Dispatcher[self_], msg_String] := self[msg];
Dispatcher /: Dot[Dispatcher[self_], msg_String[args___]] := self[msg, args];
Dispatcher /: Dot[Dispatcher[self_], msg_Symbol] := self[SymbolName[msg]];
Dispatcher /: Dot[Dispatcher[self_], msg_Symbol[args___]] := self[SymbolName[msg], args];

SetAttributes[Let, HoldAll];
Let /: Verbatim[SetDelayed][lhs_, rhs:Let[{__}, _]] :=
  Block[{With}, Attributes[With] = {HoldAll};
    lhs := Evaluate[rhs]];
Let[{}, expr_] := expr;
Let[{head_}, expr_] := With[{head}, expr];
Let[{head_, tail__}, expr_] :=
  Block[{With}, Attributes[With] = {HoldAll};
    With[{head}, Evaluate[Let[{tail}, expr]]]];

End[]

EndPackage[]