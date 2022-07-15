package sg.edu.nus.memorygameteam7;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    MediaPlayer media;
    public MusicService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate()
    {
        media = MediaPlayer.create(this, R.raw.game_music);
        media.start();
        media.setLooping(true);

    }
    public void onForeGroundService(Intent intent, int startId)
    {
    }
    public void onDestroy()
    {
        media.stop();
    }
}
