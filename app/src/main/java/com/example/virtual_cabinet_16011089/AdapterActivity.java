package com.example.virtual_cabinet_16011089;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdapterActivity extends RecyclerView.Adapter<AdapterActivity.CombineViewHolder> {
    private Context mCtx;
    private List<Activity> activityList;
    private DatabaseHelper db;
    private Activity thisActivity;
    private Boolean editMode = false;


    public AdapterActivity(Context mCtx, List<Activity> activityList, DatabaseHelper db) {
        this.mCtx = mCtx;
        this.activityList = activityList;
        this.db = db;
    }
    @NonNull
    @Override
    public CombineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.etkinlik_recyclerview, null);
        return new CombineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CombineViewHolder holder, int position) {

        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editMode){
                    editMode = true;
                    holder.inputs.setVisibility(View.VISIBLE);
                    holder.save.setVisibility(View.VISIBLE);

                    holder.editbtn.setBackgroundResource(R.drawable.delete);
                    holder.save.setText("Etkinliği Güncelle");
                    holder.a_infos.setText("İsmi\n\nTürü \n\nTarihi \n\nKonum\n\nKombin İsmi");
                    thisActivity = activityList.get(position);
                    holder.ism.setText(thisActivity.getName());
                    holder.tur.setText(thisActivity.getType());
                    holder.date.setText(thisActivity.getDate());
                    holder.lok.setText(thisActivity.getTargetLocation());
                }
                else{
                    new AlertDialog.Builder(mCtx)
                            .setTitle("Olusturduğunuz Etkinlik Silinecek")
                            .setMessage("Silmek istediğinize Emin Misiniz ? ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    db.deleteActivity(thisActivity.getId());
                                    activityList.remove(thisActivity);
                                    notifyItemRemoved(position);
                                    editMode = false;
                                    Toast.makeText(mCtx.getApplicationContext(),"Etkinlik Silindi.",Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("No", null).show();
                }
            }
        });

            if(position < activityList.size()){
                holder.editbtn.setVisibility(View.VISIBLE);
                holder.inputs.setVisibility(View.INVISIBLE);
                thisActivity = activityList.get(position);
                holder.a_infos.setText(thisActivity.toString());
            }
            else
            {
                holder.editbtn.setVisibility(View.INVISIBLE);
                holder.inputs.setVisibility(View.VISIBLE);
                holder.save.setVisibility(View.VISIBLE);
            }


        List<Combine> cList;
        cList = db.returnCombines();
        List <String> ClistNames = new ArrayList<String>();

        for (Combine combine: cList)
            ClistNames.add(combine.getTag());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCtx,
                android.R.layout.simple_spinner_dropdown_item, ClistNames);
        holder.kombinler.setAdapter(adapter);


        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = holder.ism.getText().toString();
                String tur = holder.tur.getText().toString();
                String date = holder.date.getText().toString();
                String lokasyon = holder.lok.getText().toString();
                int combineIdx  = (int) holder.kombinler.getSelectedItemId();
                Combine selectedCombine = cList.get(combineIdx);

                if (!editMode){
                    thisActivity = new Activity(name, tur, date, lokasyon, selectedCombine);
                    activityList.add(thisActivity);
                    db.updateInsertActivity(thisActivity,!editMode);  // mean if not editMode so it's new
                    Toast.makeText(mCtx.getApplicationContext(),"Etkinlik Oluşturuldu, iyi eğlenceler !.",Toast.LENGTH_SHORT).show();
                }
                else{
                    thisActivity.setName(name);
                    thisActivity.setType(tur);
                    thisActivity.setDate(date);
                    thisActivity.setTargetLocation(lokasyon);
                    thisActivity.setSelectedCombine(selectedCombine);
                    db.updateInsertActivity(thisActivity,!editMode);
                    Toast.makeText(mCtx.getApplicationContext(),"Etkinlik bilgileri güncellendi, iyi eğlenceler !.",Toast.LENGTH_SHORT).show();
                    editMode = false;
                    holder.editbtn.setBackgroundResource(R.drawable.edit);

                }
                notifyItemChanged(position);

            }
        });

        holder.date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == 1){
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mCtx,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    holder.date.setText(day + "/" + (month + 1) + "/" + year);
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
        return activityList.size()+1;
    }

    public class CombineViewHolder extends RecyclerView.ViewHolder {
        LinearLayout inputs;
        TextView a_infos;
        EditText ism,tur,date,lok;
        Button save;
        ImageButton editbtn;
        Spinner kombinler;
        public CombineViewHolder(@NonNull View itemView) {
            super(itemView);
            inputs = itemView.findViewById(R.id.a_edit_inputs);
            a_infos = itemView.findViewById(R.id.activity_infos);
            date = itemView.findViewById(R.id.date_input2);
            ism = itemView.findViewById(R.id.a_name);
            tur = itemView.findViewById(R.id.a_tur);
            lok = itemView.findViewById(R.id.konum);
            save = itemView.findViewById(R.id.etkinliki_olustur);
            editbtn = itemView.findViewById(R.id.editbtn);
            kombinler = itemView.findViewById(R.id.kombinSpinner);

        }

    }
}
