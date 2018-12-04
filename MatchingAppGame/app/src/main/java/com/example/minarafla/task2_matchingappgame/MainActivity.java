package com.example.minarafla.task2_matchingappgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    static DBAdapter myDb;

    ImageView im1;
    ImageView im2;
    ImageView im3;
    ImageView im4;
    ImageView im5;
    ImageView im6;
    ImageView im7;
    ImageView im8;


    int[] AnimalsImgsArray;
    List<Integer> AnimalsPics;

    //some flags
    boolean firstClickFlag=false, secondClickFlag=false;
    int firstImageClickedID;
    int secondImageClickedID;

    //in order to know when the same images are opened
    String picture1Resource;
    String picture2Resource;


    MediaPlayer ring;
    boolean ringing;
    //for the logcat only
    int counter=0;

    final int NumberOfCards = 8;
    int numberOfMatchedCards=0;
    String score="THE BEST MINA WAGDI";
    String date ="today";

    Date date1;
    Date date2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        openDB();

        date1 = Calendar.getInstance().getTime();

        ReferenceImageViews();
        setResourcesForImageViews();

        //getArrayImages() is the same as InitializeAnimalsPicsArrayList()
        //The only difference is that the images in the first one are stored in an array
        //While in the second, the images are stored in an ArrayList so we can shuffle them.
        AnimalsImgsArray = getArrayImages();
        AnimalsPics = new ArrayList<Integer>();
        InitializeAnimalsPicsArrayList();

        Collections.shuffle(AnimalsPics);

        im1.setOnClickListener(this);
        im2.setOnClickListener(this);
        im3.setOnClickListener(this);
        im4.setOnClickListener(this);
        im5.setOnClickListener(this);
        im6.setOnClickListener(this);
        im7.setOnClickListener(this);
        im8.setOnClickListener(this);
    }

    void ReferenceImageViews() {
        im1 = findViewById(R.id.image1);
        im2 = findViewById(R.id.image2);
        im3 = findViewById(R.id.image3);
        im4 = findViewById(R.id.image4);
        im5 = findViewById(R.id.image5);
        im6 = findViewById(R.id.image6);
        im7 = findViewById(R.id.image7);
        im8 = findViewById(R.id.image8);
    }

    public void setResourcesForImageViews(){
        im1.setImageResource(R.drawable.card_back);
        im2.setImageResource(R.drawable.card_back);
        im3.setImageResource(R.drawable.card_back);
        im4.setImageResource(R.drawable.card_back);
        im5.setImageResource(R.drawable.card_back);
        im6.setImageResource(R.drawable.card_back);
        im7.setImageResource(R.drawable.card_back);
        im8.setImageResource(R.drawable.card_back);
    }

    int[] getArrayImages() {
        int imageRes[] = {R.drawable.chicken1, R.drawable.chicken1, R.drawable.dog1, R.drawable.dog1,
                R.drawable.monkey1, R.drawable.monkey1, R.drawable.zibra1, R.drawable.zibra1};
        return imageRes;
    }

    //This function convert the images array to an arraylist in order to shuffle them
    void InitializeAnimalsPicsArrayList() {
        for (int i = 0; i < AnimalsImgsArray.length; i++) {
            AnimalsPics.add(AnimalsImgsArray[i]);
        }
    }

    @Override
    public void onClick(View v) {
        if(ringing){
            //ring.reset();
            ring.release();
            ringing=false;
        }
        int imageIndex;
        //check if the same image is clicked twice, so we flip it.
        if(checkIfSameImageClicked(v)){
            return;
        }
        //If only one card is flipped--> then flip another one
        else if((firstClickFlag!=secondClickFlag)||(firstClickFlag==false && secondClickFlag==false)){
            imageIndex=ShowClickedImage(v);
            if(firstClickFlag==true&&secondClickFlag==false) {
                secondImageClickedID=v.getId();
                secondClickFlag = true;
                picture2Resource=AnimalsPics.get(imageIndex).toString();
                if(imagesMatched()){
                    //this functiion will hide the 2 similar images.
                    //note in its implementation they are all if conditions, not if else conditions.
                    HideMatchedImages(picture1Resource);
                    if(numberOfMatchedCards==8){
                        date2= Calendar.getInstance().getTime();
                        long d = date2.getTime()-date1.getTime();
                        score=""+(d*0.001)+" seconds";
                        date=date1.toString();
                        ring.release();
                        long res=MainMenu.insertData(date,""+score);
                        if(res==0){
                            Log.i("DBmm","Data not inserted successfully");
                        }
                        else{
                            Log.i("DBmm","Data inserted successfully");
                        }
                        Intent intent = new Intent(getApplicationContext(),ShowGameScore.class);
                        intent.putExtra("score",score);
                        startActivity(intent);
                    }
                    Log.i("WINNER","GREAT");
                }
            }
            //In the beginning only that the both flags should be equal false
            else if(firstClickFlag==false && secondClickFlag==false){
                firstImageClickedID=v.getId();
                firstClickFlag=true;
                picture1Resource=AnimalsPics.get(imageIndex).toString();
            }
        }
        //if two cards are flipped
        else if(firstClickFlag==true && secondClickFlag==true){
            imageIndex=ShowClickedImage(v);
            HideImage(firstImageClickedID);
            HideImage(secondImageClickedID);
            secondClickFlag = false;
            firstClickFlag=true;
            firstImageClickedID=v.getId();
            picture1Resource=AnimalsPics.get(imageIndex).toString();
        }
    }

    public boolean checkIfSameImageClicked(View v){
        if(firstClickFlag==true&&secondClickFlag==false){
            if(v.getId()==firstImageClickedID){
                HideImage(firstImageClickedID);
                firstClickFlag=false;
                Log.i("MINASTAG",counter+" first click flag is "+firstClickFlag+" and second click flag is "+secondClickFlag);
                counter++;
                return true;
            }
        }
        else if(firstClickFlag==true&&secondClickFlag==true){
            if(v.getId()==secondImageClickedID){
                HideImage(secondImageClickedID);
                secondClickFlag=false;
                Log.i("MINASTAG",counter+" first click flag is "+firstClickFlag+" and second click flag is "+secondClickFlag);
                counter++;
                return true;
            }
            else if(v.getId()==firstImageClickedID) {
                HideImage(firstImageClickedID);
                firstClickFlag = true;
                secondClickFlag = false;
                firstImageClickedID = secondImageClickedID;
                picture1Resource=picture2Resource;
                Log.i("MINASTAG", counter + " first click flag is " + firstClickFlag + " and second click flag is " + secondClickFlag);
                counter++;
                return true;
            }
        }
        return false;
    }

    public int ShowClickedImage(View v){
        int image_num=0;
        String imageName;
        switch (v.getId()) {
            case R.id.image1:
                im1.setImageResource(AnimalsPics.get(0));
                image_num=0;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME",""+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
            case R.id.image2:
                im2.setImageResource(AnimalsPics.get(1));
                image_num=1;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME","Hello "+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
            case R.id.image3:
                im3.setImageResource(AnimalsPics.get(2));
                image_num=2;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME",""+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
            case R.id.image4:
                im4.setImageResource(AnimalsPics.get(3));
                image_num=3;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME",""+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
            case R.id.image5:
                im5.setImageResource(AnimalsPics.get(4));
                image_num=4;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME",""+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
            case R.id.image6:
                im6.setImageResource(AnimalsPics.get(5));
                image_num=5;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME",""+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
            case R.id.image7:
                im7.setImageResource(AnimalsPics.get(6));
                image_num=6;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME",""+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
            case R.id.image8:
                im8.setImageResource(AnimalsPics.get(7));
                image_num=7;
                runAnimalSound(getResources().getResourceName(AnimalsPics.get(image_num)));
                Log.i("IMAGENAME",""+getResources().getResourceName(AnimalsPics.get(image_num)));
                break;
        }
        return image_num;
    }

    public void HideMatchedImages(String res){
        ring.release();
        ringing=false;
        if(AnimalsPics.get(0).toString().equalsIgnoreCase(res)){
            im1.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }
        if(AnimalsPics.get(1).toString().equalsIgnoreCase(res)){
            im2.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }
        if(AnimalsPics.get(2).toString().equalsIgnoreCase(res)){
            im3.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }
        if(AnimalsPics.get(3).toString().equalsIgnoreCase(res)){
            im4.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }
        if(AnimalsPics.get(4).toString().equalsIgnoreCase(res)){
            im5.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }
        if(AnimalsPics.get(5).toString().equalsIgnoreCase(res)){
            im6.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }
        if(AnimalsPics.get(6).toString().equalsIgnoreCase(res)){
            im7.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }
        if(AnimalsPics.get(7).toString().equalsIgnoreCase(res)){
            im8.setVisibility(View.INVISIBLE);
            numberOfMatchedCards++;
        }


    }

    public void HideImage(int imageClicked){
            switch (imageClicked) {
                case R.id.image1:
                    im1.setImageResource(R.drawable.card_back);
                    break;
                case R.id.image2:
                    im2.setImageResource(R.drawable.card_back);
                    break;
                case R.id.image3:
                    im3.setImageResource(R.drawable.card_back);
                    break;
                case R.id.image4:
                    im4.setImageResource(R.drawable.card_back);
                    break;
                case R.id.image5:
                    im5.setImageResource(R.drawable.card_back);
                    break;
                case R.id.image6:
                    im6.setImageResource(R.drawable.card_back);
                    break;
                case R.id.image7:
                    im7.setImageResource(R.drawable.card_back);
                    break;
                case R.id.image8:
                    im8.setImageResource(R.drawable.card_back);
                    break;
            }
        }


    public boolean imagesMatched(){
        Log.i("WINNER",""+picture1Resource.toLowerCase().toString());
        Log.i("WINNER",""+picture2Resource.toLowerCase().toString());
        if(picture1Resource.toString().equalsIgnoreCase(picture2Resource.toString()))
            return true;
        return false;
    }

    public void runAnimalSound(String imageName){
        ringing=true;
        if(imageName.equalsIgnoreCase("com.example.minarafla.task2_matchingappgame:drawable/chicken1")){
            ring= MediaPlayer.create(MainActivity.this,R.raw.chicken_noise);
            ring.start();
            Log.i("ANIMALSOUND","ENTERED in chicken");

        }
        else if(imageName.equalsIgnoreCase("com.example.minarafla.task2_matchingappgame:drawable/dog1")){
            ring= MediaPlayer.create(MainActivity.this,R.raw.dog_sound);
            ring.start();
            Log.i("ANIMALSOUND","ENTERED in dog");

        }
        else if(imageName.equalsIgnoreCase("com.example.minarafla.task2_matchingappgame:drawable/monkey1")){
            ring= MediaPlayer.create(MainActivity.this,R.raw.monkey_sound);
            ring.start();
            Log.i("ANIMALSOUND","ENTERED in monkey");

        }
        else if(imageName.equalsIgnoreCase("com.example.minarafla.task2_matchingappgame:drawable/zibra1")){
            ring= MediaPlayer.create(MainActivity.this,R.raw.donkey_sound);
            ring.start();
            Log.i("ANIMALSOUND","ENTERED in donkey");

        }
    }



    /*What are the exceptions that can be encountered ?
    * First, when a person clicks on the same image twice, it should be flipped
    *
    *
    *
    *
    * */

//
//    private void openDB() {
//        myDb = new DBAdapter(this);
//        myDb.open();
//    }
//    private void closeDB() {
//        myDb.close();
//    }
//
//    public long insertData(String date, String score) {
//
//        long newId = myDb.insertRow(date,score);
//        Log.i("DB","newId "+newId);
//        if(newId==-1){
//            return  0;
//        }
//        else{
//            return 1;
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ring.stop();
        }catch(Exception e){
            Log.i("EXCEPTION","onDestroyException");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            ring.stop();
        }catch(Exception e){
            Log.i("EXCEPTION","onPauseException");
        }
    }
    @Override
    protected void onResume() {
        super.onPause();
        try {
            ring.start();
        }catch(Exception e){
            Log.i("EXCEPTION","onResumeException");
        }
    }
}




