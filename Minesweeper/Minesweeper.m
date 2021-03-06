BeginPackage["Minesweeper`", {"Common`"}]

MakeMinesweeper::usage = "Make a new minesweeper board.";
MinesweeperSolver::usage = "Create a minesweeper solver";
MinesweeperPlotter::usage = "Create a minesweeper plotter.";
MinesweeperPlotter2::usage = "An alternate minesweeper plotter.";
Minesweeper::usage = "The default minesweeper game implementation.";

Begin["`Private`"]

MakeMinesweeper[rows0_Integer, cols0_Integer, mines0_Integer, sample0_List:{}] := Module[{
	  rows, cols, mines,
    coords, board, clicked, marked, boomed, success, remaining, minesRemaining,
    mineQ, freeQ, markRemains, neighbors, calcNeighbors,
    click, mark, safe, show,
    observers = {}, recurse, update, attach, detach, notify,
    startTime, stopTime, inited, batch,
    reset, restart, solver, instruction, dispatch
  },

  mineQ = board[#] == "x" &;
  freeQ = !(clicked[#] || marked[#])&;
  markRemains = board[#] - Length@neighbors[#, marked]&;
  success := remaining == 0;

  attach[ob_] := (If[!MemberQ[observers, ob], AppendTo[observers, ob]]; Null);
  detach[ob_] := (observers = DeleteCases[observers, ob]; Null);
  notify[] := Scan[#[dispatch]&, observers];

  neighbors[cell_, crit_] :=
    Select[
      cell+#& /@ Tuples[{-1,0,1},2], 
      #!=cell && And@@Thread[1<=#<={rows,cols}] && crit[#] &
    ];

  reset[rows1_, cols1_, mines1_, sample_:{}] := (
    {rows, cols, mines} = {rows1, cols1, mines1};

    Clear[board];
    board[_] = 0;
    coords = Catenate@CoordinateBoundsArray[{{1,rows}, {1,cols}}];
    inited = False;
    restart[];

    If[Length[sample] != 0,
      (* Fill grid with sample data. *)
      mines = minesRemaining = Length[sample];
      remaining = rows*cols-mines;
      Scan[(board[#] = "x")&, sample];
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
    recurse = update = 0;
    notify[];
  );
  
  calcNeighbors[] :=
    Scan[If[!mineQ[#], board[#] = Length@neighbors[#, mineQ]]&, coords];

  SetAttributes[batch, HoldAll];
  batch[blk_] := batch[Nothing, blk];
  batch[init_, blk_] := (
    recurse++;
    If[startTime == 0,
      startTime = SessionTime[];
      If[!inited,
        (* Generate a random mine board. *)
        With[{excludes = If[init =!= Nothing, neighbors[init, True&] ~Append~ init, {}]},
          Scan[(board[#] = "x")&, RandomSample[coords ~Complement~ excludes, mines]];
        ];
        calcNeighbors[];
        inited = True;
      ]
    ];

    With[{res = blk},
      If[stopTime==0 && (boomed||success),
        stopTime = SessionTime[]
      ];
      If[--recurse == 0 && update > 0,
        update = 0;
        notify[]
      ];
      res
    ]
  );

  click[cell_] := batch[cell,
    Which[
      (* Do nothing after boomed or success. *)
      boomed || success,
        Null,
        
      (* Click on a mine cell will boom. *)
      mineQ[cell],
        clicked[cell] = True;
        boomed = True;
        update++,
        
      (* Click on a incorrectly marked cell will boom. *)
      marked[cell],
        boomed = True;
        update++,
        
      (* Click on a clicked cell will click neighbor cells if the cell is safe. *)
      clicked[cell],
        If[markRemains[cell] == 0, Scan[click, neighbors[cell, freeQ]]],
 
      (* Click a cell will flip the cell and cascade click neighbors
         cells if the cell has no mine neighbors. *)       
      True,
        clicked[cell] = True;
        remaining--;
        update++;
        If[board[cell] == 0, Scan[click, neighbors[cell, freeQ]]]
    ];
    cell
  ];

  mark[cell_] := batch[
    If[!(boomed||success) && !clicked[cell],
      If[marked[cell] = !marked[cell], minesRemaining--, minesRemaining++];
      update++
    ];
    cell
  ];

  safe[cell_ /; boomed||success||marked[cell]] = {};
  safe[cell_ ? clicked] := neighbors[cell, freeQ];
  safe[cell_] := {cell};
  
  show[cell_ ? clicked] := If[mineQ[cell], "X", board[cell]];
  show[cell_ ? marked]  := If[mineQ[cell] || !(boomed||success), "m", "w"];
  show[cell_ ? mineQ]   := If[boomed, "x", If[success, "m", " "]];
  show[_] = " ";

  dispatch["rows"] := rows;
  dispatch["cols"] := cols;
  dispatch["mines"] := mines;
  dispatch["started"] := startTime != 0;
  dispatch["boomed"] := boomed;
  dispatch["success"] := success;
  dispatch["remaining"] := remaining;
  dispatch["minesRemaining"] := If[success, 0, minesRemaining];
  dispatch["show"] := Array[show@*List, {rows, cols}];
  dispatch["plot", args___] := MinesweeperPlotter2[dispatch][plotBoard[args]];
  dispatch["attach", ob_] := attach[ob];
  dispatch["detach", ob_] := detach[ob];
  dispatch["click", cell_, True] := If[mineQ[cell], mark[cell], click[cell]];
  dispatch["click", cell_, _:False] := If[!marked[cell], click[cell], cell];
  dispatch["mark", cell_] := mark[cell];
  dispatch["safe", cell_] := safe[cell];
  dispatch["reset", keep_:False] := If[keep, restart[], reset[rows, cols, mines]];
  dispatch["reset", rows1_Integer, cols1_Integer, mines1_Integer] := reset[rows1, cols1, mines1];
  
  dispatch["timeUsed"] := Which[
    startTime == 0, 0,
    stopTime  != 0, stopTime - startTime,
    True,           SessionTime[] - startTime];

  dispatch["randomClick", True] :=
    With[{
        alternative =
          Function[AnyTrue[neighbors[#, clicked],
                           markRemains[#] == 1 && Length@neighbors[#, freeQ] == 2 &]],
        choices = RandomSample@Select[coords, freeQ[#] && !mineQ[#] &]
      },
      click@SelectFirst[choices, alternative, First[choices]]
    ];

  dispatch["randomClick", _:False] :=
    With[{
        alternative =
          Function[AnyTrue[neighbors[#, clicked],
                           markRemains[#] != Length@neighbors[#, freeQ] &]],
        choices = RandomSample@Select[coords, freeQ]
      },
      click@SelectFirst[choices, alternative, First[choices]]
    ];

  solver = AbstractSolver[<|
    "neighbors"   -> neighbors,
    "freeQ"       -> freeQ,
    "clicked"     -> clicked,
    "markRemains" -> markRemains
  |>];

  instruction[_,_][{}] = False;
  instruction[False, False][sol_] :=
    (batch@Scan[If[Last[#] == 0, click[Sow@@#], mark[Sow@@#]]&, sol]; True);
  instruction[True, False][sol_] := With[{clicks = Cases[sol, HoldPattern[_->0]]},
    Length[clicks] != 0 && (batch@Scan[click[Sow@@#]&, clicks]; True)];
  instruction[False, True][sol_] :=
    (Scan[Sow@@#&, sol]; True);
  instruction[True, True][sol_] := With[{clicks = Cases[sol, HoldPattern[_->0]]},
    Length[clicks] != 0 && (Scan[Sow@@#&, clicks]; True)];

  dispatch["instruction", clickOnly_, noAction_] := instruction[clickOnly, noAction];

  dispatch["solve", OptionsPattern[{Greedy->False, ClickOnly->False, NoAction->False}]] :=
    With[{clickOnly = OptionValue[ClickOnly]},
      With[{k = instruction[clickOnly, OptionValue[NoAction]]},
        If[OptionValue[Greedy],
          k@Normal@Fold[
            Join[#1, solver[Association, clickOnly][#2]]&,
            <||>,
            coords],
          AnyTrue[coords, solver[k, clickOnly]]
        ] || (minesRemaining < 5 && solver[k, Select[coords, freeQ], minesRemaining])
      ]];

  reset[rows0, cols0, mines0, sample0];
  Dispatcher[dispatch]
];

AbstractSolver[disp_] :=
  With[{
      neighbors   = disp["neighbors"],
      freeQ       = disp["freeQ"],
      clicked     = disp["clicked"],
      markRemains = disp["markRemains"]
    },

    Module[{solver},
      solver[k_, lst_, remains_] := Block[{C},
        Let[{
            involved = Fold[Union[#1, neighbors[#2, clicked]]&, {}, lst],
            vars     = C /@ (Fold[Union[#1, neighbors[#2, freeQ]]&, {}, involved] ~Union~ lst),
            eqn      = Map[Total[C/@neighbors[#, freeQ]] == markRemains[#]&, involved]
                       ~Append~ If[remains < 0, Nothing, Total[vars] == remains]
                       ~Append~ (0 <= vars <= 1),
            sol      = Solve[eqn, vars, Integers],
            unify    = If[Length[sol] == 0, {}, Normal[Merge[sol, Total] / Length[sol]]]
          },
          verbose[If[Count[unify, _->0|1] > 0, {eqn, unify} /. C[{x_,y_}] :> Subscript[C,x,y] // Column]];
          k[Cases[unify, (C[pos_]->tag:0|1) :> pos->tag]]
        ]];

      solver[k_, priori_:False][cell_?clicked] :=
        With[{v = neighbors[cell, freeQ]},
          Which[
            Length[v] == 0,
              k[{}],
            priori && markRemains[cell] == 0,
              k@Thread[v -> 0],
            priori && markRemains[cell] == Length[v],
              k@Thread[v -> 1],
            True,
              solver[k, v, -1]
          ]];
      solver[k_, _:False][_] := k[{}];

      solver
  ]];

MinesweeperSolver[] :=
  Module[{
      rows = 0, cols = 0, coords, board,
      neighbors, solver, dispatch
    },

    reset[grid_] :=
      With[{dim = Dimensions[grid]},
        If[dim != {rows,cols},
          {rows,cols} = dim;
          coords = Catenate@CoordinateBoundsArray[{{1,rows}, {1,cols}}];
          Clear[board]
        ];
        MapIndexed[(board[#2] = #1)&, grid, {2}]
      ];

    neighbors[cell_, crit_] :=
      Select[
        cell+#& /@ Tuples[{-1,0,1}, 2],
        #!=cell && And@@Thread[1<=#<={rows,cols}] && crit[#] &
      ];

    solver = AbstractSolver[<|
      "neighbors"   -> neighbors,
      "freeQ"       -> Function[board[#] == " "],
      "clicked"     -> Function[IntegerQ[board[#]]],
      "markRemains" -> Function[board[#] - Length@neighbors[#, board[#]=="m"&]]
    |>];

    dispatch["solve", grid_, k_, OptionsPattern[{Greedy->False, ClickOnly->False}]] :=
      With[{clickOnly = OptionValue[ClickOnly]},
        reset[grid];
        If[OptionValue[Greedy],
          k@Normal@Fold[
            Join[#1, solver[Association, clickOnly][#2]]&,
            <||>,
            coords],
          AnyTrue[coords, solver[k, clickOnly]]
        ]
      ];

    Dispatcher[dispatch]
  ];

MinesweeperSolver[board_] :=
  Module[{solver = MinesweeperSolver[], dispatch},
    dispatch["solve", opts:OptionsPattern[{Greedy->False, ClickOnly->False, NoAction->False}]] :=
      solver@solve[
        board@show,
        board@instruction[OptionValue[ClickOnly], OptionValue[NoAction]],
        Sequence@@FilterRules[{opts}, {Greedy,ClickOnly}]
      ];

    Dispatcher[dispatch]
  ];

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

  MinesweeperPlotter[board_] := Module[{item, colorMap, dispatch},
    item[t:"x"|"X"|"m"|"w"] := image[t];
    item[n_Integer /; n!=0] := Style[n, fgcolor[n], FontFamily->"Arial", FontSize->12, Bold];
    item[_] = " ";
  
    colorMap[highlights_] :=
      With[{seq = Catenate@Map[Thread, highlights /. x_RGBColor :> (x&)]},
        If[Length[seq] == 0, bgcolor[#1]&,
           With[{colorRules = SparseArray[seq, {board@rows, board@cols}, bgcolor]},
             Function[colorRules[[Sequence@@#2]][#1]]]]];
      
    dispatch["plotBoard", highlights_:{}] :=
      With[{grid = board@show, bg = colorMap[highlights]},
        Grid[
          MapIndexed[Item[item[#1], Background -> bg[##]]&, grid, {2}],
          Frame -> All,
          FrameStyle -> GrayLevel[GoldenRatio-1],
          ItemSize -> {1.2, 1.2},
          Spacings -> {0.7, 0.7},
          Alignment -> {Center, Center}
        ] ~MouseAppearance~ "Arrow"
      ];
  
    dispatch["mousePos"] :=
      Floor[{board@rows+1, 1} + {-board@rows, board@cols} * Reverse@MousePosition["EventHandlerScaled"]];

    Dispatcher[dispatch]
  ];
  
  MinesweeperPlotter2[board_] := Module[{coord, item, dispatch},
    coord[cell_] := {-0.5, board@rows + 0.5} + {1,-1}*Reverse[cell];
    dispatch["mousePos"] := {board@rows, 1} + {-1,1}*Reverse@Floor@MousePosition["Graphics"];
  
    item[t:"x"|"X"|"m"|"w", cell_] := Inset[image[t], coord[cell], Center, 0.6];
    item[n_Integer /; n!=0, cell_] := Text[Style[n, fgcolor[n], Bold, Larger], coord[cell]];
    item[_,_] = Nothing;

    dispatch["plotBoard", highlights_:{}] :=
      With[{grid = board@show, colorRules = Dispatch[Catenate@Map[Thread, highlights]]},
        ArrayPlot[
          MapIndexed[#2 /. colorRules /. {_,_} -> bgcolor[#1] &, grid, {2}],
          Epilog -> MapIndexed[item, grid, {2}],
          ImageSize -> 20{board@cols, board@rows},
          Mesh -> All
        ]];

    Dispatcher[dispatch]
  ];
];

Minesweeper[] := DynamicModule[{
    board, plotter,
    uncertain = "Guess", greedy = False, clickOnly = False,
    autoSolve = False, safe = {}, cheats = {}, solved = {},
    reset, solve, step, options,
    update = 0, Updater
  },

  board = MakeMinesweeper[16, 16, 40];
  plotter = MinesweeperPlotter[board];

  (* Manually refresh the board when some change has happended *)
  SetAttributes[Updater, HoldFirst];
  Updater[expr_] := Dynamic[update; expr];

  reset[args___] := (
    board@reset[args];
    autoSolve = False;
    safe = cheats = solved = {};
  );

  solve[] := (
    Which[
      board@boomed || board@success,
        autoSolve = False,
      !board@started,
        board@randomClick[],
      board@solve[Greedy -> greedy, ClickOnly -> clickOnly],
        Null,
      uncertain == "Pause",
        autoSolve = False,
      True,
        AppendTo[cheats, board@randomClick[uncertain == "Cheat"]];
    ];
    plotter@plotBoard[{cheats->LightRed}]
  );

  step[] :=
    Which[
      board@boomed || board@success,
        Null,
      !board@started,
        board@randomClick[],
      (solved = Catenate[#2]; #1)& @@ Reap[board@solve[Greedy->greedy, ClickOnly->clickOnly]],
        Null,
      True,
        AppendTo[cheats, board@randomClick[True]]
    ];

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
        Updater@If[autoSolve,
          Refresh[solve[], UpdateInterval -> 0.1, TrackedSymbols -> {}],
          plotter@plotBoard[{safe->LightBlue, cheats->LightRed, solved->LightGreen}]
        ], {
          {"MouseDown", 1} :>
            (safe = board@safe@plotter@mousePos; autoSolve = False; solved = {}),
          {"MouseDragged", 1} :>
            (safe = board@safe@plotter@mousePos),
          {"MouseUp", 1} :>
            (safe = {}; board@click[plotter@mousePos, CurrentValue["AltKey"]]),
          {"MouseUp", 2} :>
            (board@mark@plotter@mousePos; autoSolve = False; solved = {})
        }
      ],
      SpanFromLeft
    },

    {
      Item[Updater@If[board@success, "Success!", board@minesRemaining], Alignment->Left, ItemSize->10],
      Item[Updater@Which[
        !board@started,
          0,
        board@boomed || board@success,
          NumberForm[board@timeUsed, {Infinity, 2}],
        True,
          Refresh[Round[board@timeUsed], UpdateInterval->0.5]
      ]],
      Item[Dynamic@If[Length@cheats > 0, "Guess: "<>ToString@Length@cheats, ""], Alignment->Right, ItemSize->10]
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

  Initialization :> board@attach[update++&]
];

End[]
EndPackage[]
