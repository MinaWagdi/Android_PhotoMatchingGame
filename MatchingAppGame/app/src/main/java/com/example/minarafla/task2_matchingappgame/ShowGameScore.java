package com.example.minarafla.task2_matchingappgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowGameScore extends AppCompatActivity {

    Button ReplayButton;
    Button MainMenuButton;
    TextView textView;
    MediaPlayer ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_game_score);

        Intent i = getIntent();
        String score=i.getStringExtra("score");

//        ring= MediaPlayer.create(ShowGameScore.this,R.raw.gedo_ali);
//        ring.start();


        textView = findViewById(R.id.textView4);
        textView.setText(score);
        ReplayButton = findViewById(R.id.Replay);
        MainMenuButton=findViewById(R.id.MainMenu);

        ReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ring.stop();
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

            }
        });

        MainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ring.stop();
                Intent i= new Intent(getApplicationContext(),MainMenu.class);
                startActivity(i);

            }
        });
    }
}
