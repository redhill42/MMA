(* ::Package:: *)

BeginPackage["Minesweeper`"];

verbose::usage = "Print verbose expression value in a VerboseBlock.";
trace::usage = "Print verbose message in a VerboseBlock.";
VerboseBlock::usage = "Begin a verbose block.";

Dispatcher::usage = "Simulate an object oriented dispatcher.";

MakeMinesweeper::usage = "Make a new minesweeper board.";
MinesweeperPlotter::usage = "Create a minesweeper plotter.";
MinesweeperPlotter2::usage = "An alternate minesweeper plotter.";
Minesweeper::usage = "The default minesweeper game implementation.";

Begin["`Private`"];

SetAttributes[{verbose, trace, VerboseBlock}, HoldAll];
verbose[val_] := val;
verbose[_, val_] := val;
trace[_] = Null;

VerboseBlock[expr_] := Block[{verbose, trace},
  verbose[val_] := (Print[val]; val);
  verbose[text_, val_] := (Print[ToString@text <> " = " <> ToString@val]; val);
  trace[Null] = Null;
  trace[val_] := Print[val];
  expr
];
    
Dispatcher /: Dot[Dispatcher[self_], msg_String] := self[msg];
Dispatcher /: Dot[Dispatcher[self_], msg_String[args___]] := self[msg, args];
Dispatcher /: Dot[Dispatcher[self_], msg_Symbol] := self[SymbolName[msg]];
Dispatcher /: Dot[Dispatcher[self_], msg_Symbol[args___]] := self[SymbolName[msg], args];

