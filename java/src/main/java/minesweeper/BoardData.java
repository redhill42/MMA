/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import java.awt.Color;
import java.util.List;
import java.util.Map;

public interface BoardData {
    String[][] getField();
    Map<Color,List<Cell>> getColorMap();
}
