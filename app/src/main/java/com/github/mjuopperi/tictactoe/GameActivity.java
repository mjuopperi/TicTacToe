package com.github.mjuopperi.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class GameActivity extends AppCompatActivity {

    private Board board;
    private SharedPreferences preferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        listenForPreferenceChanges();
        initBoard();
        toggleMusic();
    }

    private void initBoard() {
        board = new Board(3, findViewById(R.id.board));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        toggleMusic();
    }

    public void listenForPreferenceChanges() {
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("musicEnabled")) toggleMusic();
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public void resetGame(View view) {
        initBoard();
    }

    public void toggleMusic() {
        Intent intent = new Intent(GameActivity.this, BackgroundMusicService.class);
        boolean musicEnabled = preferences.getBoolean("musicEnabled", false);
        if(musicEnabled) startService(intent);
        else stopService(intent);
    }

    public void openPreferences(MenuItem item) {
        startActivity(new Intent(this, GamePreferenceActivity.class));
    }
}
