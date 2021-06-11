package com.example.virtual_cabinet_16011089;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterCombine extends RecyclerView.Adapter<AdapterCombine.CombineViewHolder> {
    private Context mCtx;
    private Clothes[] clothesList;
    private DatabaseHelper db;
    private String[] images;
    private int REQUEST_IMAGE_CAPTURE = 1;

    public AdapterCombine(Context mCtx, Clothes[] clothesList, DatabaseHelper db) {
        this.mCtx = mCtx;
        this.clothesList = clothesList;
        images = new String[]{String.valueOf(R.drawable.cap), String.valueOf(R.drawable.face), String.valueOf(R.drawable.tshirt), String.valueOf(R.drawable.throus), String.valueOf(R.drawable.shoes)};
        this.db = db;
    }

    public void setClothesList(Clothes[] clothesList) {
        this.clothesList = clothesList;
    }

    @NonNull
    @Override
    public CombineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cabin_room, null);
        return new CombineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CombineViewHolder holder, int position) {
        Clothes clothes = clothesList[0];
        if(position > 1) // 0 sapka 1 yuz 2 kazak
            clothes = clothesList[position-1];

        if(position == 1)
        {
            holder.c_name.setVisibility(View.INVISIBLE);
            holder.thisImageV.setImageResource(Integer.parseInt(images[1]));
            Uri faceUri =  ((CabinRoom)mCtx).faceUri;
            holder.thisImageV.setImageURI(faceUri);
        }
        else
            if (clothes == null){
                holder.c_name.setVisibility(View.INVISIBLE);
                holder.thisImageV.setImageResource(Integer.parseInt(images[position]));
            }
            else{
                    holder.thisImageV.setImageURI(clothes.getUriFromStringPath(mCtx));
                    holder.c_name.setText(clothes.getName());
                    holder.c_name.setVisibility(View.VISIBLE);
            }

        holder.thisImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 1){
                    ((CabinRoom) mCtx).faceShow = holder.thisImageV;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ((CabinRoom) mCtx).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                else
                    showCustomDialog(view, holder, (position == 0 ? 0 : position-1));  // 0 , 2, 3 ve 4. poslar dolmalı
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class CombineViewHolder extends RecyclerView.ViewHolder {
        TextView c_name;
        ImageView thisImageV;
        public CombineViewHolder(@NonNull View itemView) {
            super(itemView);
            c_name = itemView.findViewById(R.id.info);
            thisImageV = itemView.findViewById(R.id.c_imag);
        }
    }
    private void showCustomDialog(View view, CombineViewHolder holder, Integer position) {
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        ExpandableListView expandableListView;
        ExpandableListAdapter expandableListAdapter;
        List<String> expandableListTitle;
        HashMap<String, List<Clothes>> expandableListDetail = new HashMap<>();
        View dialogView = LayoutInflater.from(mCtx).inflate(R.layout.expand_list, viewGroup, false);
        expandableListView = (ExpandableListView)dialogView.findViewById(R.id.expandableListView);

        List<Wardrop> wardropList =  db.returnWardrops();
        for( Wardrop wp: wardropList){
            List<Clothes> clothesList = db.returnClothes(wp.getID());
            expandableListDetail.put(wp.getName(), clothesList);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new AdapderExpandable(mCtx, expandableListTitle, expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                holder.c_name.setVisibility(View.VISIBLE);
                Clothes clothes = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                clothesList[position] = clothes;
                holder.c_name.setText(clothes.getName());
                holder.thisImageV.setImageURI(clothes.getUriFromStringPath(mCtx));
                alertDialog.hide();
                return false;
            }
            });
    }
}
