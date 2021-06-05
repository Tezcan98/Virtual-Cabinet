package com.example.virtual_cabinet_16011089;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Button createShelf, goCabine, goActivity;
    List<Wardrop> wList;
    DatabaseHelper db;
    AdapterWardropList wAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createShelf = findViewById(R.id.create_dolap);
        goCabine = findViewById(R.id.go_cabin);
        goActivity = findViewById(R.id.go_activity);

        db = new DatabaseHelper(this);
        wList = db.returnWardrops();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wAdapter = new AdapterWardropList(this, wList, db);
        recyclerView.setAdapter(wAdapter);

        goCabine.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(getApplicationContext(), CabinRoom.class);
                   startActivity(intent);
               }
        });

        goActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
                startActivity(intent);
            }
        });
        createShelf.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                final EditText shelfName = new EditText(MainActivity.this);
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.CENTER;

                shelfName.setHint("Raf İsmi Giriniz.");
                shelfName.setLayoutParams(layoutParams);

                linearLayout.addView(shelfName);
                linearLayout.setPadding(60, 0, 60, 0);
                dialog.setTitle("Yeni Bir Raf Oluştur.");
                dialog.setView(linearLayout);


                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = shelfName.getText().toString();
                        Integer wID = db.create_wardrop(name);
                        Wardrop wp = new Wardrop(wID, name);
                        int prevSize = wList.size();
                        wList.add(wp);
                        wAdapter.notifyItemRangeInserted(prevSize,wList.size()-prevSize);
                        Toast.makeText(getApplicationContext(),"Yeni raf oluşturuldu.",Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                dialog.show();
            }
        });

    }

}