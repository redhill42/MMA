BeginPackage["Euler`", {"JLink`"}]

ImportResource::usage = "Import an external resource";
Timed::usage = "Display solve result and used time";
JavaSolve::usage = "Solve a problem with Java program";

Hungarian::usage = "The Hungarian algorithm for solving the assignment problem";
Pell::usage = "Find the funtamental solution of a Pell equation";
PellSeries::usage = "Generate a series of Pell equation solution";
PellFunction::usage = "Returns a pure function that generate all solutions for a Pell equation";

Palindrome::usage = "Generate a series of palindrome numbers";

Begin["`Private`"]

ImportResource[name_, elements___] := ImportResource[name, elements] =
  Import["https://projecteuler.net/project/resources/" <> name, elements];

SetAttributes[Timed, HoldFirst];
Timed[expr_] :=
  With[{format = {#2, Style["Time: " <> ToString[#1], Smaller, Darker@Gray]} &},
    Column[format @@ AbsoluteTiming[expr]]];

JavaSolve[num_Integer, args___] := JavaBlock[
  JavaNew["euler.Problem" <> ToString[num]]@solve[args]
];

Hungarian[costMatrix_List] := JavaBlock[
  InstallJava[];
  MapIndexed[
    {First[#2], #1+1}&,
    JavaNew["euler.HungarianAlgorithm", costMatrix]@execute[]
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

Palindrome[len_Integer:1] :=
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

End[]

EndPackage[]
