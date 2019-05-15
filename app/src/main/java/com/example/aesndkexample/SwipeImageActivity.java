package com.example.aesndkexample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;


public class SwipeImageActivity extends Activity {


    private boolean hide;
    String path="";
    ArrayList<Bitmap> photo = new ArrayList<>();
    ArrayList<String> listaarchivos;
    ImageView decrypted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getimagen();
        setContentView(R.layout.show_images);
        //imagesmap();
        File imgFile = new File(Environment.getExternalStorageDirectory() + "/parcial3/"+listaarchivos.get(0));

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        decrypted= (ImageView) findViewById(R.id.gallery_image);
        //myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        decrypted.setImageBitmap(myBitmap);


    }
    public void getimagen(){
        File file = new File(Environment.getExternalStorageDirectory() + "/parcial3/");
        String[] paths = file.list();
        listaarchivos = new ArrayList<String>();
        for (int i=0; i<paths.length;i++){
            // File imgFile = new File(Environment.getExternalStorageDirectory() + "/parcial3/"+paths[i]);
            //textos.add(paths[i]);
            listaarchivos.add(paths[i]);
        }
        Toast.makeText(this, listaarchivos.get(0) , Toast.LENGTH_LONG).show();
    }
    public void imagesmap(){



    }







}
