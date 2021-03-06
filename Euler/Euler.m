BeginPackage["Euler`", {"JLink`"}]

Let::usage = "Consecuitive bindings for With scoping construct.";

ImportResource::usage = "Import an external resource";
Timed::usage = "Display solve result and used time";
Rounded::usage = "Display result rounded to given decimal point places";
JavaSolve::usage = "Solve a problem with Java program";

Unreap::usage = "Extract result from Reap expression";

TotientSum::usage = "Returns the totient summation";

Hungarian::usage = "The Hungarian algorithm for solving the assignment problem";
Pell::usage = "Find the funtamental solution of a Pell equation";
PellSeries::usage = "Generate a series of Pell equation solution";
PellFunction::usage = "Returns a pure function that generate all solutions for a Pell equation";

PalindromeSeries::usage = "Generate a series of palindrome numbers";
PalindromeList::usage = "Generate a list of palindrome numbers";

MatrixPowerMod::usage = "Compute the matrix power with modulus";

TOC::usage = "Generate table of contents";

Begin["`Private`"]

SetAttributes[Let, HoldAll];
Let /: Verbatim[SetDelayed][lhs_, rhs:Let[{__}, _]] :=
  Block[{With}, Attributes[With] = {HoldAll};
    lhs := Evaluate[rhs]];
Let[{}, expr_] := expr;
Let[{head_}, expr_] := With[{head}, expr];
Let[{head_, tail__}, expr_] :=
  Block[{With}, Attributes[With] = {HoldAll};
    With[{head}, Evaluate[Let[{tail}, expr]]]];

ImportResource[name_, elements___] := ImportResource[name, elements] =
  Import["https://projecteuler.net/project/resources/" <> name, elements];

coloring[time_] := Darker@Gray /; time < 1;
coloring[time_] := Darker@Darker@Green /; time < 4;
coloring[time_] := Darker@Blue /; time < 60;
coloring[time_] := Red /; time >= 60;

timing[time_, result_] :=
  Column[{result, Style["Time: " <> ToString[time], Smaller, coloring[time]]}];

SetAttributes[Timed, HoldFirst];
Timed[expr_] := timing @@ AbsoluteTiming[expr];

Rounded[n_][expr_] := NumberForm[N[expr], {Infinity, n}];

JavaSolve[num_Integer, args___] := JavaBlock[
  LoadJavaClass["euler.Problem" <> ToString[num]];
  Symbol["euler`Problem" <> ToString[num] <> "`solve"][args]
];

SetAttributes[Unreap, HoldFirst];
Unreap[expr_] := First@Last@Reap[expr];

TotientSum[x_?Positive] := Module[{Phi, Res},
  Phi[1] = 1;
  Phi[n_] := Phi[n] =
    n(n+1)/2 - Floor[(n+1)/2] -
    Sum[
      Phi@Floor[n/z] +
      If[z == Floor[n/z], 0, (Floor[n/z] - Floor[n/(z+1)]) * Phi[z]],
      {z, 2, Floor@Sqrt[n]}];
  Res = Phi[x];
  Clear[Phi];
  Res
];

