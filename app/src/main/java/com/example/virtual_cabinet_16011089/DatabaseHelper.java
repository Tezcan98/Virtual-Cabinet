package com.example.virtual_cabinet_16011089;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sqllite_database";


    private final String CLOTHES_TABLE = "kiyafet_tablosu";
    private String c_name = "baslik";
    private String c_type = "tur";
    private String color = "renk";
    private String pattern = "desen";
    private String cost = "maliyet";
    private String c_date = "alinma_tarihi";
    private String image_path = "resim_path";

    private final String WARDROP_TABLE = "dolab_tablosu";
    private String w_name = "dolab_ismi";


    private final String ACTIVITY_TABLE = "etkinlik_tablosu";
    private String name = "isim";
    private String a_type = "turu";
    private String a_date = "tarih";
    private String location = "konum";

    private final String COMBINE_TABLE = "kombin_tablosu";
    private String c_tag = "kombin_etiketi";
    private String p_activity = "gosterdigi_etkinlik";

    private final String COMBINE_CLOTHES_TABLE = "kombin_kiyafet_tablosu";
    private String clothes_id = "kiyafet_id";
    private String clothes_number = "kiyafet_numarasi"; // kombinin kaçıncı numarası
    private String combine_id = "combine_id";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db=this.getWritableDatabase();
    }
    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLOTHES = "CREATE TABLE " + CLOTHES_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "wardrop_id INTEGER,"
                + c_name + " TEXT,"
                + c_type + " TEXT,"
                + color + " TEXT,"
                + pattern + " TEXT,"
                + cost + " FLOAT,"
                + image_path + " TEXT,"
                + c_date + " TEXT, "
                + "FOREIGN KEY(wardrop_id) REFERENCES "+WARDROP_TABLE+"(id) ON DELETE CASCADE )";

        db.execSQL(CREATE_CLOTHES);

        String CREATE_WARDROP = "CREATE TABLE " + WARDROP_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT , "
                +  w_name + " TEXT)";
        db.execSQL(CREATE_WARDROP);

        String CREATE_COMBINE = "CREATE TABLE " + COMBINE_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +  c_tag + " TEXT,"
                +  p_activity + " INTEGER, "
                + "FOREIGN KEY("+p_activity+") REFERENCES "+ACTIVITY_TABLE+"(id))";
        db.execSQL(CREATE_COMBINE);

        String CREATE_PAIR = "CREATE TABLE " + COMBINE_CLOTHES_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +  clothes_id + " INTEGER, "
                +  combine_id + " INTEGER, "
                +  clothes_number + " INTEGER, "
                + "FOREIGN KEY("+clothes_id+") REFERENCES "+CLOTHES_TABLE+"(id) ON DELETE CASCADE,"
                + "FOREIGN KEY("+combine_id+") REFERENCES "+COMBINE_TABLE+"(id) ON DELETE CASCADE)";
        db.execSQL(CREATE_PAIR);

        String CREATE_ACTIVITY = "CREATE TABLE " + ACTIVITY_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + name + " TEXT,"
                + a_type + " TEXT,"
                + a_date + " TEXT,"
                + combine_id + " INTEGER,"
                + location + " TEXT,"
                + "FOREIGN KEY("+combine_id+") REFERENCES "+COMBINE_TABLE+"(id) ON DELETE CASCADE)";




        db.execSQL(CREATE_ACTIVITY);

    }
    public Integer create_wardrop(String wName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesIntoWardropTable = new ContentValues();
        valuesIntoWardropTable.put(this.w_name, wName);
        return Math.toIntExact(db.insert(WARDROP_TABLE, null, valuesIntoWardropTable));

    }

    public void updateInsertClothes(Clothes clothes, Integer wNo, Boolean newClothes) {
        String name = clothes.getName();
        String type = clothes.getType();
        String color = clothes.getColor();
        String pattern = clothes.getPattern();
        Float cost = clothes.getCost();
        String image_path = clothes.getImagePath();
        String c_date = clothes.getDate();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesClothesTable = new ContentValues();
        valuesClothesTable.put("wardrop_id", wNo);
        valuesClothesTable.put(this.c_name, name);
        valuesClothesTable.put(this.c_type, type);
        valuesClothesTable.put(this.color, color);
        valuesClothesTable.put(this.pattern, pattern);
        valuesClothesTable.put(this.cost, cost);
        valuesClothesTable.put(this.image_path, image_path);
        valuesClothesTable.put(this.c_date, c_date);
        if (newClothes)
            db.insert(CLOTHES_TABLE, null, valuesClothesTable );
        else
            db.update(CLOTHES_TABLE, valuesClothesTable, "id = '" + clothes.getID()+"'" ,null );
    }

    public void updateInsertActivity(Activity activity, Boolean newActivities) {
            String name = activity.getName();
            String type = activity.getType();
            String Date = activity.getDate();
            String Location = activity.getTargetLocation();
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues valuesClothesTable = new ContentValues();
            valuesClothesTable.put(this.name, name);
            valuesClothesTable.put(this.a_type, type);
            valuesClothesTable.put(this.a_date, Date);
            valuesClothesTable.put(this.location, Location);
            valuesClothesTable.put(this.combine_id, Location);
            if (newActivities)
                db.insert(ACTIVITY_TABLE, null, valuesClothesTable );
            else
                db.update(ACTIVITY_TABLE, valuesClothesTable, "id = '" + activity.getId  ()+"'" ,null );
        }


    public Combine createCombine(Clothes[] clothesList, String CombineTag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues combineValues = new ContentValues();
        combineValues.put(c_tag, CombineTag);
        int combine_id = (int) db.insert(COMBINE_TABLE, null, combineValues);
        Combine combine = new Combine(combine_id, CombineTag);

        for(int i=0; i<5; i++){
            if (clothesList[i] != null){
                Clothes clothes = clothesList[i];
                ContentValues pairs = new ContentValues();
                pairs.put(this.clothes_id, clothes.getID());
                pairs.put(this.combine_id, combine_id);
                pairs.put(this.clothes_number, i);
                db.insert(COMBINE_CLOTHES_TABLE, null, pairs);
                combine.addClothes(i, Math.toIntExact(clothes.getID()));
            }
        }
        return combine;
    }
    public Combine updateCombine(Clothes[] clothesList, Combine combine) {
            SQLiteDatabase db = this.getWritableDatabase();
            for(int i=0; i<5; i++){
                if (clothesList[i] != null){
                    Clothes clothes = clothesList[i];
                    ContentValues pairs = new ContentValues();
                    pairs.put(this.clothes_id, clothes.getID());
                    pairs.put(this.combine_id, combine_id);
                    pairs.put(this.clothes_number, i);
                    db.update(COMBINE_CLOTHES_TABLE, pairs, this.clothes_number +  "= '" + i +"' and  " + this.combine_id + "= '"+combine_id+"'", null);
                    combine.addClothes(i, Math.toIntExact(clothes.getID()));
                }
            }
            return combine;
    }

    public List<Clothes> returnClothes(Integer wNo){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM '" + CLOTHES_TABLE + "' where wardrop_id='"+wNo+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Clothes> clothesList = new ArrayList<Clothes>();
        if (cursor.moveToFirst()){
            do {
                // Passing values
                Integer id = cursor.getInt(0);
                String name = cursor.getString(2);
                String type = cursor.getString(3);
                String color = cursor.getString(4);
                String pattern = cursor.getString(5);
                float cost = cursor.getFloat(6);
                String imagePath = cursor.getString(7);
                String date = cursor.getString(8);

                Clothes dummyClothes = new Clothes(name, type, color, pattern, cost, imagePath, date);
                dummyClothes.setID(id);
                clothesList.add(dummyClothes);
            } while(cursor.moveToNext());
        }
        return clothesList;
    }
    public Clothes[] returnPairClothes(Integer combineID){
        SQLiteDatabase db = this.getReadableDatabase();
        String join_query = "SELECT * FROM "+ CLOTHES_TABLE +" clothes INNER JOIN "+COMBINE_CLOTHES_TABLE+" pair ON clothes.id = pair.kiyafet_id INNER JOIN "+COMBINE_TABLE+" combine  ON pair.combine_id = combine.id  WHERE pair.combine_id = '"+ combineID+"'";
        Cursor cursor = db.rawQuery(join_query, null);
        List<Clothes> clothesList = new ArrayList<Clothes>();
        List<Integer> ClothesNumberList = new ArrayList<Integer>();
        if (cursor.moveToFirst()){
            do {
                // Passing values
                Integer id = cursor.getInt(0);
                String name = cursor.getString(2);
                String type = cursor.getString(3);
                String color = cursor.getString(4);
                String pattern = cursor.getString(5);
                float cost = cursor.getFloat(6);
                String imagePath = cursor.getString(7);
                String date = cursor.getString(8);
                ClothesNumberList.add(cursor.getInt(12));

                Clothes dummyClothes = new Clothes(name, type, color, pattern, cost, imagePath, date);
                dummyClothes.setID(id);
                clothesList.add(dummyClothes);
            } while(cursor.moveToNext());
        }

        Clothes[] tmpArrayList = new Clothes[5];
        int i=0;
        for(Clothes clothes: clothesList){
            tmpArrayList[ClothesNumberList.get(i)] = clothesList.get(i);
            i++;
        }
        return tmpArrayList;
    }

    public List<Wardrop> returnWardrops(){

        List<Wardrop> WardropList = new ArrayList<Wardrop>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM '" + WARDROP_TABLE + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                Integer id = cursor.getInt(0);
                SQLiteStatement s = db.compileStatement( "select count(*) from "+ CLOTHES_TABLE +" where wardrop_id='" + id + "';");
                long count = s.simpleQueryForLong();
                String name = cursor.getString(1);
                Wardrop dummyWardrop = new Wardrop(id, name);
                dummyWardrop.setClothesCount((int)count);
                WardropList.add(dummyWardrop);
            } while(cursor.moveToNext());
        }
        return WardropList;
    }

       public List<Activity> returnActivities(){
        List<Activity> ActivityList = new ArrayList<Activity>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM '" + ACTIVITY_TABLE + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                Integer id = cursor.getInt(0);
                String name = cursor.getString(1);
                String a_type = cursor.getString(2);
                String a_date = cursor.getString(3);
                String location = cursor.getString(4);
                Activity Dummyactivity = new Activity(name, a_type, a_date, location);
                Dummyactivity.setId(id);
                ActivityList.add(Dummyactivity);
            } while(cursor.moveToNext());
        }
        return ActivityList;
    }


     public List<Combine> returnCombines(){

        List<Combine> CombinesList = new ArrayList<Combine>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM '" + COMBINE_TABLE + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                Integer id = cursor.getInt(0);
                String tag = cursor.getString(1);
                Combine dummyCombine = new Combine(id, tag);
                CombinesList.add(dummyCombine);

                // Do something Here with values
            } while(cursor.moveToNext());
        }
        return CombinesList;
    }

    public void deleteWardrop(Integer wNo){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(WARDROP_TABLE,  "id=" + wNo, null);
    }
    public void deleteActivity(Integer aNo){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(ACTIVITY_TABLE,  "id=" + aNo, null);
    }
    public void deleteClothes(Integer cNo){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(CLOTHES_TABLE,  "id=" + cNo, null);
    }

    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CLOTHES_TABLE, null, null);
        db.delete(WARDROP_TABLE, null, null);
        db.close();
    }
    public void dropTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+CLOTHES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+WARDROP_TABLE);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }


}