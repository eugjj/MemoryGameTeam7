package sg.edu.nus.memorygameteam7;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageViewAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> imgInfoList;
    private int maxPosition;

    public ImageViewAdapter(Context context, List<Bitmap> imgInfoList){
        this.context=context;
        this.imgInfoList=imgInfoList;
    }

    @Override
    public int getCount(){
        maxPosition=imgInfoList.size();
        return maxPosition;
    }
    @Override
    public Object getItem(int position) {
        return imgInfoList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 325));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(10, 10, 10, 10);
            imageView.setCropToPadding(true);
            imageView.setBackgroundResource(R.drawable.bg_card);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(imgInfoList.get(position));
        return imageView;
    }

}
