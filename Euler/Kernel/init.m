(* Compile java source code and place the jar into Java directory *)
Begin["Euler`Private`"]
With[{base = FileNameJoin[{ParentDirectory@DirectoryName[$InputFileName], "Java"}]},
  With[{
      source = FileNameJoin[{base, "src", "main", "java"}],
      target = FileNameJoin[{base, "euler.jar"}]
    },
    If[!FileExistsQ[target],
      With[{tmpdir = CreateDirectory[]},
        Run["javac", "-d", tmpdir, "-g", Sequence@@FileNames["*.java", source, Infinity]];
        Run["jar", "cf", target, "-C", tmpdir, "."];
        DeleteDirectory[tmpdir, DeleteContents->True];
      ]
    ];
  ]];
End[]

Get["Euler`Euler`"];
Get["Euler`kempnerSums`"];
