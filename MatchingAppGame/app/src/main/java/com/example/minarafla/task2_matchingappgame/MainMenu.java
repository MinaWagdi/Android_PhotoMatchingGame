package com.example.minarafla.task2_matchingappgame;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button StartButton;
    Button ScoreButton;

    static DBAdapter myDb;
    MediaPlayer ring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        myDb = new DBAdapter(this);
        myDb.open();

//        ring= MediaPlayer.create(MainMenu.this,R.raw.gedo_ali);
//        ring.start();

        //long d = insertData(" "," ");

        StartButton=findViewById(R.id.StartBtn);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ring.stop();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        ScoreButton=findViewById(R.id.ScoresBtn);
        ScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //ring.stop();
                    Cursor res = getAllData();
                    if (res.getCount() == 0) {
                        showMessage("Error", "No data found");
                        return;
                    } else {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("ID : " + res.getString(DBAdapter.COL_ROWID) + "\n");
                        Log.i("READs","ID "+res.getString(DBAdapter.COL_ROWID));
                        buffer.append("Date : " + res.getString(DBAdapter.COL_DATE) + "\n");
                        buffer.append("Score : " + res.getString(DBAdapter.COL_SCORE) + "\n");
                        while (res.moveToNext()) {
                            buffer.append("ID : " + res.getString(DBAdapter.COL_ROWID) + "\n");
                            Log.i("READs","ID "+res.getString(DBAdapter.COL_ROWID));
                            buffer.append("Date : " + res.getString(DBAdapter.COL_DATE) + "\n");
                            buffer.append("Score : " + res.getString(DBAdapter.COL_SCORE) + "\n");
                        }
                        Log.i("READs","DATA RETRIEVED is "+buffer.toString());
                        showMessage("Data", buffer.toString());
                    }
                }catch(Exception ex){
                    Log.i("DB"," "+ ex.toString());
                }
            }
        });

    }

    Cursor getAllData(){
        return myDb.getAllRows();
    }

    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


//    static public void openDB() {
//        myDb = new DBAdapter(this);
//        myDb.open();
//    }
    static public void closeDB() {
        myDb.close();
    }

    static public long insertData(String date, String score) {

        long newId = myDb.insertRow(date,score);
        Log.i("DB","newId "+newId);
        if(newId==-1){
            return  0;
        }
        else{
            return 1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ring.release();
//        ring= MediaPlayer.create(MainMenu.this,R.raw.gedo_ali);
//        ring.start();
    }
}
