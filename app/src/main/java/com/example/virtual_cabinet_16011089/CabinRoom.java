package com.example.virtual_cabinet_16011089;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class CabinRoom extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private Combine thisCombine;
    private Button addCombine, change;
    Clothes[] ClothesList;
    Boolean saved = false;
    private Dialog dialog;
    int clickedCombine;
    AdapterCombine cAdapter;
    AdapterChangeCombine wAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabin_room);
        db = new DatabaseHelper(this);

        ClothesList = new Clothes[5];

        // todo 4.  madde e şıkkı kombinle eşle ~ 40dk
        // ------------- 2:10 saat
        // todo: resim, kamera, alma görüntüleme ~ 2 saat
        // todo: genel hatalar test et ~1 saat
        // -------------- 3 saat
        // todo: video & gönderme ~ 1 saat


        addCombine = findViewById(R.id.addCombine);
        change = findViewById(R.id.change);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(CabinRoom.this));
        cAdapter = new AdapterCombine(this, ClothesList, db);
        recyclerView.setAdapter(cAdapter);

        addCombine.setOnClickListener(new View.OnClickListener() {
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
                            thisCombine = db.createCombine(ClothesList, combineName);
                            addCombine.setText("Kombini Güncelle");

                            saved = true;
                            Toast.makeText(getApplicationContext(),"Kombin Kaydedildi", Toast.LENGTH_LONG).show();
                            shareCombine();

                        }
                    });

                    dialog.show();

                }
                else{
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
                        thisCombine    = wAdapter.clickedCombine;
                        if (thisCombine != null){
                            clickedCombine = thisCombine.getID();
                            ClothesList = db.returnPairClothes(clickedCombine);
                            cAdapter.setClothesList(ClothesList);
                            cAdapter.notifyItemRangeChanged(0,5);

                            addCombine.setText("Kombini Güncelle");
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
                // Do nothing but close the dialog

                // TODO:  Kıyafet ismi ve resimleri paylaş
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("text/*");
//        Uri uri = getUriForFile(mCtx, "com.enestezcan.fileprovider", storageDir);
//        share.putExtra(Intent.EXTRA_STREAM,  uri);
                CabinRoom.this.startActivity(Intent.createChooser(share, "share file with"));

                dialog.dismiss();
            }
        });



        AlertDialog alert = builder.create();
        alert.show();


    }


}