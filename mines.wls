#!/usr/bin/env wolframscript

SetDirectory[DirectoryName@ExpandFileName@First@$ScriptCommandLine];

Needs["JLink`"];
Needs["Minesweeper`"];

MinesweeperJ[] := JavaBlock@Module[{
    board, frame, minefield,
    toolbar, solverbar, resetbar, button,
    remainsPane, timePane, timeCb,
    cellSize, safe = {}, refresh,
    reset, solve, step, solving = False,
    mouseDown, mouseUp, mouseDragged, translateCoords
  },

  InstallJava[];
  AddToClassPath[FileNameJoin[{Directory[], "java"}]];
  LoadClass["java.awt.BorderLayout"];
  LoadClass["java.awt.GridBagConstraints"];

  cellSize  = minesweeper`BoardPanel`CELLUSIZE;
  board     = MakeMinesweeper[16, 16, 40];
  frame     = JavaNew["com.wolfram.jlink.MathFrame", "Minesweeper"];
  minefield = JavaNew["minesweeper.BoardPanel"];

  toolbar[] :=
    With[{
        pane = JavaNew["java.awt.Panel", JavaNew["java.awt.GridBagLayout"]],
        c    = JavaNew["java.awt.GridBagConstraints"]
      },

      remainsPane = JavaNew["minesweeper.DigitPane"];
      c@gridx = 0;
      c@fill = GridBagConstraints`NONE;
      c@anchor = GridBagConstraints`WEST;
      c@weightx = 0.1;
      pane@add[remainsPane, c];

      c@gridx = 1;
      c@fill = GridBagConstraints`NONE;
      c@anchor = GridBagConstraints`CENTER;
      c@weightx = 1;
      pane@add[solverbar[], c];

      timeCb[] := Round[board@timeUsed];
      With[{cb = ImplementJavaInterface["minesweeper.ValueCallback", {"value" -> ToString[timeCb]}]},
        timePane = JavaNew["minesweeper.DigitPane", cb];
        c@gridx = 2;
        c@fill = GridBagConstraints`NONE;
        c@anchor = GridBagConstraints`EAST;
        c@weightx = 0.1;
        pane@add[timePane, c];
      ];

      pane
    ];

  button[name_, action_] :=
    With[{
        btn = JavaNew["java.awt.Button", name],
        l   = JavaNew["com.wolfram.jlink.MathActionListener", action]
      },
      btn@addActionListener[l];
      btn
    ];

  solverbar[] :=
    With[{pane = JavaNew["java.awt.Panel", JavaNew["java.awt.FlowLayout"]]},
      Scan[pane@add[button@@##]&, {
        "Restart" -> ToString[reset]<>"[]",
        "Solve"   -> ToString[solve]<>"[]",
        "Step"    -> ToString[step]<>"[]"
      }];
      pane
    ];

  resetbar[] :=
    With[{pane = JavaNew["java.awt.Panel", JavaNew["java.awt.FlowLayout"]]},
      Scan[pane@add[button@@##]&, {
        "Beginner"     -> ToString[reset]<>"[9,9,10]",
        "Intermediate" -> ToString[reset]<>"[16,16,40]",
        "Expert"       -> ToString[reset]<>"[16,30,99]"
      }];
      pane
    ];

  frame@setLocationRelativeTo[Null];
  frame@setLayout[JavaNew["java.awt.BorderLayout"]];
  frame@add[minefield, ReturnAsJavaObject[BorderLayout`CENTER]];
  frame@add[toolbar[], ReturnAsJavaObject[BorderLayout`NORTH]];
  frame@add[resetbar[], ReturnAsJavaObject[BorderLayout`SOUTH]];
  frame@setResizable[False];

  refresh[_:Null] := JavaBlock[
    With[{dim = JavaNew["java.awt.Dimension", cellSize*board@cols, cellSize*board@rows]},
      If[!minefield@isPreferredSizeSet[] || !dim@equals[minefield@getPreferredSize[]],
        minefield@setPreferredSize[dim];
        frame@pack[]
      ]
    ];

    minefield@update[MapIndexed[If[MemberQ[safe, #2], "0", ToString[#1]]&, board@show, {2}]];
    remainsPane@setValue[board@minesRemaining];
  ];

  board@attach[refresh];
  refresh[];

  solve[] :=
    If[!solving,
      SessionSubmit@ScheduledTask[
        solving = True;
        While[step[], Pause[0.05]];
        solving = False,
        {0.05}
      ]];

  step[] := !(board@boomed || board@success) && (board@solve || (board@randomClick; True));

  reset[args___] := If[!solving, board@reset[args]];

  translateCoords[x_, y_, f_] :=
    With[{pos = Floor[{y,x}/cellSize] + {1,1}},
      If[!solving && TrueQ[And@@Thread[1<=pos<={board@rows,board@cols}]], f[pos]];
    ];

  mouseDown[evt_,x_,y_,_] :=
    JavaBlock[
      translateCoords[x, y,
        Switch[evt@getButton[],
          1, safe = board@safe[#]; refresh[],
          3, board@mark[#],
          _, Null
        ]&
      ];
      ReleaseJavaObject[evt]
    ];

  mouseDragged[evt_,x_,y_,_] :=
    JavaBlock[
      translateCoords[x, y,
        Switch[evt@getButton[],
          1, safe = board@safe[#]; refresh[],
          _, Null
        ]&
      ];
      ReleaseJavaObject[evt]
    ];

  mouseUp[evt_,x_,y_,_] :=
    JavaBlock[
      translateCoords[x, y,
        Switch[evt@getButton[],
          1, safe = {}; refresh[]; board@click[#],
          _, Null
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

  JavaShow[frame];
  frame@setModal[];
  DoModal[]
];

MinesweeperJ[]
