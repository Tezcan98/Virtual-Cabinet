package com.example.virtual_cabinet_16011089;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WardropScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private AdapterClothes cAdapter;
    public ImageView targetIw;
    public Bitmap ImageBitmap = null;  // TODO bir ekranı editlerken diğerindeki resmi değiştirince yanlıslık olacak daha sonra bakarsın.


    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrop_screen);
        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        Integer wNo = intent.getExtras().getInt("wardropNumber");
        List<Clothes> ClothesList = db.returnClothes(wNo);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(WardropScreen.this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManagaer);

        cAdapter = new AdapterClothes(this, ClothesList, db, wNo);
        recyclerView.setAdapter(cAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            if(requestCode == PICK_IMAGE){

                try {
                    ImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    targetIw.setImageBitmap(ImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else
                if(requestCode == REQUEST_IMAGE_CAPTURE){
                    Bundle extras = data.getExtras();
                    ImageBitmap = (Bitmap) extras.get("data");
                    targetIw.setImageBitmap(ImageBitmap);

                }


        }


    public String saveImage(String pictureFile){
        if (ImageBitmap != null) {
            File picName = getOutputMediaFile(pictureFile);
            try {
                FileOutputStream fos = new FileOutputStream(picName);
                ImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                return picName.getAbsolutePath();
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private  File getOutputMediaFile(String filename){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/images");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + filename+ ".jpg" );
        return mediaFile;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this,MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}