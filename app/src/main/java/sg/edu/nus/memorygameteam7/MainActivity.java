package sg.edu.nus.memorygameteam7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText PlayerName;
    private Button SubmitButton;
    String player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlayerName = findViewById(R.id.playerName);
        SubmitButton = findViewById(R.id.submitBtn);

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(PlayerName)) {

                    hideKeyboard();

                    SharedPreferences pref = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    // add in shared preferences for score

                    player = PlayerName.getText().toString();
                    editor.putString("playerName", player);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() > 0){
            return false;
        }
        return true;
    }
    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}