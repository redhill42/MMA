#!/usr/bin/env wolframscript

SetDirectory[DirectoryName@ExpandFileName@First@$ScriptCommandLine];

Needs["JLink`"];
Needs["Minesweeper`"];

MinesweeperJ[] := JavaBlock@Module[{
    board, frm, panel, statusbar, resetbar,
    cellSize, safe = {}, refresh,
    mouseDown, mouseUp, mouseDragged, translateCoords
  },

  InstallJava[];
  AddToClassPath[FileNameJoin[{Directory[], "java"}]];
  LoadClass["java.awt.BorderLayout"];

  cellSize  = minesweeper`BoardPanel`CELLUSIZE;
  board     = MakeMinesweeper[16, 16, 40];
  frm       = JavaNew["com.wolfram.jlink.MathFrame", "Minesweeper"];
  panel     = JavaNew["minesweeper.BoardPanel"];
  statusbar = JavaNew["java.awt.Label", ""];

  resetbar[] :=
    With[{pane = JavaNew["java.awt.Panel", JavaNew["java.awt.FlowLayout"]]},
      Scan[With[{
          btn = JavaNew["java.awt.Button", First[#]],
          l   = JavaNew["com.wolfram.jlink.MathActionListener", ToString[board]<>"@reset"<>Last[#]]
        },
        btn@addActionListener[l];
        pane@add[btn]
      ]&, {"Beginner"->"[9,9,10]", "Intermediate"->"[16,16,40]", "Expert"->"[16,30,99]"}];
      pane
    ];

  frm@setLocationRelativeTo[Null];
  frm@setLayout[JavaNew["java.awt.BorderLayout"]];
  frm@add[panel, ReturnAsJavaObject[BorderLayout`CENTER]];
  frm@add[statusbar, ReturnAsJavaObject[BorderLayout`SOUTH]];
  frm@add[resetbar[], ReturnAsJavaObject[BorderLayout`SOUTH]]
  frm@setResizable[False];

  refresh[_:Null] := JavaBlock[
    With[{dim = JavaNew["java.awt.Dimension", cellSize*board@cols, cellSize*board@rows]},
      If[!panel@isPreferredSizeSet[] || !dim@equals[panel@getPreferredSize[]],
        panel@setPreferredSize[dim];
        frm@pack[]
      ]
    ];

    panel@update[MapIndexed[If[MemberQ[safe, #2], "0", ToString[#1]]&, board@show, {2}]];
    statusbar@setText[If[board@success, "Success!", ToString[board@minesRemaining]]];
  ];

  board@attach[refresh];
  refresh[];

  translateCoords[x_, y_, f_] :=
    With[{pos = Floor[{y,x}/cellSize] + {1,1}},
      If[And@@Thread[1<=pos<={board@rows,board@cols}], f[pos]];
    ];

  mouseDown[evt_,x_,y_,_] :=
    JavaBlock[
      translateCoords[x, y,
        Switch[evt@getButton[],
          1, safe = board@safe[#]; refresh[],
          3, board@mark[#]
        ]&
      ];
      ReleaseJavaObject[evt]
    ];

  mouseDragged[evt_,x_,y_,_] :=
    JavaBlock[
      translateCoords[x, y,
        Switch[evt@getButton[],
          1, safe = board@safe[#]; refresh[]
        ]&
      ];
      ReleaseJavaObject[evt]
    ];

  mouseUp[evt_,x_,y_,_] :=
    JavaBlock[
      translateCoords[x, y,
        Switch[evt@getButton[],
          1, safe = {}; refresh[]; board@click[#],
          2, board@reset
        ]&
      ];
      ReleaseJavaObject[evt]
    ];

  With[{listener = JavaNew["com.wolfram.jlink.MathMouseListener"]},
    listener@setHandler["mousePressed", ToString[mouseDown]];
    listener@setHandler["mouseReleased", ToString[mouseUp]];
    panel@addMouseListener[listener]];
  With[{listener = JavaNew["com.wolfram.jlink.MathMouseMotionListener"]},
    listener@setHandler["mouseDragged", ToString[mouseDragged]];
    panel@addMouseMotionListener[listener]];

  JavaShow[frm];
  frm@setModal[];
  DoModal[]
];

MinesweeperJ[]
