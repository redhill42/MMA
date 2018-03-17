(* Compile java source code and place the jar into Java directory *)
Begin["Minesweeper`Private`"]
With[{base = FileNameJoin[{ParentDirectory@DirectoryName[$InputFileName], "Java"}]},
  With[{
      source = FileNameJoin[{base, "src", "main", "java"}],
      resources = FileNameJoin[{base, "src", "main", "resources"}],
      target = FileNameJoin[{base, "minesweeper.jar"}]
    },
    If[!FileExistsQ[target],
      With[{tmpdir = CreateDirectory[]},
        Print["Compiling java source files into " <> tmpdir];
        Scan[If[DirectoryQ[#], CopyDirectory[#, FileNameJoin[{tmpdir, FileNameTake[#]}]]]&,
             FileNames["*", resources]];
        Run["javac", "-d", tmpdir, "-g", Sequence@@FileNames["*.java", source, Infinity]];
        Run["jar", "cf", target, "-C", tmpdir, "."];
        DeleteDirectory[tmpdir, DeleteContents->True];
      ]
    ];
  ]];
End[]

Get["Minesweeper`Minesweeper`"];
Get["Minesweeper`Game`"];
