/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import java.util.ResourceBundle;

final class LocalStrings {
    private static final ResourceBundle lStrings
        = ResourceBundle.getBundle("minesweeper.LocalStrings");

    private LocalStrings() {}

    public static String _L(String key) {
        return lStrings.getString(key);
    }
}
