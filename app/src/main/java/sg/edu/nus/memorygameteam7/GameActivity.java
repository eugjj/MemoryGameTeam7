package sg.edu.nus.memorygameteam7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    GridView gridView;
    TextView tracker;
    Chronometer timer;
    int clicks = 0;
    int score = 0;
    int firstSelectedImage;
    int secondSelectedImage;
    int firstPos = -1;
    ImageView firstView;
    ArrayList<Integer> matchedImageList;
    Animation flipAnimation;
    Intent musicIntent;
    Intent imageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gridView = findViewById(R.id.gridView);
        matchedImageList = new ArrayList<>();

        musicIntent = new Intent(getApplicationContext(), MusicService.class);
        startService(musicIntent);

        imageIntent = getIntent();
        ArrayList<Bitmap> gameImages = new ArrayList<>();
        for(int i=0; i<6; i++){
            byte[] imageByteArray = imageIntent.getByteArrayExtra("img" + i);
            Bitmap bitImage = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            gameImages.add(bitImage);
            gameImages.add(bitImage);
        }
        Collections.shuffle(gameImages);

        GridViewAdapter adapter = new GridViewAdapter(this, gameImages);
        gridView.setAdapter(adapter);
        timer = (Chronometer) findViewById(R.id.timerView);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        tracker = (TextView) findViewById(R.id.matchView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imgView = view.findViewById(R.id.imageView);

                    while (score < 6 && !matchedImageList.contains(position)) {
                        if (clicks == 0) {
                            imgView.clearColorFilter();
                            firstSelectedImage = gameImages.get(position).getGenerationId();
                            firstPos = position;
                            firstView = imgView;
                            clicks++;
                            break;
                        }
                        if (clicks == 1) {
                            imgView.clearColorFilter();
                            secondSelectedImage = gameImages.get(position).getGenerationId();
                            if (checkForMatching(firstSelectedImage, secondSelectedImage)) {
                                matchedImageList.add(firstPos);
                                matchedImageList.add(position);
                                correctMatch();
                                break;
                            } else {
                                wrongSound();
                                Toast.makeText(getApplicationContext(), "Incorrect selection!", Toast.LENGTH_SHORT).show();
                                imgView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        flipAnimation(imgView);
                                        flipAnimation(firstView);
                                    }
                                }, 1000);
                                imgView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        imgView.setColorFilter(Color.argb(255, 255, 255, 255));
                                        firstView.setColorFilter(Color.argb(255, 255, 255, 255));
                                    }
                                }, 1500);
                                resetSelection();
                                break;
                            }
                        }
                    }
                }
            private void flipAnimation(ImageView view) {
                flipAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip);
                view.startAnimation(flipAnimation);
            }
        });
    }
    protected boolean checkForMatching(int firstSelectedImage, int secondSelectedImage){
        if (firstSelectedImage == secondSelectedImage){
            return true;
        }
        else return false;
    }
    protected void resetSelection(){
        firstSelectedImage = 0;
        secondSelectedImage = 0;
        clicks = 0;
    }
   protected void correctMatch(){
           score++;
           correctSound();
           if (score>=6){
               stopService(musicIntent);
               timer.stop();
               long timeUsed = SystemClock.elapsedRealtime() - timer.getBase();
               int convertTime = (int)(timeUsed/1000);
               Intent intent = new Intent(getApplicationContext(), ResultScreen.class);
               intent.putExtra("time", convertTime);
               startActivity(intent);
           }
           tracker.setText(score + " of 6 matches");
           Toast.makeText(getApplicationContext(), "Correct match!", Toast.LENGTH_SHORT).show();
           resetSelection();
       }
       protected void correctSound(){
           MediaPlayer mMediaPlayer;
           mMediaPlayer=MediaPlayer.create(this, R.raw.correct);
           mMediaPlayer.start();
       }
       protected void wrongSound(){
           MediaPlayer mMediaPlayer;
           mMediaPlayer=MediaPlayer.create(this, R.raw.wrong);
           mMediaPlayer.start();
       }
   }