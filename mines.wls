#!/usr/bin/env wolframscript

SetDirectory[DirectoryName@ExpandFileName@First@$ScriptCommandLine];
Needs["Minesweeper`"];

If[DirectoryQ[FileNameJoin[{Directory[], "Minesweeper", "Java"}]],
  JLink`AddToClassPath[FileNameJoin[{Directory[], "Minesweeper", "Java"}]]
];

Game[];
