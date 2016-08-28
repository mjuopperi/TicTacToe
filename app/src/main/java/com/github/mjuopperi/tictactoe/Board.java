package com.github.mjuopperi.tictactoe;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final String CROSS = "X";
    public static final String NOUGHT = "O";

    private View parent;
    private List<Button> board;
    private int width;
    private int movesLeft;
    private AI ai;
    private boolean gameActive;

    public Board(int width, View parent) {
        this.parent = parent;
        initBoard(width, parent);
        this.ai = new DumbAI(board);
        this.width = width;
        this.movesLeft = width * 3;
        gameActive = true;
    }

    private void initBoard(int width, View parent) {
        board = new ArrayList<Button>();
        TableLayout table = (TableLayout) parent.findViewById(R.id.board);
        table.removeAllViews();
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
                if (movesLeft == 0) gameActive = false;
                else if (!gameActive) Toast.makeText(parent.getContext(), "Game is over. Restart to play again.", Toast.LENGTH_LONG).show();
                else {
                    if (check((Button) v)) {
                        checkForWinner();
                        movesLeft--;
                        if (movesLeft > 0) {
                            ai.play();
                            checkForWinner();
                            movesLeft--;
                        }
                    }
                }
            }
        };
    }


    private boolean check(Button button) {
        if (button.getText().toString().isEmpty()) {
            button.setText(CROSS);
            return true;
        } else return false;
    }

    private void checkForWinner() {
        List<ValAndIndex> winningRow = checkAll();
        if (winningRow != null) {
            Toast.makeText(parent.getContext(), "Congratulations " + winningRow.get(0).val + "! You have won.", Toast.LENGTH_LONG).show();
            for (ValAndIndex cell : winningRow) {
                board.get(cell.index).setTextColor(ContextCompat.getColor(parent.getContext(), R.color.colorAccent));
            }
            gameActive = false;
        }
    }

    private class ValAndIndex {
        private String val;
        private int index;

        public ValAndIndex(String val, int index) {
            this.val = val;
            this.index = index;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof ValAndIndex && this.val.equals(((ValAndIndex) other).val);
        }
    }

    private List<ValAndIndex> checkAll() {
        List<ValAndIndex> winner;
        if ((winner = checkRows()) != null) return winner;
        if ((winner = checkColumns()) != null) return winner;
        return null;
    }

    private List<ValAndIndex> checkRows() {
        for (int c = 0; c < width; c++) {
            List<ValAndIndex> row = new ArrayList<>();
            for (int r = c * 3; r < c * 3 + 3; r++) {
                row.add(new ValAndIndex(board.get(r).getText().toString(), r));
            }
            if (hasWinner(row)) return row;
        }
        return null;
    }

    private List<ValAndIndex> checkColumns() {
        for (int c = 0; c < width; c++) {
            List<ValAndIndex> row = new ArrayList<>();
            for (int r = c; r < board.size(); r += 3) {
                row.add(new ValAndIndex(board.get(r).getText().toString(), r));
            }
            if (hasWinner(row)) return row;
        }
        return null;
    }

    private boolean hasWinner(List<ValAndIndex> row) {
        boolean allMatch = !row.get(0).val.isEmpty(); // Initializes to false if there is an empty value
        for (int i = 1; i < row.size(); i++) {
            if (!row.get(i).equals(row.get(i - 1))) allMatch = false;
        }
        return allMatch;
    }
}
