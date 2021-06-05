package com.example.virtual_cabinet_16011089;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterWardropList extends RecyclerView.Adapter<AdapterWardropList.WListViewHolder> {
    private Context mCtx;
    private List<Wardrop> wardropsList;
    private DatabaseHelper db;
    public AdapterWardropList(Context mCtx, List<Wardrop> wardropsList, DatabaseHelper db) {
        this.mCtx = mCtx;
        this.wardropsList = wardropsList;
        this.db = db;
    }

    @NonNull
    @Override
    public WListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.wardrop_list, null);
        return new WListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WListViewHolder holder, int position) {
        Wardrop thisWardrop = wardropsList.get(position);

        holder.w_name.setText(thisWardrop.getName());
        holder.w_info.setText(thisWardrop.getClothesCount().toString()+" Kıyafet");

        holder.selfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, WardropScreen.class);
                intent.putExtra("wardropNumber", thisWardrop.getID());
                mCtx.startActivity(intent);
            }
        });
        holder.xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new AlertDialog.Builder(mCtx)
                            .setTitle("Olusturduğunuz Dolap ve içindekiler Silinecek")
                            .setMessage("Silmek istediğinize Emin Misiniz ? ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    db.deleteWardrop(thisWardrop.getID());
                                    wardropsList.remove(thisWardrop);
                                    notifyItemRemoved(position);
                                }
                    }).setNegativeButton("No", null).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return wardropsList.size();
    }

    public class WListViewHolder extends RecyclerView.ViewHolder {
        TextView w_name,w_info;
        View selfView;
        ImageButton xButton;
        public WListViewHolder(@NonNull View itemView) {
            super(itemView);
            w_name = itemView.findViewById(R.id.w_name);
            w_info = itemView.findViewById(R.id.w_info);
            selfView = itemView.findViewById(R.id.selfView);
            xButton = itemView.findViewById(R.id.imag);
        }

    }
}
