package com.example.virtual_cabinet_16011089;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class AdapterClothes extends RecyclerView.Adapter<AdapterClothes.ClothesViewHolder> {
    private Context mCtx;
    private List<Clothes> clothesList;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private DatabaseHelper db;
    private Integer wNo;
    private Boolean editMode = false;

    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PICK_IMAGE = 1;
    private Clothes thisClothes;

    public AdapterClothes(Context mCtx,   List<Clothes> clothesList, DatabaseHelper db, Integer wNo) {
        this.mCtx = mCtx;
        this.clothesList = clothesList;
        this.db = db;
        this.wNo = wNo;
    }

//    @Override
//    public int getItemViewType(int position) {
//        switch (position) {
//            case 0: return 0;
//            default: return 1;
//        }
//    }

    @NonNull
    @Override
    public ClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.add_new_clothes, null);
        return new ClothesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesViewHolder holder, int position) {

        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editMode){
                    editMode = true;
                    thisClothes = clothesList.get(position);
                    holder.editbtn.setBackgroundResource(R.drawable.delete);
                    holder.c_info.setText("Başlık\n\nTür \n\nRenk \n\nDesen \n\nFiyat\n\nAlınma Tarihi");
                    holder.baslik.setText(thisClothes.getName());
                    holder.tur.setText(thisClothes.getType());
                    holder.renk.setText(thisClothes.getColor());
                    holder.desen.setText(thisClothes.getPattern());
                    holder.fiyat.setText(String.valueOf((thisClothes.getCost())));
                    holder.date.setText(thisClothes.getDate());
                    holder.editInputs.setVisibility(View.VISIBLE);
                    holder.kaydet.setVisibility(View.VISIBLE);
                    holder.kaydet.setText("Bilgileri Güncelle");
                }
                else
                {
                    new AlertDialog.Builder(mCtx)
                            .setTitle("Olusturduğunuz Etkinlik Silinecek")
                            .setMessage("Silmek istediğinize Emin Misiniz ? ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    db.deleteClothes(thisClothes.getID());
                                    clothesList.remove(thisClothes);
                                    notifyItemRemoved(position);
                                    editMode = false;
                                    Toast.makeText(mCtx.getApplicationContext(),"Kıyafet Silindi.",Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("No", null).show();
                }
            }
        });




        if(position < clothesList.size() ){
            holder.editInputs.setVisibility(View.INVISIBLE);
            holder.kaydet.setVisibility(View.INVISIBLE);
            holder.editbtn.setVisibility(View.VISIBLE);
            thisClothes = clothesList.get(position); // pos = 0 for new clothes
            holder.c_info.setText(thisClothes.toString());

//            holder.imag.setImageResource(Integer.parseInt(thisClothes.getImagePath()));
        }else{
            holder.editInputs.setVisibility(View.VISIBLE);
            holder.kaydet.setVisibility(View.VISIBLE);
            holder.editbtn.setVisibility(View.INVISIBLE);

            holder.c_info.setText("Başlık\n\nTür \n\nRenk \n\nDesen \n\nFiyat\n\nAlınma Tarihi");
            holder.baslik.setText("");
            holder.tur.setText("");
            holder.renk.setText("");
            holder.desen.setText("");
            holder.fiyat.setText("");
            holder.date.setText("");



        }


        holder.kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baslik, tur, renk, desen, fiyat, date;
                String imagePath = "";
                baslik = holder.baslik.getText().toString();
                tur = holder.tur.getText().toString();
                renk = holder.renk.getText().toString();
                desen = holder.desen.getText().toString();
                fiyat = holder.fiyat.getText().toString();
                date = holder.date.getText().toString();
                if (fiyat.isEmpty())
                    fiyat = "0.0f";

                if(editMode){
                    thisClothes.setName(baslik);
                    thisClothes.setType(tur);
                    thisClothes.setColor(renk);
                    thisClothes.setPattern(desen);
                    thisClothes.setCost(Float.valueOf(fiyat));
                    thisClothes.setDate(date);
                    db.updateInsertClothes(thisClothes, wNo, !editMode);
                    Toast.makeText(mCtx.getApplicationContext(),"Kıyafet Bilgileri Güncellendi.",Toast.LENGTH_SHORT).show();
                    editMode = false;
                    holder.editbtn.setBackgroundResource(R.drawable.edit);
                }
                else{
                    db.updateInsertClothes(thisClothes, wNo, !editMode); // if not editmode so it's new insert
                    Clothes thisClothes = new Clothes(baslik, tur, renk, desen, Float.valueOf(fiyat), imagePath,date);
                    clothesList.add(thisClothes);
                    Toast.makeText(mCtx.getApplicationContext(),"Kıyafet Bilgileri Olusturuldu.",Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position);

//                    onBindViewHolder(holder,position);
            }
        });
        holder.imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"Camera", "Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Resim Seç");
                builder.setItems(items, new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int item) {
                        ((WardropScreen)mCtx).targetIw = holder.imag;
                        if (items[item].equals("Camera")) {

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            ((WardropScreen) mCtx).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                        } else if (items[item].equals("Gallery")) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            ((WardropScreen) mCtx).startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
        holder.date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == 1){
                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = new DatePickerDialog(mCtx,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    holder.date.setText(day + "/" + (month+1) + "/" + year);
                                }
                            }, year, month, dayOfMonth);
                    datePickerDialog.show();
                    return true;
                }

                return false;
            }
        });
    }



    @Override
    public int getItemCount() {
        return clothesList.size()+1;
    }



    public class ClothesViewHolder extends RecyclerView.ViewHolder {
        ImageView imag;
        TextView c_info;
        EditText baslik,tur, renk, desen, fiyat, date;
        Button kaydet;
        LinearLayout editInputs;
        ImageButton editbtn;
        public ClothesViewHolder(@NonNull View itemView) {
            super(itemView);
            c_info = itemView.findViewById(R.id.clothes_infos);
            imag = itemView.findViewById(R.id.imag);
            baslik = itemView.findViewById(R.id.c_name);
            tur = itemView.findViewById(R.id.tur);
            renk = itemView.findViewById(R.id.renk);
            desen = itemView.findViewById(R.id.desen);
            fiyat = itemView.findViewById(R.id.fiyat);
            kaydet = itemView.findViewById(R.id.kaydet);
            date = itemView.findViewById(R.id.date_input);
            editInputs = itemView.findViewById(R.id.edit_inputs);
            editbtn = itemView.findViewById(R.id.editbtn2);
        }
    }
}
