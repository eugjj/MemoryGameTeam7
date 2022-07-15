package sg.edu.nus.memorygameteam7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Bitmap> imagesId;
    public GridViewAdapter(@NonNull Context context, ArrayList<Bitmap> ids) {
        this.context = context;
        this.imagesId = ids;
    }

    @Override
    public int getCount() {
        return imagesId.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(this.context).inflate(R.layout.griditem, parent, false);
        }        
        ImageView imgView = listItemView.findViewById(R.id.imageView);        
        imgView.setImageBitmap(imagesId.get(position));        
        imgView.setColorFilter(Color.argb(255, 255, 255, 255));
        return listItemView;
    }

}