package com.vic.robotforcovidtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.vic.robotforcovidtest.arg.CurrentPosArg;
import com.vic.robotforcovidtest.arg.InputArgs;
import com.vic.robotforcovidtest.util.RobortHelper;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private EditText topCordLayer, currentPosLayer, navigationLayer;
    private RobortHelper robortHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        robortHelper = RobortHelper.getInstance(MainActivity.this);

        initFields();

        findViewById(R.id.ID_find_result).setOnClickListener(view -> {
            parseAndCollectInput();
        });

    }

    private void parseAndCollectInput() {
        InputArgs inputArgs = new InputArgs();
        String topCordText = topCordLayer.getText().toString();
        if (topCordText.isEmpty()) {
            topCordLayer.setError("please input the top coordinates");
            return;
        } else {
            StringTokenizer tokens = new StringTokenizer(topCordText, " ");
            if (tokens.countTokens() < 2) {
                topCordLayer.setError("please separate coordinates with space");
                return;
            } else if (tokens.countTokens() > 2) {
                topCordLayer.setError("maximum 2 coordinates possible");
                return;
            } else {
                try {
                    inputArgs.tX = Integer.parseInt(tokens.nextToken());
                    inputArgs.ty = Integer.parseInt(tokens.nextToken());
                } catch (NumberFormatException e) {
                    topCordLayer.setError("Coordinate should be integers (example: 5 5)");
                    return;
                }
            }
        }

        String currentCordText = currentPosLayer.getText().toString();
        if (currentCordText.isEmpty()) {
            currentPosLayer.setError("please input the current position");
            return;
        } else {
            StringTokenizer tokens = new StringTokenizer(currentCordText, " ");
            if (tokens.countTokens() < 3) {
                currentPosLayer.setError("please separate coordinates with space");
                return;
            } else if (tokens.countTokens() > 3) {
                currentPosLayer.setError("current coordinate limit exceeded");
                return;
            } else {
                try {
                    CurrentPosArg currentPosArg = new CurrentPosArg();
                    currentPosArg.cX = Integer.parseInt(tokens.nextToken());
                    currentPosArg.cy = Integer.parseInt(tokens.nextToken());
                    String pos = tokens.nextToken();
                    if (pos.length() > 1) {
                        currentPosLayer.setError("Compass points should be any one of N S E W");
                        return;
                    } else {
                        currentPosArg.cPosition = robortHelper.parseCompass(pos);
                        if (currentPosArg.cPosition == null) {
                            currentPosLayer.setError("Compass code undefined, should be any one of N S E W");
                            return;
                        }
                        inputArgs.posArg = currentPosArg;
                    }
                } catch (NumberFormatException e) {
                    currentPosLayer.setError("Coordinate should be integers with Compass points separated with space (example: 1 3 N)");
                    return;
                }
            }
        }


        String navigationText = navigationLayer.getText().toString();
        if (navigationText.isEmpty()) {
            navigationLayer.setError("please input the navigation code");
            return;
        } else {
            if (!robortHelper.verifyRoute(navigationText)) {
                navigationLayer.setError("Route code undefined");
                return;
            }
            inputArgs.navigationRoute = navigationText;
        }


        if (validateCurrentWithMax(inputArgs.tX, inputArgs.ty, inputArgs.posArg)) {//Checks for position out of bounds 
            findResult(inputArgs);
        }

    }

    private boolean validateCurrentWithMax(int tX, int ty, CurrentPosArg posArg) {
        if (posArg.cX < 0 || posArg.cy < 0) {
            Toast.makeText(this, "Error : Out of bounds : Current position should be zero or greater !", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (posArg.cX > tX || posArg.cy > ty) {
            Toast.makeText(this, "Error: Out of bounds : Current position greater than top coordinates !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void initFields() {
        topCordLayer = findViewById(R.id.ID_top_layer);
        currentPosLayer = findViewById(R.id.ID_middle_layer);
        navigationLayer = findViewById(R.id.ID_last_layer);
    }

    private void findResult(InputArgs inputArgs) {
        CurrentPosArg currentPosForNavigation = null;
        for (char c : inputArgs.navigationRoute.toCharArray()) {
            currentPosForNavigation = robortHelper.getCurrentPosForNavigation(inputArgs.posArg, robortHelper.parseRoute(c));
            if (!validateCurrentWithMax(inputArgs.tX, inputArgs.ty, currentPosForNavigation)) {//Checks for position out of bounds
                return;
            }
        }
        if (currentPosForNavigation != null) {
            showResult(currentPosForNavigation);
        }
    }

    private void showResult(CurrentPosArg currentPosForNavigation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Robot is in the position");
        builder.setMessage("" + currentPosForNavigation.cX + ", " + currentPosForNavigation.cy + ", " + currentPosForNavigation.cPosition);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
        });
        builder.setNegativeButton("", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}