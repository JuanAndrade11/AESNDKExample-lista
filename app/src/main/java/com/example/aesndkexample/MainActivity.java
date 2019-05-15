package com.example.aesndkexample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CuadroDialogo.FinalizoCuadroDialogo{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Activity thisActivity = this;
    static File apkStorage = null;
    ListView lista;
    Context context = this;
    List<String> items;
    ArrayAdapter textos;
    byte[] key = null;
    byte[] dataToBeEnctrypted = null;
    static {
        System.loadLibrary("native-lib");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public void crearCarpeta(){
        File apkStorage = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + "parcial3");
        File apkStorage2 = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + "parcial3cp");
        if (!apkStorage.exists()) {
            apkStorage.mkdir();
            System.out.println("directorio creado");
        }

    }
    public void checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "We need access to the internal storage and other permissions, please grant all the permissions...");
            builder.setTitle("Permissions granting");
            builder.setPositiveButton("acept",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(thisActivity,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1227);
                            crearCarpeta();

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    public native static byte[] crypt(byte[] data, byte[] key, long time, int mode);
    //public native static byte[] crypt(byte[] data, long time, int mode);

    public void escribirArchivo(byte[] fileBytes){
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"
                    + "parcial3cp" + "/" + date+".cp" );
            out.write(fileBytes);
            out.close();
            textos.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void escribirArchivo2(byte[] fileBytes){
        try {

            OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"
                    + "parcial3" + "/" +"imagen.jpg" );
            out.write(fileBytes);
            out.close();
            textos.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        checkPermissions();
        getimagen();
        lista = (ListView) findViewById(R.id.listitem);
        //items = new ArrayList<String>();
        showList(lista);
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                File input = new File(Environment.getExternalStorageDirectory() + "/parcial3cp/" + items.get(position));
                try {
                    dataToBeEnctrypted = readFileToByteArray(input);

                    Toast.makeText(thisActivity,"Hola cracks",Toast.LENGTH_LONG);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Click", "catch " + position + " de mi ListView");
                }

                Log.i("Click", "click en el elemento " + position + " de mi ListView");
                new CuadroDialogo(context,MainActivity.this);

                return false;
            }
        });
    }
    public void getimagen(){

        File file = new File(Environment.getExternalStorageDirectory() + "/parcial3cp/");
        String[] paths = file.list();
        items = new ArrayList<String>();
        if(paths!=null) {
            for (int i = 0; i < paths.length; i++) {
                // File imgFile = new File(Environment.getExternalStorageDirectory() + "/parcial3/"+paths[i]);
                //textos.add(paths[i]);
                items.add(paths[i]);
            }
        }


    }
    public void showList(ListView list){
        getimagen();
        textos = new ArrayAdapter (getApplicationContext(), android.R.layout.simple_list_item_1, items);
        list.setBackgroundColor(Color.DKGRAY);
        list.setAdapter(textos);
    }
    public void getKey(String k){
        StringBuilder sb = new StringBuilder(k);
        while(sb.length()<16){
            sb.append('*');
        }
        key = sb.toString().getBytes(StandardCharsets.UTF_8);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_IMAGE_CAPTURE){



                    Toast.makeText(this, "soy null", Toast.LENGTH_LONG).show();

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                dataToBeEnctrypted = stream.toByteArray();
                //imageBitmap.recycle();

                new CuadroDialogo(context,MainActivity.this);



                //((ImageView)findViewById(R.id.imageview1)).setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void ResultadoCuadroDialogo(String Ffin) {
        try{

            getKey(Ffin);
            byte[] encryptedData=crypt(dataToBeEnctrypted, key, System.currentTimeMillis(),0);
            escribirArchivo(encryptedData);
            showList(lista);
            Toast.makeText(this, Ffin , Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(this, "Action not possible!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void ResultadoCuadroDialogo2(String Ffin) {
        try{

            getKey(Ffin);
            byte[] encryptedData=crypt(dataToBeEnctrypted, key, System.currentTimeMillis(),1);
            escribirArchivo2(encryptedData);
            Toast.makeText(this, Ffin+ " desencr" , Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(this, "Action not possible!", Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(MainActivity.this, SwipeImageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
