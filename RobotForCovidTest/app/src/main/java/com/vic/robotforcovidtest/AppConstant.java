package com.vic.robotforcovidtest;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

public class AppConstant {

    public static final String COMPASS_NORTH = "N";
    public static final String COMPASS_SOUTH = "S";
    public static final String COMPASS_EAST = "E";
    public static final String COMPASS_WEST = "W";

    @StringDef({COMPASS_NORTH, COMPASS_SOUTH, COMPASS_EAST, COMPASS_WEST})
    public @interface CURRENT_POS {}



    public static final String NAV_LEFT = "L";
    public static final String NAV_RIGHT = "R";
    public static final String NAV_MOVE = "M";

    @StringDef({NAV_LEFT, NAV_RIGHT, NAV_MOVE})
    public @interface NAVIGATION_CODE {}


}
