#!/usr/bin/env wolframscript

SetDirectory[DirectoryName@ExpandFileName@First@$ScriptCommandLine];

Needs["JLink`"];
Needs["Minesweeper`"];

MinesweeperJ[] := JavaBlock@Module[{
    board, frame, minefield,
    toolbar, resetbar, remainsPane, timePane, timeCb,
    cellSize, safe = {}, refresh,
    advanced, toggleAdvanced, advancedBtn, stick,
    uncertain = "Guess", greedy = False, clickOnly = False,
    reset, solve, step, solving = False, guesses, guessLabel,
    mouseDown, mouseUp, mouseDragged, translateCoords,
    code, button, checkbox
  },

  InstallJava[];
  AddToClassPath[FileNameJoin[{Directory[], "java"}]];
  LoadClass["java.awt.BorderLayout"];
  LoadClass["java.awt.GridBagConstraints"];
  LoadClass["javax.swing.GroupLayout$Alignment"];

  cellSize  = minesweeper`BoardPanel`CELLUSIZE;
  board     = MakeMinesweeper[16, 16, 40];
  frame     = JavaNew["com.wolfram.jlink.MathFrame", "Minesweeper"];
  minefield = JavaNew["minesweeper.BoardPanel"];

  SetAttributes[code, HoldFirst];
  code[t_] := ToString@Unevaluated[t];

  button[label_, action_] :=
    With[{
        btn = JavaNew["java.awt.Button", label],
        l   = JavaNew["com.wolfram.jlink.MathActionListener", action]
      },
      btn@addActionListener[l];
      btn
    ];

  checkbox[label_, state_, action_] :=
    With[{
        box = JavaNew["java.awt.Checkbox", label, state],
        l   = JavaNew["com.wolfram.jlink.MathItemListener", action]
      },
      box@addItemListener[l];
      box
    ];

  checkbox[label_, group_, state_, action_] :=
    With[{box = checkbox[label, state, action]},
      box@setCheckboxGroup[group];
      box
    ];

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

      With[{buttons = JavaNew["java.awt.Panel", JavaNew["java.awt.FlowLayout"]]},
        buttons@add[button["Restart", code[reset[]]]];
        buttons@add[advancedBtn = button[">>", code[toggleAdvanced[]]]];
        c@gridx = 1;
        c@fill = GridBagConstraints`NONE;
        c@anchor = GridBagConstraints`CENTER;
        c@weightx = 1;
        pane@add[buttons, c];
      ];

      timeCb[] := Round[board@timeUsed];
      With[{cb = ImplementJavaInterface["minesweeper.ValueCallback", {"value"->ToString[timeCb]}]},
        timePane = JavaNew["minesweeper.DigitPane", cb];
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
      Scan[pane@add[button@@##]&, {
        "Beginner"     -> code[reset[9,9,10]],
        "Intermediate" -> code[reset[16,16,40]],
        "Expert"       -> code[reset[16,30,99]]
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

  reset[args___] :=
    If[!solving,
      board@reset[args];
      guesses = 0;
      guessLabel@setText[""];
    ];

  solve[] /; solving := Null;
  solve[] :=
    SessionSubmit@ScheduledTask[
      solving = True;
      While[solving,
        Which[
          board@boomed || board@success,
            solving = False,
          !board@started,
            board@randomClick,
          board@solve[Greedy->greedy, ClickOnly->clickOnly],
            Null,
          uncertain == "Pause",
            solving = False,
          True,
            board@randomClick[uncertain=="Cheat"];
            guesses++;
            guessLabel@setText["Guess: "<>ToString[guesses]]
        ];
        Pause[0.1];
      ],
      {0.05}
    ];

  step[] :=
    Which[
      solving,
        Null,
      board@boomed || board@success,
        Null,
      !board@started,
        board@randomClick,
      !board@solve[Greedy->greedy, ClickOnly->clickOnly],
        board@randomClick[True];
        guesses++;
        guessLabel@setText["Guess: "<>ToString[guesses]]
    ];

  translateCoords[x_, y_, f_] :=
    With[{startx = (minefield@getSize[]@width - board@cols*cellSize)/2},
      With[{pos = Floor[{y,x-startx}/cellSize] + {1,1}},
        If[!solving && TrueQ[And@@Thread[1<=pos<={board@rows,board@cols}]], f[pos]];
      ]];

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

  (* Advanced Dialog. *)
  advanced = JavaNew["com.wolfram.jlink.MathFrame", "Advanced"];

  With[{
      uncertainG = JavaNew["java.awt.CheckboxGroup"],
      uncertainF = code@Function[
        JavaBlock[
          If[#2 == 1, uncertain = #1@getSource[]@getLabel[]];
          ReleaseJavaObject[#1]
        ]
      ]
    },

    With[{
        layout       = JavaNew["javax.swing.GroupLayout", advanced],
        solveButton  = button["Solve", code@solve[]],
        stepButton   = button["Step", code@step[]],
        guessBox     = checkbox["Guess", uncertainG, uncertain=="Guess", uncertainF],
        cheatBox     = checkbox["Cheat", uncertainG, uncertain=="Cheat", uncertainF],
        pauseBox     = checkbox["Pause", uncertainG, uncertain=="Pause", uncertainF],
        greedyBox    = checkbox["Greedy mode", greedy, code[(greedy=#2==1; ReleaseJavaObject[#1])&]],
        clickOnlyBox = checkbox["ClickOnly", clickOnly, code[(clickOnly=#2==1; ReleaseJavaObject[#1])&]]
      },

      guessLabel = JavaNew["java.awt.Label"];

      advanced@setLayout[layout];
      layout@setAutoCreateGaps[True];
      layout@setAutoCreateContainerGaps[True];

      layout@setHorizontalGroup[
        layout@createParallelGroup[GroupLayout$Alignment`LEADING]
          @addGroup[layout@createSequentialGroup[]
            @addComponent[solveButton]
            @addComponent[stepButton]
          ]
          @addGroup[layout@createSequentialGroup[]
            @addComponent[guessBox]
            @addComponent[cheatBox]
            @addComponent[pauseBox]
          ]
          @addComponent[greedyBox]
          @addComponent[clickOnlyBox]
          @addComponent[guessLabel]
      ];

      layout@setVerticalGroup[
        layout@createSequentialGroup[]
          @addGroup[layout@createParallelGroup[GroupLayout$Alignment`LEADING]
            @addComponent[solveButton]
            @addComponent[stepButton]
          ]
          @addGroup[layout@createParallelGroup[GroupLayout$Alignment`LEADING]
            @addComponent[guessBox]
            @addComponent[cheatBox]
            @addComponent[pauseBox]
          ]
          @addComponent[greedyBox]
          @addComponent[clickOnlyBox]
          @addComponent[guessLabel]
      ]
  ]];

  advanced@pack[];
  advanced@setResizable[False];
  advanced@onClose[code[toggleAdvanced[]]];

  stick[master_, slave_] :=
    With[{pos = master@getLocation[], size = master@getSize[]},
      pos@translate[size@width, 0];
      slave@setLocation[pos]
    ];

  toggleAdvanced[] := JavaBlock[
    If[advanced@isVisible[],
      advancedBtn@setLabel[">>"]; advanced@setVisible[False],
      advancedBtn@setLabel["<<"]; stick[frame, advanced]; JavaShow[advanced]
    ]];

  JavaShow[frame];
  frame@setModal[];
  DoModal[]
];

MinesweeperJ[]
