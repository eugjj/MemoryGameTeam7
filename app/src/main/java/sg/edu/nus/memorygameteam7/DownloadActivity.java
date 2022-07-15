package sg.edu.nus.memorygameteam7;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {
    private List<Bitmap> imgInfoList = new ArrayList<>();
    private List<Bitmap> imgDownloadList = new ArrayList<>();
    private List<Integer> selectedImgList = new ArrayList<>();
    private android.widget.GridView GridView;
    private android.widget.ProgressBar ProgressBar;
    private android.widget.TextView TextView;
    private TextView ErrorInfo;
    private EditText InputUrl;
    private Button Fetch;
    private Thread downloadThread;
    private final int highlight = Color.argb(155, 216, 180, 216);
    private final int maxImagesSelect = 6;
    private boolean downloadFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        InputUrl = findViewById(R.id.inputURL);
        Fetch = findViewById(R.id.fetchBtn);
        GridView = findViewById(R.id.grid_view);
        ProgressBar = findViewById(R.id.progressBar);
        TextView = findViewById(R.id.tv_downloading);
        ErrorInfo = findViewById(R.id.urlError);

        setDefaultImage();

        Fetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideKeyboard();
                ProgressBar.setVisibility(View.INVISIBLE);
                TextView.setVisibility(View.INVISIBLE);
                ErrorInfo.setVisibility(View.INVISIBLE);
                if (downloadThread != null)
                    downloadThread.interrupt();
                setDefaultImage();
                downloadThread = new MyThread();
                downloadThread.start();
            }
        });

        GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!downloadFinished) {
                    return;
                }
                if (selectedImgList.size() < maxImagesSelect) {
                    ImageView imageView = (ImageView) view;
                    if (!selectedImgList.contains(i)) {
                        addEffect(imageView);
                        selectedImgList.add(i);
                        if (selectedImgList.size() == maxImagesSelect) {
                            startGameActivity(selectedImgList);
                        }
                    } else if (selectedImgList.contains(i)) {
                        removeEffect(imageView);
                        selectedImgList.remove(Integer.valueOf(i));
                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (selectedImgList.size() == maxImagesSelect) {
            resetImages();
        }
    }

    class MyThread extends Thread {
        ImageDownload imageDownload = new ImageDownload();

        @Override
        public void run() {

            String inputUrl = InputUrl.getText().toString();
            if (!inputUrl.contains("https://"))
                inputUrl = "https://" + inputUrl;

            resetDownload();
            String urlString = imageDownload.getUrlString(inputUrl);
            if (urlString == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ErrorInfo.setText(getString(R.string.errorUrlNotification));
                        ErrorInfo.setVisibility(View.VISIBLE);
                    }
                });
                return;
            }

            List<String> srcList = imageDownload.imgUrlList(urlString);
            if (srcList.size() < 20) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ErrorInfo.setText(R.string.no_enough_image);
                        ErrorInfo.setVisibility(View.VISIBLE);
                    }
                });
                return;}

            for (int i = 0; i < 20; i++) {
                if (interrupted()) {
                    runOnUiThread(DownloadActivity.this::setDefaultImage);
                    return;}
                try{
                    Thread.sleep(300);
                    Bitmap bitmap = imageDownload.ImageDownloader(srcList.get(i));
                    if (imgDownloadList.size()>=i)
                        imgDownloadList.add(i, bitmap);
                    updateGridView(i, bitmap);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
            downloadFinished = true;
        }
    }

    private void setDefaultImage() {
        imgInfoList.clear();
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
        for (int i = 0; i < 20; i++) {
            imgInfoList.add(defaultBitmap);
        }
        GridView.setAdapter(new ImageViewAdapter(this, imgInfoList));
    }

    private void updateGridView(int i, Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    ImageView imageView = (ImageView) GridView.getChildAt(i);
                    imageView.setImageBitmap(bitmap);
                    ProgressBar.setProgress(i + 1);
                    if (i == 19) {
                        TextView.setText(R.string.download_complete);
                        TextView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProgressBar.setVisibility(View.INVISIBLE);
                                TextView.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);
                    } else {
                        TextView.setText(getString(R.string.downloading, i + 1));
                        ProgressBar.setVisibility(View.VISIBLE);
                        TextView.setVisibility(View.VISIBLE);
                    }
                }catch(NullPointerException e){
                    return;
                }
            }
        });
    }

    public void resetDownload() {
        downloadFinished = false;
        imgDownloadList.clear();
        selectedImgList.clear();
    }

    public void addEffect(ImageView imageView) {
        imageView.setColorFilter(highlight, PorterDuff.Mode.SRC_ATOP);
        imageView.setBackgroundResource(R.drawable.bg_card_selected);
    }

    public void removeEffect(ImageView imageView) {
        imageView.clearColorFilter();
        imageView.setBackgroundResource(R.drawable.bg_card);
    }

    public void resetImages() {
        GridView = findViewById(R.id.grid_view);
        GridView.setAdapter(new ImageViewAdapter(this, imgDownloadList));
        selectedImgList.clear();
    }

    public void startGameActivity(List<Integer> selectedImgList) {

        Intent intent = new Intent(this, GameActivity.class);
        for (int i = 0; i < selectedImgList.size(); i++) {

            Bitmap bm = imgDownloadList.get(selectedImgList.get(i));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra("img" + i, byteArray);
        }
        startActivity(intent);    }


    public void hideKeyboard() {
        try {
            // use application level context to avoid unnecessary leaks.
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}