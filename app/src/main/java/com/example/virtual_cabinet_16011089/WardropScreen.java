package com.example.virtual_cabinet_16011089;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import java.util.List;

public class WardropScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private AdapterClothes cAdapter;
    public ImageView targetIw;

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
                Uri imageUri = data.getData();
                targetIw.setImageURI(imageUri);
            }
            else
                if(requestCode == REQUEST_IMAGE_CAPTURE){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    targetIw.setImageBitmap(imageBitmap);
                }


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