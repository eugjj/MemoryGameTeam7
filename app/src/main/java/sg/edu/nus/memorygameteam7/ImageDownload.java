package sg.edu.nus.memorygameteam7;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageDownload {


    protected String getUrlString(String inputUrl) {
        try {
            URL url = new URL(inputUrl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;}

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String urlString = "";
            String current;
            while ((current = in.readLine()) != null) {
                urlString += current;
            }
            return urlString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected List<String> imgUrlList(String urlString){
        List<String> tempSrcList = new ArrayList<String>();
        Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");
        Matcher matcher = p.matcher(urlString);
        boolean hasPic = matcher.find();
        if(hasPic)
        {
            while (hasPic)
            {
                String group = matcher.group(2);
                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher matcher2 = srcText.matcher(group);
                if (matcher2.find()) {
                    tempSrcList.add(matcher2.group(3));
                }
                hasPic = matcher.find();
            }
        }

        List<String> srcList = new ArrayList<String>();
        for(int i=0;i<tempSrcList.size();i++){
            String tempSRC=tempSrcList.get(i);
            if(!srcList.contains(tempSRC) && tempSRC.contains("https://")){
                srcList.add(tempSrcList.get(i));
            }
        }

        Iterator<String> iterator=srcList.iterator();
        while (iterator.hasNext()){
            String url=iterator.next();
            if(!url.contains("jpg")){
                iterator.remove();
            }
        }

        return srcList;
    }

    protected Bitmap ImageDownloader (String imgURL){
        try{
            URL url=new URL(imgURL);
            URLConnection conn=url.openConnection();

            InputStream in=conn.getInputStream();
            Bitmap bitmap= BitmapFactory.decodeStream(in);
            bitmap = scaleImage(bitmap);
            in.close();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap scaleImage(Bitmap bm){

        int screenWidth = 300;
        int screenHeight = 325;

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) screenWidth) / width;
        float scaleHeight=((float) screenHeight) / height;

        float scale=Math.max(scaleWidth,scaleHeight);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newBm;
    }
}
