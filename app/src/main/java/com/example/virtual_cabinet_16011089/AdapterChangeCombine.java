package com.example.virtual_cabinet_16011089;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AdapterChangeCombine extends RecyclerView.Adapter<AdapterChangeCombine.CombineViewHolder> {
    private Context mCtx;
    private List<Combine> combineList;
    private DatabaseHelper db;
    private String[] images;
    private ViewGroup parent;
    private Dialog dialog;
    Combine clickedCombine;

    public AdapterChangeCombine(Context mCtx, List<Combine> combineList, Dialog dialog) {
        this.mCtx = mCtx;
        this.combineList = combineList;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public CombineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_combines, null);
        this.parent = parent;

        return new CombineViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CombineViewHolder holder, int position) {
        Combine thisCombine = combineList.get(position);
        holder.tag_view.setText(thisCombine.getTag());

        holder.tag_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                clickedCombine = thisCombine;
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return combineList.size();
    }

    public class CombineViewHolder extends RecyclerView.ViewHolder {
        Button tag_view;
        public CombineViewHolder(@NonNull View itemView) {
            super(itemView);
            tag_view = itemView.findViewById(R.id.combine_tag);
        }
    }

}
