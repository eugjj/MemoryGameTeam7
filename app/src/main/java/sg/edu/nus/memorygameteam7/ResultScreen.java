package sg.edu.nus.memorygameteam7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultScreen extends AppCompatActivity implements View.OnClickListener {

    protected int time;
    protected int bestTime;
    protected String playerName, bestPlayer;
    protected TextView thisTimed, topScorer, topScorerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        thisTimed = findViewById(R.id.thisTimed);
        topScorer = findViewById(R.id.topScorer);
        topScorerTime = (TextView) findViewById(R.id.topScorerTime);

        Intent intent = getIntent();
        time = intent.getIntExtra("time",100);
        SharedPreferences pref = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        playerName = pref.getString("playerName", null);
        thisTimed.setText("You've completed the game in " + time + " seconds");

        bestTime();


        Button mainMenu = findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.mainMenu){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void bestTime(){
        SharedPreferences pref = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        bestTime = pref.getInt("TOP_SCORE",100);
        bestPlayer = pref.getString("TOP_SCORER", null);
        SharedPreferences.Editor editor = pref.edit();

        if(time < bestTime){
            topScorer.setText("Top Scorer: " + playerName);
            topScorerTime.setText("Top Score Time: " + time + "seconds");

            editor.putInt("TOP_SCORE", time);
            editor.commit();
            editor.putString("TOP_SCORER", playerName);
            editor.commit();
        }
        else{
            topScorer.setText("Top Scorer: " + bestPlayer);
            topScorerTime.setText("Top Score Time: " + bestTime + " seconds");
        }
    }
}