MakeMinesweeper[rows0_Integer, cols0_Integer, mines0_Integer, sample0_List:{}] := Module[{
	rows, cols, mines,
    grid, clicked, marked, boomed, success, remaining, minesRemaining,
    mineQ, freeQ, markRemains, neighbors, calcNeighbors, randomCell, 
    click, mark, safe, show,
    startTime, stopTime, inited, start, stop,
    reset, restart, solve, solve0, dispatch
  },

  mineQ = grid[#] == "x" &;
  freeQ = !(clicked[#] || marked[#])&;
  markRemains = grid[#] - Length@neighbors[#, marked]&;
  success := remaining == 0;

  neighbors[cell_, crit_] :=
    Select[
      cell+#& /@ Tuples[{-1,0,1},2], 
      #!=cell && And@@Thread[1<=#<={rows,cols}] && crit[#] &
    ];

  randomCell[pred_] := 
    Do[With[{cell = RandomInteger[rows*cols-1] ~QuotientRemainder~ cols + {1,1}},
      If[pred[cell], Return[cell]]],
      Infinity];
  
  reset[rows1_, cols1_, mines1_, sample_:{}] := (
    {rows, cols, mines} = {rows1, cols1, mines1};

    Clear[grid];
    grid[_] = 0;
    inited = False;
    restart[];

    If[Length[sample] != 0,
      (* Fill grid with sample data. *)
      mines = minesRemaining = Length[sample];
      remaining = rows*cols-mines;
      Scan[(grid[#] = "x")&, sample];
      calcNeighbors[];
      inited = True
    ];
  );
  
  restart[] := (
    Clear[clicked, marked];
    clicked[_] = False;
    marked[_] = False;
    boomed = False;
    startTime = stopTime = 0;
    minesRemaining = mines;
    remaining = rows*cols-mines;
  );
  
  calcNeighbors[] :=
    Do[With[{cell = {x,y}},
      If[!mineQ[cell], grid[cell] = Length@neighbors[cell, mineQ]]],
      {x, 1, rows}, {y, 1, cols}
    ];

  start[init_] := 
    If[startTime == 0,
      startTime = SessionTime[];

      If[!inited,
        inited = True;
        
        (* Generate a random mine grid. *)
        With[{excludes = If[init =!= Nothing, neighbors[init, True&] ~Append~ init, {}]},
          Do[grid[randomCell[!mineQ[#] && !MemberQ[excludes, #] &]] = "x", mines]
        ];
        calcNeighbors[]
      ]
    ];
  
  stop[] := If[stopTime==0 && (boomed||success), stopTime = SessionTime[]];
  
  click[cell_] := (
    start[cell];
    Which[
      (* Do nothing after boomed or success. *)
      boomed || success,
        Null,
        
      (* Click on a mine cell will boom. *)
      mineQ[cell],
        clicked[cell] = True;
        boomed = True,
        
      (* Click on a incorrectly marked cell will boom. *)
      marked[cell],
        boomed = True,
        
      (* Click on a clicked cell will click neighbor cells if the cell is safe. *)
      clicked[cell],
        If[markRemains[cell] == 0, Scan[click, neighbors[cell, freeQ]]],
 
      (* Click a cell will flip the cell and cascade click neighbors
         cells if the cell has no mine neighbors. *)       
      True,
        clicked[cell] = True;
        remaining--;
        If[grid[cell] == 0, Scan[click, neighbors[cell, freeQ]]]
    ];
    stop[];
    cell
  );

  mark[cell_] := (
    start[Nothing];
    If[!(boomed||success) && !clicked[cell],
      If[marked[cell] = !marked[cell], minesRemaining--, minesRemaining++]];
    cell
  );

  safe[cell_ /; boomed||success||marked[cell]] = {};
  safe[cell_ ? clicked] := neighbors[cell, freeQ];
  safe[cell_] := {cell};
  
  show[cell_ ? clicked] := If[mineQ[cell], "X", grid[cell]];
  show[cell_ ? marked]  := If[mineQ[cell] || !(boomed||success), "m", "w"];
  show[cell_ ? mineQ]   := If[boomed, "x", If[success, "m", " "]];
  show[_] = " ";

  dispatch["Rows"] := rows;
  dispatch["Cols"] := cols;
  dispatch["Mines"] := mines;
  dispatch["Show"] := Array[show@*List, {rows, cols}];
  dispatch["Click", cell_, True] := If[mineQ[cell], mark[cell], click[cell]];
  dispatch["Click", cell_, _:False] := If[!marked[cell], click[cell]];
  dispatch["Mark", cell_] := mark[cell];
  dispatch["Safe", cell_] := safe[cell];
  dispatch["Started"] := startTime != 0;
  dispatch["Boomed"] := boomed;
  dispatch["Success"] := success;
  dispatch["Remaining"] := remaining;
  dispatch["MinesRemaining"] := If[success, 0, minesRemaining];
  
  dispatch["Reset", keep_:False] := If[keep, restart[], reset[rows, cols, mines]];
  dispatch["Reset", rows1_Integer, cols1_Integer, mines1_Integer] := reset[rows1, cols1, mines1];
  
  dispatch["TimeUsed"] := Which[
      startTime == 0, 0,
      stopTime  != 0, stopTime - startTime,
      True,           SessionTime[] - startTime];

  dispatch["RandomClick", True] :=
    With[{alternative = Function[AnyTrue[neighbors[#, clicked], Function[markRemains[#] == 1 && Length@neighbors[#, freeQ] == 2]]]},
      click@SelectFirst[Join@@Array[List, {rows,cols}],
                        freeQ[#] && !mineQ[#] && alternative[#] &,
                        randomCell[freeQ[#] && !mineQ[#] &]]];
                        
  dispatch["RandomClick", _:False] :=
    With[{reasoningMineQ = Function[AnyTrue[neighbors[#, clicked], Function[markRemains[#] == Length@neighbors[#, freeQ]]]]},
      click@randomCell[freeQ[#] && !reasoningMineQ[#] &]];

  solve0[k_, lst_, remains_] := Block[{C},
    With[{involved = Fold[Union[#1, neighbors[#2, clicked]]&, {}, lst]},
      With[{vars = C /@ (Fold[Union[#1, neighbors[#2, freeQ]]&, {}, involved] ~Union~ lst)},
        With[{eqn = Map[Total[C/@neighbors[#, freeQ]] == markRemains[#]&, involved]
                    ~Append~ If[remains < 0, Nothing, Total[vars] == remains]
                    ~Append~ (0 <= vars <= 1)},
          With[{sol = Solve[eqn, vars, Integers]},
            With[{unify = If[Length[sol] == 0, {}, Normal[Total[Association/@sol] / Length[sol]]]},
              trace[If[Count[unify, _->0|1] > 0, {eqn, unify} /. C[{x_,y_}] :> Subscript[C,x,y] // Column]];
              k[unify /. C[pos_] :> pos]
            ]]]]]];
  
  solve[k_, priori_:False][cell_] := With[{v = neighbors[cell, freeQ]},
    Which[
      !clicked[cell] || Length[v] == 0,
        k[{}],
      priori && markRemains[cell] == 0,
        k@Thread[v -> 0],
      priori && markRemains[cell] == Length[v],
        k@Thread[v -> 1],
      True,
        solve0[k, v, -1]
    ]
  ];
      
  dispatch["Solve", OptionsPattern[{Greedy -> False, ClickOnly -> False}]] :=
    With[{clickOnly = OptionValue[ClickOnly]},
      With[{
        k = If[clickOnly,
              Function[With[{clicks = Cases[#, (pos_->0) :> pos]},
                Length[clicks] != 0 && (Scan[click, Sow@clicks]; True)]],
              Function[With[{clicks = Cases[#, (pos_->0) :> pos], marks = Cases[#, (pos_->1) :> pos]},
                Length[clicks]+Length[marks] != 0 && (Scan[click, Sow@clicks]; Scan[mark, Sow@marks]; True)]]
            ]
        },
        If[OptionValue[Greedy],
          AnyTrue[Array[solve[k, clickOnly]@*List, {rows, cols}], Identity, 2],
          AnyTrue[Array[List, {rows, cols}], solve[k, clickOnly], 2]
        ]
        || (minesRemaining < 5 && solve0[k, Join@@Array[If[freeQ[{##}], {##}, Nothing]&, {rows, cols}], minesRemaining])
      ]
    ];

  dispatch["Solve", cell_] := solve[Identity][cell];
      
  reset[rows0, cols0, mines0, sample0];
  Dispatcher[dispatch]
]

Module[{fgcolor, bgcolor, image},
  fgcolor = <|1->Blue, 2->Darker@Green, 3->Red, 4->Darker@Blue, 5->Magenta, 6->Darker@Cyan, 7->Darker@Yellow, 8->Black|>;
  
  bgcolor["x"|"w"|_Integer] = LightBlue;
  bgcolor["X"] = Red;
  bgcolor[_] = LightGray;
  
  image["x"|"X"] = Image[CompressedData["
1:eJzFl89LAkEUx4dMCPTQxR+VRSH4N8SKUN26ViJoKIoWVlpZEunBg96jW1Fk
dOxe9pfpIWOa77Zjy7Srs7qbD76y7s7O5+28NzNv1nKnOwczhJCLOfazk73a
rFSy17vz7E+8fFE8LBfy2+XLwmGhsp5zsZurmmbJxBbVNA27YaKabqbA7+r4
XZv6RBTPmZ6Ztka0pYKG2YbW57nGMLMzXX+fTAnh+QpTienDgI97J0zLwjsJ
rS/e7mwIvy30yX1YYrpn6uO+2+2mwWCQRiIR6vf7RT++mF7JT4qLbKoxzGzL
oD2YPVyHw2GaTCZpo9GgrVZLVa1WE/n6nOgbfM/GED5sT/TB6/XSTCYzYDab
TVoqlWg6nabRaNSMLwq+pEaw9T6ovgcCAVqtVlVuvV6nsVhM9UeSqY+JLBsW
YuqBw9n5fJ56PB6rXC7Eb9EC/wnv8TEH2+VyjcvmepBkY471kWt8zCf4bjH+
IQl+Ge1TqZTKR7xtYHMdS/A7mN+YY8jzMXJtmN5MmNg/Bus51hZ8O+aYjWy9
uuR3z1LE51jXwMf8dojPpRjxfT6fmneKovwH/8/4/4P04y9ax0GuWf7p7cRB
flGCj/1b3LfsEPY0mfUH9ugA/06SDUO9YWc+oq8FC/w4sTcG2H/3LbDFGsgO
9SV8MKu/xonFOPVX26A9akgf063kuGCsX8hPvK3Wn6Pqb8wf7KHvBlysLUfk
7xyzUn9P+/xhxZw4f1mxaZ8/YROfv78BKw95gA==
"], "Byte", ColorSpace -> "RGB", Interleaving -> True]; 

  image["m"] = Image[CompressedData["
1:eJzt188rg3EcwPGv+XnAFFFOHJw4zEE5KTeu01PitGWWy1abEklJTm4OTq6E
k4N/wM+zA4WVnNgUIQeKzftr3+VraXls++7g+dZr9Tz1fN7f5+nZau3+sHfc
JYSI1vDh9U31RyK+6aEGDqxQdCIYCowNhiYDwUCk11/OyTalQjgrz9WtlGJ5
8K54TEVTQlRhdFWIWPrwk2Wg24olXOB566tdtD6DXRjALm6QREoqZp+BzVjE
OR4yTV0x+gzqwTau8PZTt9B9LnZjBme4z9UsUr8Oc4jJ98p0X9uHfNcsHOPO
dD9rL33YQ6IUfW0fHdjEtf7dM9XX9tGCZVzi1XRf20ctZuU+NoR4Md3X9lEZ
FmKlVH21LKfv9J2+sX4XFnCIE8S1flydO8A8OgvULMOImn0rvv/m5pJQ1wyr
GX9ZTSJ9P082utkesY9Gm203TpHMo50hZ8hnUW+jvy7S/yvybWfIWWu/bFdj
B0cFJmdW2XgG/2J9AD9hZlo=
"], "Byte", ColorSpace -> "RGB", Interleaving -> True]; 

  image["w"] = Image[CompressedData["
1:eJzFl01vUkEUhqcqrQsWbiD4GQ0Jv8FcQlK7c9Ok1aYJGEhBbKiFGmpihAVN
IHHZuNNoXLh0axR/gMZ/RBcCx/NyL3iZztyZqVZO+kKZO3OeMzNnPu6d8sHm
kwtCiMPL/LG58+Jes7nz8sEV/rHVOKztNqqP7zeeV3erzbvli1x4O9AlFvEf
a8SqCHfLBnIysAImSd8uMRz7riY6/gt2OfR7zKpZuhqE+ANLtpIll78S4icX
r5ndzSnKVjtC/Bj7TGU/EcP0OX/THsfDxduSn1usOuubgo+yfdZNqc02T+po
5HMjx/hIiO/TelIM11lvWUOwYrEYpVIpymQylEwm5TjQ5pPwU3yODZ/B2Ops
Ta4fxHAC3+l0mvL5PHU6Her1ehO1Wi2ZP8sJja/VCD7sodzu2coKlUqlGbPb
7VK9XqdisUjZbFbJL7MU7IKBrY2hv75O7XabcrkcxeNxXZ+VbJ7ssQMbdoN1
UuN+j5eWZn4OlpcjuRo2yjF/1xz4H+ALY/55Y2Muhpo7e6p3lmyssSFyDXON
Mcc4RPi1YUPDYFxN1kD9QqEw4WO+Tf4t2FM9teD3sb6xxpDn4VxTcRzY0BcN
E+fHbD/H3oK+Y43JPioh3kj6v2LIzZAG4s+Z5cnPsa+Bj/Wtah+O4QzssDwV
P5FITPLO8zxjnluOexT/1PhH6RzGX7a+rt055l/Y9m3ZNs8k2dxtcH4PbdkO
dX4Ju/0H9v4MfTPVfWPJhuG+MXCc26gYkG9XHfhb8vnrsr405++j/8HWxRDc
P0wx6O5fVnuDJOf715H+/plgvRZ+Dpu4aPNR+PNtff8ku/s31g/O0K8KLvaW
PXF6jRnv37TA9w+ZLccWYf/k/YvEwt8/F/r+/Ru8/pqH
"], "Byte", ColorSpace -> "RGB", Interleaving -> True];

  MinesweeperPlotter[grid_] := Module[{item, colorMap, dispatch},
    item[t:"x"|"X"|"m"|"w"] := image[t];
    item[n_Integer /; n!=0] := Style[n, fgcolor[n], FontFamily->"Arial", FontSize->12, Bold];
    item[_] = " ";
  
    colorMap[highlights_] :=
      With[{seq = Join@@Map[Thread, highlights /. x_RGBColor :> (x&)]},
        If[Length[seq] == 0, bgcolor[#1]&,
           With[{colorRules = SparseArray[seq, {grid.Rows, grid.Cols}, bgcolor]},
             Function[colorRules[[Sequence@@#2]][#1]]]]];
      
    dispatch["PlotBoard", highlights_:{}] := 
      With[{board = grid.Show, bg = colorMap[highlights]},
        Grid[
          MapIndexed[Item[item[#1], Background -> bg[##]]&, board, {2}],
          Frame -> All,
          FrameStyle -> GrayLevel[GoldenRatio-1],
          ItemSize -> {1.2, 1.2},
          Spacings -> {0.7, 0.7},
          Alignment -> {Center, Center}
        ] ~MouseAppearance~ "Arrow"
      ];
  
    dispatch["MousePos"] :=
      Floor[{grid.Rows+1, 1} + {-grid.Rows, grid.Cols} * Reverse@MousePosition["EventHandlerScaled"]];
  
    Dispatcher[dispatch]
  ];
  
  MinesweeperPlotter2[grid_] := Module[{coord, item, dispatch},
    coord[cell_] := {-0.5, grid.Rows + 0.5} + {1,-1}*Reverse[cell];
    dispatch["MousePos"] := {grid.Rows, 1} + {-1,1}*Reverse@Floor@MousePosition["Graphics"];
  
    item[t:"x"|"X"|"m"|"w", cell_] := Inset[image[t], coord[cell], Center, 0.6];
    item[n_Integer /; n!=0, cell_] := Text[Style[n, fgcolor[n], Bold, Larger], coord[cell]];
    item[_,_] = Nothing;

    dispatch["PlotBoard", highlights_:{}] := With[{board = grid.Show},
      ArrayPlot[
        MapIndexed[#2 /. Append[Join@@Map[Thread, highlights], _ -> #1]&, board, {2}],
        Epilog -> MapIndexed[item, board, {2}],
        ColorFunction -> bgcolor, ColorFunctionScaling -> False,
        ImageSize -> 20{grid.Cols, grid.Rows},
        Mesh -> All
      ]];

    Dispatcher[dispatch]
  ];
];

Minesweeper[] := DynamicModule[{
    grid, plotter,
    autoSolve = False, uncertain = "Guess", greedy = False, clickOnly = False, 
    safe = {}, cheats = {}, solved = {},
    reset, solve, step, options
  },

  grid = MakeMinesweeper[16, 16, 40];
  plotter = MinesweeperPlotter[grid];

  reset[args___] := (
    grid.Reset[args];
    autoSolve = False; 
    safe = cheats = solved = {};
  );

  solve[] := (
    Which[
      grid.Boomed || grid.Success,
        autoSolve = False,
      grid."Solve"[Greedy -> greedy, ClickOnly -> clickOnly],
        Null,
      uncertain == "Cheat" || !grid.Started,
        AppendTo[cheats, grid.RandomClick[True]],
      uncertain == "Guess",
        AppendTo[cheats, grid.RandomClick[False]],
      True,
        autoSolve = False
    ];
    plotter.PlotBoard[{cheats->LightRed}]
  );

  step[] := (
    Which[
      grid.Boomed || grid.Success,
        Null,
      First@Reap[grid."Solve"[Greedy -> greedy, ClickOnly -> clickOnly], _, (solved = Join@@#2)&],
        Null,
      True,
        AppendTo[cheats, grid.RandomClick[True]]
    ];
  );
  
  options[] :=
    CreateDialog[{
      "When uncertain: ", RadioButtonBar[Dynamic@uncertain, {"Guess", "Cheat", "Pause"}],
      Row[{Checkbox[Dynamic@greedy], " Greedy mode "}],
      Row[{Checkbox[Dynamic@clickOnly], " No Flags "}],
      DefaultButton[]
    }, Modal->True, WindowTitle->"Options"];  
    
  Panel@Grid[{
    {
      Item[Row[{
        Button["Restart", reset[CurrentValue["AltKey"]]], 
        Button["Solve",   autoSolve = True; solved = {}],
        Button["Step",    step[]],
        Button["Options", options[]]
      }], Alignment->Center],
      SpanFromLeft
    },

    {
      EventHandler[
        Dynamic@If[autoSolve,
          Refresh[solve[], UpdateInterval -> 0.1, TrackedSymbols -> {}],
          plotter.PlotBoard[{safe->LightBlue, cheats->LightRed, solved->LightGreen}]
        ], {
          {"MouseDown", 1} :>
            (safe = grid.Safe[plotter.MousePos]; autoSolve = False; solved = {}),
          {"MouseDragged", 1} :>
            (safe = grid.Safe[plotter.MousePos]),
          {"MouseUp", 1} :>
            (safe = {}; grid.Click[plotter.MousePos, CurrentValue["AltKey"]]),
          {"MouseUp", 2} :>
            (grid.Mark[plotter.MousePos]; autoSolve = False; solved = {})
        }
      ],
      SpanFromLeft
    },

    {
      Item[Dynamic@If[grid.Success, "Success!", grid.MinesRemaining], Alignment->Left, ItemSize->10],
      Item[Dynamic@Which[
        !grid.Started, 
          0,
        grid.Boomed || grid.Success,
          NumberForm[grid.TimeUsed, {Infinity, 2}],
        True,
          Refresh[Round[grid.TimeUsed], UpdateInterval->0.5]
      ]],
      Item[Dynamic@If[Length@cheats > 1, "Guess: "<>ToString[Length[cheats]-1], ""], Alignment->Right, ItemSize->10]
    },

    {
      Item[Row[{
        Button["Beginner", reset[9, 9, 10]],
        Button["Intermediate", reset[16, 16, 40]],
        Button["Expert", reset[16, 30, 99]]
      }], Alignment->Center],
      SpanFromLeft
    }
  }],

  SaveDefinitions -> True,
  Deinitialization :> reset[]
];

End[];
EndPackage[];

