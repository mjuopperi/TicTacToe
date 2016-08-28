package com.github.mjuopperi.tictactoe;

import android.widget.Button;

import java.util.List;
import java.util.Random;

public class DumbAI extends AI {

    private List<Button> board;

    public DumbAI(List<Button> board) {
        this.board = board;
    }

    public void play() {
        int start = new Random().nextInt(9);
        for (int i = start; i < board.size() + start; i++) {
            Button cell = board.get(i % board.size());
            if (cell.getText().toString().isEmpty()) {
                cell.setText(Board.NOUGHT);
                break;
            }
        }
    }
}
