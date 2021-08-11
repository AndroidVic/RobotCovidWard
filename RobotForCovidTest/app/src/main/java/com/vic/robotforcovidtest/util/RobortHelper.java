package com.vic.robotforcovidtest.util;

import android.content.Context;
import android.widget.Toast;

import com.vic.robotforcovidtest.AppConstant;
import com.vic.robotforcovidtest.arg.CurrentPosArg;

public class RobortHelper {

    public Context mContext;
    public static RobortHelper instance;

    public RobortHelper(Context mContext) {
        this.mContext = mContext;
    }

    public static RobortHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RobortHelper(context);
        }
        return instance;
    }

    @AppConstant.NAVIGATION_CODE
    public String parseRoute(char code) {
        switch (Character.toLowerCase(code)) {
            case 'l':
                return AppConstant.NAV_LEFT;
            case 'r':
                return AppConstant.NAV_RIGHT;
            case 'm':
                return AppConstant.NAV_MOVE;
        }
        return null;
    }

    @AppConstant.CURRENT_POS
    public String parseCompass(String code) {
        switch (code.toLowerCase()) {
            case "n":
                return AppConstant.COMPASS_NORTH;
            case "s":
                return AppConstant.COMPASS_SOUTH;
            case "e":
                return AppConstant.COMPASS_EAST;
            case "w":
                return AppConstant.COMPASS_WEST;
        }
        return null;
    }

    public CurrentPosArg getCurrentPosForNavigation(CurrentPosArg posArg, @AppConstant.NAVIGATION_CODE String navigationCode) {
        if (navigationCode != AppConstant.NAV_MOVE) {//If Move , calculate the current coordinates
            String currentCompass = getCurrentCompass(posArg.cPosition, navigationCode);
            posArg.cPosition = currentCompass;
            return posArg;
        } else {// if L or R ,calculate the rotation and get the facing compass position
            return getMovedPosition(posArg);

        }
    }

    public boolean verifyRoute(String navigationText) {
        boolean parseOK = true;
        for (char c : navigationText.toCharArray()) {
            String route = parseRoute(c);
            if (route == null) {
                parseOK = false;
                break;
            }
        }
        return parseOK;
    }

    private CurrentPosArg getMovedPosition(CurrentPosArg cPosition) {
        switch (cPosition.cPosition) {
            case AppConstant.COMPASS_NORTH:
                cPosition.cy += 1;
                break;
            case AppConstant.COMPASS_SOUTH:
                cPosition.cy -= 1;
                break;
            case AppConstant.COMPASS_EAST:
                cPosition.cX += 1;
                break;
            case AppConstant.COMPASS_WEST:
                cPosition.cX -= 1;
                break;
        }
        return cPosition;
    }

    private @AppConstant.CURRENT_POS
    String getCurrentCompass(String cPosition, String navigationCode) {
        switch (cPosition) {
            case AppConstant.COMPASS_NORTH:
                if (navigationCode == AppConstant.NAV_RIGHT) {
                    return AppConstant.COMPASS_EAST;
                } else {
                    return AppConstant.COMPASS_WEST;
                }
            case AppConstant.COMPASS_SOUTH:
                if (navigationCode == AppConstant.NAV_RIGHT) {
                    return AppConstant.COMPASS_WEST;
                } else {
                    return AppConstant.COMPASS_EAST;
                }
            case AppConstant.COMPASS_EAST:
                if (navigationCode == AppConstant.NAV_RIGHT) {
                    return AppConstant.COMPASS_SOUTH;
                } else {
                    return AppConstant.COMPASS_NORTH;
                }
            case AppConstant.COMPASS_WEST:
                if (navigationCode == AppConstant.NAV_RIGHT) {
                    return AppConstant.COMPASS_NORTH;
                } else {
                    return AppConstant.COMPASS_SOUTH;
                }
        }
        return null;
    }
}