Hungarian[costMatrix_List] := JavaBlock[
  InstallJava[];
  MapIndexed[
    {First[#2], #1+1}&,
    JavaNew["euler.algo.Hungarian", costMatrix]@execute[]
  ]
];

Pell[d_Integer, c:1|-1:1] := Module[{solvable, approach},
  solvable[{__, x__}] := c == 1 || OddQ@Length[x];
  solvable[_] := False;

  approach[{x__, f_}] :=
    With[{q = FromContinuedFraction[{x}]},
      If[Numerator[q]^2 - d Denominator[q]^2 == c,
        {Numerator[q], Denominator[q]},
        approach[f, {x, f}]]];

  approach[f_, {x__, {}}] := approach[f, {x, f}];
  approach[f_, {x__, {y_, ys___}}] :=
    With[{q = FromContinuedFraction[{x, y}]},
      If[Numerator[q]^2 - d Denominator[q]^2 == c,
        {Numerator[q], Denominator[q]},
        approach[f, {x, y, {ys}}]]];

  With[{f = ContinuedFraction[Sqrt[d]]},
    If[solvable[f], approach[f], {}]]
];

PellSeries[d_] := PellSeries[d, 1];
PellSeries[d_, 1] := Module[{p, q, x, y, next},
  {x, y} = {p, q} = Pell[d];
  next[] := With[{x0 = x, y0 = y},
    x = p x0 + d q y0;
    y = p y0 + q x0;
    {x0, y0}];
  next
];

PellSeries[d_, -1] := Module[{p, q, x, y, next},
  {x, y} = {p, q} = Pell[d, -1];
  next[] := With[{x0 = x, y0 = y},
    With[{x1 = p x0 + d q y0, y1 = p y0 + q x0},
      x = p x1 + d q y1;
      y = p y1 + q x1;
      {x0, y0}];
    {x0, y0}];
  next
];

PellFunction[d_Integer, c:1|-1:1][n_] :=
  With[{series = PellSeries[d, c]},
    Nest[series[]&, {}, n]];

PalindromeSeries[len_Integer:1] :=
  Module[{n, length, limit, init, next, mix},
    init[l_] := (
      length = l;
      n = 10^(Floor[(length+1)/2] - 1);
      limit = n * 10;
    );

    mix[n_, l_?EvenQ] := mix[n, n, l/2];
    mix[n_, l_?OddQ] := mix[n, Floor[n/10], (l-1)/2];
    mix[a_, b_, 0] := a;
    mix[a_, b_, n_] := mix[a*10 + Mod[b,10], Floor[b/10], n-1];

    next[] := With[{r = mix[n, length]},
      If[++n >= limit, init[length + 1]];
      r
    ];

    init[len];
    next
  ];

PalindromeList[n_Integer, minlen_Integer:1] :=
  With[{p = PalindromeSeries[minlen]},
    NestList[p[]&, p[], n - 1]];

PalindromeList[{len_Integer}] := PalindromeList[{len, len}];

PalindromeList[{minlen_Integer, maxlen_Integer}] :=
  With[{p = PalindromeSeries[minlen]},
    Most@NestWhileList[p[]&, p[], IntegerLength[#] <= maxlen&]];

MatrixPowerMod[a_, 0, _] :=
  IdentityMatrix[Length[a]];
MatrixPowerMod[a_, n_?Negative, m_] :=
  MatrixPowerMod[Inverse[a], -n, m];
MatrixPowerMod[a_, n_, m_] :=
  Fold[With[{b = Mod[#1.#1, m]},
    If[#2 == 1, Mod[a.b, m], b]]&,
    a, Rest@IntegerDigits[n, 2]];

TOC[total_Integer] := Module[{entries, cell},
  entries = First@Last@Reap@Scan[
    With[{tag = Lookup[Options[#], CellTags]},
      If[Head[tag] === String, Sow[ToExpression[tag]]]]&,
    Cells[EvaluationNotebook[], CellStyle->"Subsubsection"]];

  cell[num_] :=
    If[MemberQ[entries, num],
      Item[
        Button[
          Style[num, FontFamily->"Helvetica", FontSize->12],
          NotebookLocate@ToString[num],
          Appearance->"Frameless"],
        Background->RGBColor[0.8, 0.9, 0.72]],
      Item[
        Style[num, FontFamily->"Helvetica", FontSize->13],
        Background->White]];

  Column[
    Map[
      Grid[
        Partition[Map[cell, #], 20, 20, {1, 1}, {}],
        Frame->All,
        FrameStyle->Gray,
        ItemSize->{1.7, 1.5},
        Spacings->{0.6, Automatic}
      ]&,
      Partition[Range[total], 100, 100, {1, 1}, {}]],
    Center, 1]
];

End[]

EndPackage[]
