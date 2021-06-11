package com.example.virtual_cabinet_16011089;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CabinRoom extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper db;
    public Combine thisCombine;
    public ImageView faceShow;
    private Button addCombineButton, change;
    Clothes[] ClothesList;
    Boolean saved = false;
    private Dialog dialog;
    int clickedCombine;
    AdapterCombine cAdapter;
    AdapterChangeCombine wAdapter;
    private Bitmap ImageBitmap = null;
    public Uri faceUri;
    private int PICK_IMAGE = 1;
    private String facePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabin_room);
        db = new DatabaseHelper(this);

        ClothesList = new Clothes[4];
        addCombineButton = findViewById(R.id.addCombine);
        change = findViewById(R.id.change);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(CabinRoom.this));
        cAdapter = new AdapterCombine(this, ClothesList, db);
        recyclerView.setAdapter(cAdapter);

        addCombineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!saved){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CabinRoom.this);
                    EditText combineText = new EditText(CabinRoom.this);
                    LinearLayout linearLayout = new LinearLayout(CabinRoom.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.gravity = Gravity.CENTER;

                    combineText.setHint("Kombin Başlığı Giriniz");
                    combineText.setLayoutParams(layoutParams);

                    linearLayout.addView(combineText);
                    linearLayout.setPadding(60, 0, 60, 0);
                    dialog.setTitle("Kombin İsmi Belirleyin.");
                    dialog.setView(linearLayout);

                    dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String combineName = combineText.getText().toString();
                            faceUri = saveImage(ImageBitmap,combineName+"img.jpg");
                            Combine combine = new Combine(combineName, facePath);
                            thisCombine = db.createCombine(ClothesList, combine);
                            addCombineButton.setText("Kombini Güncelle");
                            saved = true;
                            Toast.makeText(getApplicationContext(),"Kombin Kaydedildi", Toast.LENGTH_LONG).show();
                            shareCombine();
                        }
                    });
                    dialog.show();
                }
                else{
                    faceUri = saveImage(ImageBitmap,thisCombine.getTag()+"img.jpg");
                    thisCombine.setFacePath(facePath);
                    db.updateCombine(ClothesList, thisCombine);
                    Toast.makeText(getApplicationContext(),"Kombin Güncellendi", Toast.LENGTH_LONG).show();
                    shareCombine();
                }

            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Combine> combineList = db.returnCombines();
                Dialog dialog = showDialog(CabinRoom.this, combineList);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (thisCombine != null){
                            faceUri = thisCombine.getFaceUri(CabinRoom.this);
                            clickedCombine = thisCombine.getID();
                            ClothesList = db.returnPairClothes(clickedCombine);
                            cAdapter.setClothesList(ClothesList);
                            cAdapter.notifyItemRangeChanged(0,5);
                            addCombineButton.setText("Kombini Güncelle");
                            saved = true;
                        }
                    }
                });
            }
        });


    }


    public Dialog showDialog(Context context, List<Combine> combineList){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.combines_list);

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView_pop);
        wAdapter = new AdapterChangeCombine(CabinRoom.this, combineList, dialog);
        recyclerView.setAdapter(wAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        dialog.show();
        return dialog;
    }

    public void shareCombine(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Paylaşın");
        builder.setMessage("Kombininiz cok güzel gözüküyor ! Arkadaşlarınızla paylaşmak ister misiniz ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("image/*");
                List<Bitmap> resimler = new ArrayList<Bitmap>();
                int width = 0, height=0;
                Bitmap bitmap = null;
                for(int i=0; i<4; i++){
                    if(ClothesList[i] != null){
                        Uri uri = ClothesList[i].getUriFromStringPath(CabinRoom.this);
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(CabinRoom.this.getContentResolver(), uri);


                            resimler.add(bitmap);
                            height += bitmap.getHeight();
                            int thisWidth = bitmap.getWidth();
                            if (thisWidth > width)
                                width = thisWidth;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
                Canvas canvas = new Canvas(result);
                int prevheight = 0;
                for(Bitmap bm: resimler){
                    canvas.drawBitmap(bm, 0f, prevheight , null);
                    prevheight += bm.getHeight();
                }

                Uri sendUri = saveImage(result, "Kombinim.jpg");
                share.putExtra(Intent.EXTRA_STREAM, sendUri);
                Intent chooser = Intent.createChooser(share, "Kombinini ne ile paylaşacaksın ? ");
                CabinRoom.this.startActivity(chooser);

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public Uri saveImage(Bitmap ImageBitmap, String pictureFile){
        if (ImageBitmap != null) {
            File picName = getOutputMediaFile(pictureFile);
            try {
                FileOutputStream fos = new FileOutputStream(picName);
                ImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                facePath = picName.getAbsolutePath();
                return FileProvider.getUriForFile(CabinRoom.this, BuildConfig.APPLICATION_ID + ".provider", picName);
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
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + filename );
        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == PICK_IMAGE)

                ImageBitmap = (Bitmap) data.getExtras().get("data");
                faceShow.setImageBitmap(ImageBitmap);

    }



}