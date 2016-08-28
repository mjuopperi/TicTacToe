package com.github.mjuopperi.tictactoe;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final String CROSS = "X";
    public static final String NOUGHT = "O";


    private List<Button> board;
    private AI ai;

    public Board(int width, View parent) {
        initBoard(width, parent);
        ai = new DumbAI(board);
    }

    private void initBoard(int width, View parent) {
        board = new ArrayList<Button>();
        TableLayout table = (TableLayout) parent.findViewById(R.id.board);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,(float) 1.0);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,(float) 1.0);
        for (int r = 0; r < width; r++){
            TableRow row = new TableRow(parent.getContext());
            row.setLayoutParams(params);

            for (int c = 0;c < width; c++){
                Button cell = new Button(parent.getContext());
                cell.setTextColor(Color.GRAY);
                cell.setLayoutParams(rowParams);
                cell.setTag(3 * r + c);
                cell.setOnClickListener(buttonChecker());
                row.addView(cell);
                board.add(cell);
            }
            table.addView(row);
        }
    }

    private View.OnClickListener buttonChecker() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check((Button) v)) ai.play();
            }
        };
    }

    private boolean check(Button button) {
        if (button.getText().toString().isEmpty()) {
            button.setText(CROSS);
            return true;
        } else return false;
    }
}
