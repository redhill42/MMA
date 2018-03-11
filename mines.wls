#!/usr/bin/env wolframscript

SetDirectory[DirectoryName@ExpandFileName@First@$ScriptCommandLine];

Needs["JLink`"];
Needs["Minesweeper`"];

MinesweeperJ[] := JavaBlock@Module[{
    board, frm, minefield, resetbar,
    statusbar, minesPane, timePane, timeCb,
    cellSize, safe = {}, refresh,
    mouseDown, mouseUp, mouseDragged, translateCoords
  },

  InstallJava[];
  AddToClassPath[FileNameJoin[{Directory[], "java"}]];
  LoadClass["java.awt.BorderLayout"];
  LoadClass["java.awt.GridBagConstraints"];

  cellSize  = minesweeper`BoardPanel`CELLUSIZE;
  board     = MakeMinesweeper[16, 16, 40];
  frm       = JavaNew["com.wolfram.jlink.MathFrame", "Minesweeper"];
  minefield = JavaNew["minesweeper.BoardPanel"];

  statusbar[] :=
    With[{
        pane = JavaNew["java.awt.Panel", JavaNew["java.awt.GridBagLayout"]],
        c    = JavaNew["java.awt.GridBagConstraints"]
      },

      minesPane = JavaNew["minesweeper.DigitPane"];
      c@gridx = c@gridy = 0;
      c@fill = GridBagConstraints`NONE;
      c@anchor = GridBagConstraints`WEST;
      c@weightx = 0.1;
      pane@add[minesPane, c];

      With[{
          btn = JavaNew["java.awt.Button", "Restart"],
          l   = JavaNew["com.wolfram.jlink.MathActionListener", ToString[board]<>"@reset"]
        },
        btn@addActionListener[l];
        c@gridx = 1;
        c@fill = GridBagConstraints`NONE;
        c@anchor = GridBagConstraints`CENTER;
        c@weightx = 1;
        pane@add[btn, c];
      ];

      timeCb[] := Round[board@timeUsed];
      With[{cb = ImplementJavaInterface["minesweeper.ValueCallback", {"value" -> ToString[timeCb]}]},
        timePane = JavaNew["minesweeper.DigitPane", cb, 4];
        c@gridx = 2;
        c@fill = GridBagConstraints`NONE;
        c@anchor = GridBagConstraints`EAST;
        c@weightx = 0.1;
        pane@add[timePane, c];
      ];

      pane
    ];

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
  frm@add[minefield, ReturnAsJavaObject[BorderLayout`CENTER]];
  frm@add[statusbar[], ReturnAsJavaObject[BorderLayout`NORTH]];
  frm@add[resetbar[], ReturnAsJavaObject[BorderLayout`SOUTH]];
  frm@setResizable[False];

  refresh[_:Null] := JavaBlock[
    With[{dim = JavaNew["java.awt.Dimension", cellSize*board@cols, cellSize*board@rows]},
      If[!minefield@isPreferredSizeSet[] || !dim@equals[minefield@getPreferredSize[]],
        minefield@setPreferredSize[dim];
        frm@pack[]
      ]
    ];

    minefield@update[MapIndexed[If[MemberQ[safe, #2], "0", ToString[#1]]&, board@show, {2}]];
    minesPane@setValue[board@minesRemaining];
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
    minefield@addMouseListener[listener]];
  With[{listener = JavaNew["com.wolfram.jlink.MathMouseMotionListener"]},
    listener@setHandler["mouseDragged", ToString[mouseDragged]];
    minefield@addMouseMotionListener[listener]];

  JavaShow[frm];
  frm@setModal[];
  DoModal[]
];

MinesweeperJ[]
