#!/usr/bin/env wolframscript

SetDirectory[DirectoryName@ExpandFileName@First@$ScriptCommandLine];

Needs["JLink`"];
Needs["Minesweeper`"];

With[{
    source = FileNameJoin[{Directory[], "java", "src", "main", "java"}],
    resources = FileNameJoin[{Directory[], "java", "src", "main", "resources"}],
    target = FileNameJoin[{Directory[], "java", "target"}]
  },
  If[!FileExistsQ[target],
    CreateDirectory[target];
    Scan[If[DirectoryQ[#], CopyDirectory[#, FileNameJoin[{target, FileNameTake[#]}]]]&,
         FileNames["*", resources]
    ];
    Run["javac", "-d", target, Sequence@@FileNames["*.java", source, Infinity]];
  ];

  InstallJava[];
  AddToClassPath[target];
];

JavaBlock@Module[{
    board = MakeMinesweeper[16, 16, 40], jboard,
    rows, cols, mines,
    started, boomed, success,
    remaining, minesRemaining, timeUsed,
    reset, restart, attach, show,
    click, randomClick, mark, safe, solve,
    trash, clean, Boolean
  },

  (* Release temporarily generated java objects *)
  clean[obj_] := (
    ReleaseJavaObject[trash];
    trash = obj
  );

  (* Cached Boolean objects *)
  Boolean[True] = MakeJavaObject[True];
  Boolean[False] = MakeJavaObject[False];

  rows[]                := board@rows;
  cols[]                := board@cols;
  mines[]               := board@mines;
  started[]             := Boolean[board@started];
  boomed[]              := Boolean[board@boomed];
  success[]             := Boolean[board@success];
  remaining[]           := board@remaining;
  minesRemaining[]      := board@minesRemaining;
  timeUsed[]            := board@timeUsed;
  reset[r_,c_,m_]       := board@reset[r,c,m];
  restart[keep_]        := board@reset[keep];
  attach[listener_]     := board@attach[listener@update[]&];
  show[]                := clean@MakeJavaObject[Map[ToString/*ToCharacterCode/*First, board@show, {2}]];
  click[r_,c_,cheat_]   := clean@JavaNew["minesweeper.Cell", board@click[{r,c}, cheat]];
  randomClick[cheat_]   := clean@JavaNew["minesweeper.Cell", board@randomClick[cheat]];
  mark[r_,c_]           := clean@JavaNew["minesweeper.Cell", board@mark[{r,c}]];

  solve[greedy_, clickOnly_, solved_] :=
    If[solved === Null,
      Boolean[board@solve[Greedy->greedy, ClickOnly->clickOnly]],
      Boolean@First@Reap[
        board@solve[Greedy->greedy, ClickOnly->clickOnly],
        _,
        JavaBlock[
          Scan[solved@add[JavaNew["minesweeper.Cell", #]]&, Catenate[#2]];
          ReleaseJavaObject[solved]
        ]&
      ]
    ];

  jboard = ImplementJavaInterface["minesweeper.Board", {
    "rows"              -> ToString[rows],
    "cols"              -> ToString[cols],
    "started"           -> ToString[started],
    "boomed"            -> ToString[boomed],
    "success"           -> ToString[success],
    "remaining"         -> ToString[remaining],
    "minesRemaining"    -> ToString[minesRemaining],
    "timeUsed"          -> ToString[timeUsed],
    "reset"             -> ToString[reset],
    "restart"           -> ToString[restart],
    "attach"            -> ToString[attach],
    "show"              -> ToString[show],
    "click"             -> ToString[click],
    "randomClick"       -> ToString[randomClick],
    "mark"              -> ToString[mark],
    "solve"             -> ToString[solve]
  }];

  With[{frame = JavaNew["com.wolfram.jlink.MathJFrame", "Minesweeper"]},
    JavaNew["minesweeper.Minesweeper", frame, jboard];
    JavaShow[frame];
    frame@setModal[];
    DoModal[]
  ]
];
