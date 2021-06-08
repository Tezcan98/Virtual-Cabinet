package com.example.virtual_cabinet_16011089;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterCombine extends RecyclerView.Adapter<AdapterCombine.CombineViewHolder> {
    private Context mCtx;
    private Clothes[] clothesList;
    private DatabaseHelper db;
    private String[] images;
    private ViewGroup parent;

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
        this.parent = parent;
        return new CombineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CombineViewHolder holder, int position) {
        Clothes clothes = clothesList[position];
        if (clothes == null){
            holder.c_name.setVisibility(View.INVISIBLE);
            holder.thisImageV.setImageResource(Integer.parseInt(images[position]));
            holder.thisImageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomDialog(view, holder, position);
                }
            });
        }
        else{
            holder.c_name.setText(clothes.getName());
            holder.c_name.setVisibility(View.VISIBLE);
            holder.thisImageV.setImageURI(clothes.getUriFromStringPath(mCtx));
        }
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
                holder.thisImageV.setOnClickListener(null);
                holder.thisImageV.setImageURI(clothes.getUriFromStringPath(mCtx));
                alertDialog.hide();
                return false;
            }
            });
    }
}
