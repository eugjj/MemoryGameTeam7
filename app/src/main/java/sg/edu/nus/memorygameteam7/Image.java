package sg.edu.nus.memorygameteam7;

import android.graphics.Bitmap;

public class Image {
    private String fname;
    private int id;
    private Bitmap bm;
    
    public Image(String fname, int id, Bitmap bm) {
        this.fname = fname;
        this.id = id;
        this.bm = bm;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }
}