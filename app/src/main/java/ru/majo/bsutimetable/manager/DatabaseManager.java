package ru.majo.bsutimetable.manager;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.inject.Inject;

import ru.majo.bsutimetable.database.DatabaseHelper;
import ru.majo.bsutimetable.model.FavoriteValue;
import ru.majo.bsutimetable.model.Timetable;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.utils.TimetableUtils;
import rx.Observable;

/**
 * Created by Majo on 16.01.2016.
 */
public class DatabaseManager {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mHelper;

    @Inject
    public DatabaseManager(Context context,DatabaseHelper databaseHelper){
        mContext = context;
        mHelper = databaseHelper;
        mDatabase = mHelper.getWritableDatabase();
    }

    public Observable<Boolean> writeDatabase(){
        return Observable.create(subscriber -> {
            try {
                Log.e("db","onWriteDatabase");

                AssetManager assetManager = mContext.getResources().getAssets();
                InputStream inputStream = assetManager.open("stud.db");
                String outFileName = "data/data/ru.majo.bsutimetable/databases/stud.db";
                OutputStream fileOutputStream = new FileOutputStream(outFileName);

                byte[] buffer = new byte[8192];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                Log.e("db","complete");

                subscriber.onNext(true);
                subscriber.onCompleted();

            } catch (Exception e) {
                Log.e("db","error " + e.getMessage());

                subscriber.onError(e);
            }
        });
    }

    public void clearDataBase(){
        mHelper.clearDB(mDatabase);
    }

    public void writeDMainTable() throws IOException {
        File file = new File(mContext.getFilesDir().getAbsolutePath()+"/rasp_dmain.json");
        JsonFactory jFactory = new JsonFactory();
        JsonParser jParser = jFactory.createParser(file);
        String sql = "INSERT INTO rasp_dmain" +" VALUES (?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        Log.e("NETWORK", "write to DMAIN");

        while (jParser.nextToken()!= JsonToken.END_ARRAY){
            statement.clearBindings();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if (DatabaseHelper.GRUP_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(1, jParser.getText().replace(" ", ""));
                }
                if (DatabaseHelper.DZ_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    String s=jParser.getText();
                    String string=s.charAt(0)+""+s.charAt(1)+s.charAt(2)+s.charAt(3)+"-"+
                            s.charAt(4)+s.charAt(5)+"-"+s.charAt(6)+s.charAt(7);
                    statement.bindString(2, string);
                }
                if (DatabaseHelper.PARA_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(3, jParser.getText());
                }
                if (DatabaseHelper.DIS_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(4, jParser.getText());
                }
                if (DatabaseHelper.VID_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(5, jParser.getText());}
                if (DatabaseHelper.AUD_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(6, jParser.getText());
                }
                if (DatabaseHelper.PREP_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(7, jParser.getText());
                }
                if (DatabaseHelper.TIP_DMAIN.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(8, jParser.getText());
                }
            }
            statement.execute();
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        jParser.close();
        file.delete();
    }

    public void writeMainTable() throws IOException {
        Log.e("NETWORK", "write to MAIN");

        File file = new File(mContext.getFilesDir().getAbsolutePath()+"/rasp_main.json");
        JsonFactory jFactory = new JsonFactory();
        JsonParser jParser = jFactory.createParser(file);
        String sql = "INSERT INTO rasp_main" +" VALUES (?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        while (jParser.nextToken()!= JsonToken.END_ARRAY){
            statement.clearBindings();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if (DatabaseHelper.SUBJECT_ID_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(1, jParser.getText());
                }
                if (DatabaseHelper.TEACHER_ID_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(2, jParser.getText());
                }
                if (DatabaseHelper.AUDITORY_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(3, jParser.getText());
                }
                if (DatabaseHelper.PAIR_TYPE_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(4, jParser.getText());
                }
                if (DatabaseHelper.PAIR_NUMBER_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(5, jParser.getText());
                }
                if (DatabaseHelper.GROUP_NUMBER_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(6, jParser.getText().replaceAll("[\\s]{1,}", ""));
                }
                if (DatabaseHelper.DAY_OF_WEEK_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(7, jParser.getText());
                }
                if (DatabaseHelper.WEEK_MAIN.equals(fieldname))
                {
                    jParser.nextToken();
                    statement.bindString(8, jParser.getText());
                }
            }
            statement.execute();
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        jParser.close();
        file.delete();
    }

    public void writeTeachersTable() throws IOException {
        Log.e("NETWORK", "write to TEACHERS");

        File file = new File(mContext.getFilesDir().getAbsolutePath()+"/rasp_teachers.json");
        JsonFactory jFactory = new JsonFactory();
        JsonParser jParser = jFactory.createParser(file);
        String sql = "INSERT INTO rasp_teachers" +" VALUES (?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        while (jParser.nextToken()!= JsonToken.END_ARRAY){
            statement.clearBindings();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if (DatabaseHelper.PREP_TEACHERS.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(1, jParser.getText());
                }
                if (DatabaseHelper.FIO_TEACHERS.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(2, jParser.getText().replaceAll("[\\s]{2,}", ""));
                }
            }
            statement.execute();
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        jParser.close();
        file.delete();
    }

    public void writeSubjectsTable() throws IOException {
        Log.e("NETWORK", "write to SUBJECTS");

        File file = new File(mContext.getFilesDir().getAbsolutePath()+"/rasp_subjects.json");

        JsonFactory jFactory = new JsonFactory();
        JsonParser jParser = jFactory.createParser(file);
        String sql = "INSERT INTO rasp_subjects" +" VALUES (?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        while (jParser.nextToken()!= JsonToken.END_ARRAY){
            statement.clearBindings();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if (DatabaseHelper.DIS_SUBJECTS.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(1, jParser.getText());
                }
                if (DatabaseHelper.DISS_SUBJECTS.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(2, jParser.getText().replaceAll("[\\s]{2,}", ""));
                }
                if (DatabaseHelper.DISP_SUBJECTS.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(3, jParser.getText().replaceAll("[\\s]{2,}", ""));
                }
            }
            statement.execute();
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        jParser.close();
        file.delete();
    }

    public void writeGroupTable() throws IOException {
        Log.e("NETWORK", "write to GROUP");

        File file = new File(mContext.getFilesDir().getAbsolutePath()+"/rasp_group.json");

        JsonFactory jFactory = new JsonFactory();
        JsonParser jParser = jFactory.createParser(file);
        String sql = "INSERT INTO rasp_group" +" VALUES (?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        while (jParser.nextToken()!= JsonToken.END_ARRAY){
            statement.clearBindings();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if (DatabaseHelper.GRUP_GROUP.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(1, jParser.getText());
                }
                if (DatabaseHelper.TIP_GROUP.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(2, jParser.getText());
                }
            }
            statement.execute();
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        jParser.close();
        file.delete();
    }

    public void writeGroupAuditory() throws IOException {
        Log.e("NETWORK", "write to AUDITORY");

        File file = new File(mContext.getFilesDir().getAbsolutePath()+"/rasp_auditory.json");

        JsonFactory jFactory = new JsonFactory();
        JsonParser jParser = jFactory.createParser(file);
        String sql = "INSERT INTO rasp_auditory" +" VALUES (?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        while (jParser.nextToken()!= JsonToken.END_ARRAY){
            statement.clearBindings();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if (DatabaseHelper.KOR_AUDITORY.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(1, jParser.getText());
                }
                if (DatabaseHelper.AUD_AUDITORY.equals(fieldname)) {
                    jParser.nextToken();
                    statement.bindString(2, jParser.getText());
                }
            }
            statement.execute();
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        jParser.close();
        file.delete();
    }

    public String convertNameToId(String name){
        String sqlQuery = "SELECT "+ DatabaseHelper.PREP_TEACHERS +" FROM "+DatabaseHelper.TABLE_TEACHERS
                + " WHERE " + DatabaseHelper.FIO_TEACHERS + "='" +name+"'";
        Cursor cursor = mDatabase.rawQuery(sqlQuery, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(DatabaseHelper.PREP_TEACHERS));
    }

    public String convertIdToName(String id){
        if (!id.equals("0")){
            String sqlQuery = "SELECT "+ DatabaseHelper.FIO_TEACHERS +" FROM "+DatabaseHelper.TABLE_TEACHERS
                    + " WHERE " + DatabaseHelper.PREP_TEACHERS + "='" +id+"'";
            Cursor cursor = mDatabase.rawQuery(sqlQuery, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIO_TEACHERS));
        }
        else
            return "";
    }

    public Observable<ArrayList<String>> getHintList(int type){
        return Observable.create(subscriber -> {
            ArrayList<String> hintList = new ArrayList<String>();
            Cursor cursor;
            String sqlQuery;
            try {
                if (type == 0) {
                    sqlQuery = "SELECT " + DatabaseHelper.GROUP_NUMBER_MAIN + " FROM " + DatabaseHelper.TABLE_MAIN
                            + " GROUP BY " + DatabaseHelper.GROUP_NUMBER_MAIN;
                    cursor = mDatabase.rawQuery(sqlQuery, null);
                    while (cursor.moveToNext()) {
                        if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROUP_NUMBER_MAIN)) != "") {
                            hintList.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROUP_NUMBER_MAIN)));
                        }
                    }
                } else if (type == 1 || type == 3) {
                    sqlQuery = "SELECT " + DatabaseHelper.FIO_TEACHERS + " FROM " + DatabaseHelper.TABLE_TEACHERS;
                    cursor = mDatabase.rawQuery(sqlQuery, null);
                    while (cursor.moveToNext()) {
                        hintList.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIO_TEACHERS)));
                    }
                } else if (type == 2) {
                    sqlQuery = "SELECT " + DatabaseHelper.GRUP_DMAIN + " FROM " + DatabaseHelper.TABLE_DMAIN + " WHERE "
                            + DatabaseHelper.TIP_DMAIN + "='z' GROUP BY " + DatabaseHelper.GRUP_DMAIN;
                    cursor = mDatabase.rawQuery(sqlQuery, null);
                    while (cursor.moveToNext()) {
                        if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRUP_DMAIN)).length() < 7)
                            hintList.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRUP_DMAIN)));
                    }
                } else if (type == 4) {
                    sqlQuery = "SELECT " + DatabaseHelper.GRUP_DMAIN + " FROM " + DatabaseHelper.TABLE_DMAIN +
                            " WHERE " + DatabaseHelper.TIP_DMAIN + "='c' GROUP BY " + DatabaseHelper.GRUP_DMAIN;
                    cursor = mDatabase.rawQuery(sqlQuery, null);
                    while (cursor.moveToNext()) {
                        hintList.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRUP_DMAIN)));
                    }
                } else if (type == 5) {
                    sqlQuery = "SELECT " + DatabaseHelper.AUD_AUDITORY + " FROM " + DatabaseHelper.TABLE_AUDITORY;
                    cursor = mDatabase.rawQuery(sqlQuery, null);
                    while (cursor.moveToNext()) {
                        hintList.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUD_AUDITORY)));
                    }
                } else if (type == 6) {
                    sqlQuery = "SELECT " + DatabaseHelper.GRUP_DMAIN + " FROM " + DatabaseHelper.TABLE_DMAIN
                            + " WHERE " + DatabaseHelper.TIP_DMAIN + "='s' GROUP BY " + DatabaseHelper.GRUP_DMAIN;
                    cursor = mDatabase.rawQuery(sqlQuery, null);
                    while (cursor.moveToNext()) {
                        hintList.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRUP_DMAIN)));
                    }
                }
                subscriber.onNext(hintList);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e.getCause());
            }
        });
    }

    public Observable<ArrayList<FavoriteValue>> createItems(int type, ArrayList<String> favoriteList){
        return getHintList(type).map(strings -> {
            ArrayList<FavoriteValue> resultList = new ArrayList<FavoriteValue>();
            for (String string : strings) {
                if (favoriteList.contains(string))
                    resultList.add(new FavoriteValue(string, true));
                else
                    resultList.add(new FavoriteValue(string, false));
            }
            return resultList;
        });
    }

    public Observable<ArrayList<User>> getFavorites(){
        return Observable.create(subscriber -> {
            ArrayList<User> list = new ArrayList<User>();
            String sqlQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SAVED;
            Cursor cursor = mDatabase.rawQuery(sqlQuery, null);
            try {
                while (cursor.moveToNext()) {
                    User user = new User(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SAVED_TYPE)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.SAVED_INPUT)));
                    list.add(user);
                }
            } finally {
                cursor.close();
            }
            subscriber.onNext(list);
            subscriber.onCompleted();
        });
    }

    public Observable<ArrayList<String>> getFavoritesByType(int type){
        return Observable.create(subscriber -> {
            ArrayList<String> list = new ArrayList<String>();
            String sqlQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SAVED + " WHERE " + DatabaseHelper.SAVED_TYPE
                    + " ='" + type + "'";
            Cursor cursor = mDatabase.rawQuery(sqlQuery, null);
            try {
                while (cursor.moveToNext())
                    list.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SAVED_INPUT)));
            } finally {
                cursor.close();
            }
            subscriber.onNext(list);
            subscriber.onCompleted();
        });
    }

    public void insertFavorite(User favoriteValue){
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.SAVED_INPUT, favoriteValue.getValue());
        newValues.put(DatabaseHelper.SAVED_TYPE, favoriteValue.getType());
        mDatabase.insert(DatabaseHelper.TABLE_SAVED, null, newValues);
    }

    public void deleteFavorite(User favoriteValue){
        mDatabase.delete(DatabaseHelper.TABLE_SAVED, DatabaseHelper.SAVED_INPUT + " = '"
                + favoriteValue.getValue() + "' and " + DatabaseHelper.SAVED_TYPE + " = '" + favoriteValue.getType() + "'", null);
    }

    public Observable<ArrayList<String>> getDatesByMonthObservable(User user){
        return Observable.create(subscriber -> {
            ArrayList<String> list = getDatesByMonth(user);
            subscriber.onNext(list);
            subscriber.onCompleted();
        });
    }

    public ArrayList<String> getDatesByMonth(User user) {
        ArrayList<String> list = new ArrayList<String>();
        String sqlQuery;
        Cursor cursor;
        if (user.getType()==2 || user.getType()==4){
            sqlQuery = "SELECT " + DatabaseHelper.DZ_DMAIN +" FROM "+  DatabaseHelper.TABLE_DMAIN +" WHERE "+
                    DatabaseHelper.GRUP_DMAIN+ "='"+ user.getValue() +"' GROUP BY "+DatabaseHelper.DZ_DMAIN;
        } else if (user.getType()==3){
            sqlQuery = "SELECT "+ DatabaseHelper.DZ_DMAIN +" FROM "+  DatabaseHelper.TABLE_DMAIN +" WHERE "+
                    DatabaseHelper.PREP_DMAIN+ "='"+ user.getValue() +"' GROUP BY "+DatabaseHelper.DZ_DMAIN;
        } else if (user.getType()==5){
            sqlQuery = "SELECT " + DatabaseHelper.DZ_DMAIN +" FROM "+  DatabaseHelper.TABLE_DMAIN +" WHERE "+
                    DatabaseHelper.AUD_DMAIN+ "='"+ user.getValue() +"' GROUP BY "+DatabaseHelper.DZ_DMAIN;
        } else {
            sqlQuery = "SELECT " + DatabaseHelper.DZ_DMAIN +" FROM "+  DatabaseHelper.TABLE_DMAIN +" WHERE "+
                    DatabaseHelper.GRUP_DMAIN+ "='"+ user.getValue() +"' AND " +DatabaseHelper.VID_DMAIN + "='ЭК"
                    +"' GROUP BY "+DatabaseHelper.DZ_DMAIN;
        }
        cursor = mDatabase.rawQuery(sqlQuery, null);
        try{
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DZ_DMAIN)));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public Observable<ArrayList<Timetable>> getLessonsObservable(TransmittedInfo transmittedInfo){
        return Observable.create(subscriber -> {
            ArrayList<Timetable> list = getLessons(transmittedInfo);
            subscriber.onNext(list);
            subscriber.onCompleted();

        });
    }

    public ArrayList<Timetable> getLessons(TransmittedInfo transmittedInfo) {
        ArrayList<Timetable> list = new ArrayList<Timetable>();
        String sqlQuery;
        Cursor cursor;
        Log.e("USER", transmittedInfo.toString());
        if (transmittedInfo.getUser().getType()==0){
            sqlQuery = "SELECT " + DatabaseHelper.PAIR_NUMBER_MAIN + "," + DatabaseHelper.AUDITORY_MAIN+ ","+
                    DatabaseHelper.DISS_SUBJECTS+","+ DatabaseHelper.DISP_SUBJECTS + ","+ DatabaseHelper.PAIR_TYPE_MAIN +
                    ","+ DatabaseHelper.FIO_TEACHERS + " FROM " + DatabaseHelper.TABLE_MAIN + " JOIN " +
                    DatabaseHelper.TABLE_SUBJECTS + " ON " + DatabaseHelper.TABLE_MAIN + "." + DatabaseHelper.SUBJECT_ID_MAIN
                    + "=" + DatabaseHelper.TABLE_SUBJECTS + "." + DatabaseHelper.DIS_SUBJECTS + " JOIN "+
                    DatabaseHelper.TABLE_TEACHERS + " ON " + DatabaseHelper.TABLE_MAIN + "." + DatabaseHelper.TEACHER_ID_MAIN +
                    "="+ DatabaseHelper.TABLE_TEACHERS +"." + DatabaseHelper.PREP_TEACHERS
                    + " WHERE " + DatabaseHelper.GROUP_NUMBER_MAIN + " ='" + transmittedInfo.getUser().getValue() +
                    "' AND " + DatabaseHelper.DAY_OF_WEEK_MAIN + " ='" + transmittedInfo.getDay() + "' AND "+
                    DatabaseHelper.WEEK_MAIN + " ='" + transmittedInfo.getWeek() + "'"  + " ORDER BY " + DatabaseHelper.PAIR_NUMBER_MAIN ;

            cursor = mDatabase.rawQuery(sqlQuery, null);
            try {
                while (cursor.moveToNext()) {
                    Timetable timetable = new Timetable(TimetableUtils.getTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAIR_NUMBER_MAIN)).charAt(0)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUDITORY_MAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAIR_TYPE_MAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIO_TEACHERS)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISS_SUBJECTS)));
                    list.add(timetable);
                }
            } finally {
                cursor.close();
            }
        } else if (transmittedInfo.getUser().getType()==1) {
            sqlQuery = "SELECT " + DatabaseHelper.PAIR_NUMBER_MAIN + ","+ DatabaseHelper.AUDITORY_MAIN+ ","
                    + DatabaseHelper.DISS_SUBJECTS +","+ DatabaseHelper.DISP_SUBJECTS + ","+ DatabaseHelper.PAIR_TYPE_MAIN
                    + ","+ DatabaseHelper.GROUP_NUMBER_MAIN
                    + " FROM " + DatabaseHelper.TABLE_MAIN + " JOIN " + DatabaseHelper.TABLE_SUBJECTS + " ON "
                    + DatabaseHelper.TABLE_MAIN + "." + DatabaseHelper.SUBJECT_ID_MAIN
                    + "=" + DatabaseHelper.TABLE_SUBJECTS + "." + DatabaseHelper.DIS_SUBJECTS //+ " JOIN "
//                    + DatabaseHelper.TABLE_TEACHERS + " ON " + DatabaseHelper.TABLE_MAIN + "." + DatabaseHelper.TEACHER_ID_MAIN
//                    + "="+ DatabaseHelper.TABLE_TEACHERS +"." + DatabaseHelper.PREP_TEACHERS
                    + " WHERE " + DatabaseHelper.TEACHER_ID_MAIN + " ='" + transmittedInfo.getUser().getValue()
                    + "' AND " + DatabaseHelper.DAY_OF_WEEK_MAIN + " ='" + transmittedInfo.getDay() + "' AND "
                    + DatabaseHelper.WEEK_MAIN + " ='" + transmittedInfo.getWeek() + "'"  + " ORDER BY " + DatabaseHelper.PAIR_NUMBER_MAIN ;
            cursor = mDatabase.rawQuery(sqlQuery, null);
            try{
                ArrayList<String> times = new ArrayList<String>();
                ArrayList<String> groups = new ArrayList<String>();

                while (cursor.moveToNext()) {
                    Timetable timetable = new Timetable(TimetableUtils.getTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAIR_NUMBER_MAIN)).charAt(0)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUDITORY_MAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAIR_TYPE_MAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROUP_NUMBER_MAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISS_SUBJECTS)));
                    list.add(timetable);
                    times.add(timetable.getTime());
                    groups.add(timetable.getType());

                }
                for (int i=0;i<times.size()-1;i++) {
                    if (times.get(i).equals(times.get(i+1))) {
                        list.get(i).setType(groups.get(i) + "," + groups.get(i + 1));
                        groups.set(i, groups.get(i) + "," + groups.get(i + 1));
                        list.remove(i+1);
                        times.remove(i+1);
                        groups.remove(i+1);
                        i=-1;
                    }
                }
            } finally {
                cursor.close();
            }
        } else if (transmittedInfo.getUser().getType()==2 ||transmittedInfo.getUser().getType()==4){
            sqlQuery = "SELECT " + DatabaseHelper.PARA_DMAIN+","+ DatabaseHelper.DISS_SUBJECTS+","+
                    DatabaseHelper.VID_DMAIN+","+ DatabaseHelper.AUD_DMAIN+ ","+ DatabaseHelper.FIO_TEACHERS+","+
                    DatabaseHelper.DISP_SUBJECTS+ " FROM " + DatabaseHelper.TABLE_DMAIN + " JOIN " + DatabaseHelper.TABLE_SUBJECTS +
                    " ON " + DatabaseHelper.TABLE_DMAIN + "." + DatabaseHelper.DIS_DMAIN
                    + "=" + DatabaseHelper.TABLE_SUBJECTS + "." + DatabaseHelper.DIS_SUBJECTS + " JOIN "+
                    DatabaseHelper.TABLE_TEACHERS + " ON " + DatabaseHelper.TABLE_DMAIN + "." + DatabaseHelper.PREP_DMAIN + "="+
                    DatabaseHelper.TABLE_TEACHERS +"." + DatabaseHelper.PREP_TEACHERS
                    + " WHERE " + DatabaseHelper.GRUP_DMAIN +" ='" + transmittedInfo.getUser().getValue()+"'" +" AND "
                    + DatabaseHelper.DZ_DMAIN  + "='"+ transmittedInfo.getDate()+"' ORDER BY " + DatabaseHelper.PARA_DMAIN;
            cursor = mDatabase.rawQuery(sqlQuery, null);
            try{
                while (cursor.moveToNext()) {
                    Timetable timetable = new Timetable(TimetableUtils.getTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PARA_DMAIN)).charAt(0)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUD_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.VID_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIO_TEACHERS)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISS_SUBJECTS)
                            )) ;
                    list.add(timetable);
                }
            } finally {
                cursor.close();
            }
        }
        else if (transmittedInfo.getUser().getType()==3){
            sqlQuery = "SELECT " + DatabaseHelper.PARA_DMAIN+","+ DatabaseHelper.DISS_SUBJECTS+","+ DatabaseHelper.VID_DMAIN+","+
                    DatabaseHelper.AUD_DMAIN+ ","+ DatabaseHelper.GRUP_DMAIN+","+ DatabaseHelper.DISP_SUBJECTS+ " FROM " +
                    DatabaseHelper.TABLE_DMAIN + " JOIN " + DatabaseHelper.TABLE_SUBJECTS + " ON " + DatabaseHelper.TABLE_DMAIN +
                    "." + DatabaseHelper.DIS_DMAIN
                    + "=" + DatabaseHelper.TABLE_SUBJECTS + "." + DatabaseHelper.DIS_SUBJECTS
                    + " WHERE " + DatabaseHelper.PREP_DMAIN +" ='" + transmittedInfo.getUser().getValue()+"'" +" AND "
                    + DatabaseHelper.DZ_DMAIN  + "='"+ transmittedInfo.getDate()+"' ORDER BY " + DatabaseHelper.PARA_DMAIN;
            cursor = mDatabase.rawQuery(sqlQuery, null);
            try{
                while (cursor.moveToNext()) {
                    Timetable timetable = new Timetable(TimetableUtils.getTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PARA_DMAIN)).charAt(0)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUD_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.VID_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRUP_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISS_SUBJECTS)
                            ));
                    list.add(timetable);
                }
            } finally {
                cursor.close();
            }
        }
        else if (transmittedInfo.getUser().getType()==5){
            sqlQuery = "SELECT " + DatabaseHelper.PARA_DMAIN+","+ DatabaseHelper.DISS_SUBJECTS+","+
                    DatabaseHelper.VID_DMAIN+","+ DatabaseHelper.FIO_TEACHERS+ ","+ DatabaseHelper.GRUP_DMAIN+","+
                    DatabaseHelper.DISP_SUBJECTS+ " FROM " + DatabaseHelper.TABLE_DMAIN +" JOIN " +
                    DatabaseHelper.TABLE_SUBJECTS + " ON " + DatabaseHelper.TABLE_DMAIN  + "." + DatabaseHelper.DIS_DMAIN + "=" +
                    DatabaseHelper.TABLE_SUBJECTS+"."+ DatabaseHelper.DIS_SUBJECTS
                    + " JOIN "+ DatabaseHelper.TABLE_TEACHERS + " ON " + DatabaseHelper.TABLE_DMAIN + "." + DatabaseHelper.PREP_DMAIN +
                    "="+ DatabaseHelper.TABLE_TEACHERS +"." + DatabaseHelper.PREP_TEACHERS
                    + " WHERE " + DatabaseHelper.AUD_DMAIN +" ='" + transmittedInfo.getUser().getValue()+"'"
                    +" AND " + DatabaseHelper.DZ_DMAIN  + "='"+ transmittedInfo.getDate()+"' ORDER BY " + DatabaseHelper.PARA_DMAIN;
            cursor = mDatabase.rawQuery(sqlQuery, null);
            try{
                while (cursor.moveToNext()) {
                    Timetable timetable = new Timetable(TimetableUtils.getTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PARA_DMAIN)).charAt(0)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIO_TEACHERS)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.VID_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRUP_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISS_SUBJECTS)
                            ));
                    list.add(timetable);
                }
            } finally {
                cursor.close();
            }
        }
        else if (transmittedInfo.getUser().getType()==6){
            sqlQuery = "SELECT " + DatabaseHelper.PARA_DMAIN+","+ DatabaseHelper.DISS_SUBJECTS+","+
                    DatabaseHelper.VID_DMAIN+","+ DatabaseHelper.AUD_DMAIN+ ","+ DatabaseHelper.FIO_TEACHERS+","+
                    DatabaseHelper.DISP_SUBJECTS+ " FROM " + DatabaseHelper.TABLE_DMAIN + " JOIN " + DatabaseHelper.TABLE_SUBJECTS
                    + " ON " + DatabaseHelper.TABLE_DMAIN + "." + DatabaseHelper.DIS_DMAIN
                    + "=" + DatabaseHelper.TABLE_SUBJECTS + "." + DatabaseHelper.DIS_SUBJECTS + " JOIN "+
                    DatabaseHelper.TABLE_TEACHERS + " ON " + DatabaseHelper.TABLE_DMAIN + "." + DatabaseHelper.PREP_DMAIN +
                    "="+ DatabaseHelper.TABLE_TEACHERS +"." + DatabaseHelper.PREP_TEACHERS
                    + " WHERE " + DatabaseHelper.VID_DMAIN+"='ЭК' AND "+ DatabaseHelper.GRUP_DMAIN +" ='" +
                    transmittedInfo.getUser().getValue()+"'" +" AND " + DatabaseHelper.DZ_DMAIN  + "='"+
                    transmittedInfo.getDate()+"' ORDER BY " + DatabaseHelper.PARA_DMAIN;
            cursor = mDatabase.rawQuery(sqlQuery, null);
            try{
                while (cursor.moveToNext()) {

                    Timetable timetable = new Timetable(TimetableUtils.getTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PARA_DMAIN)).charAt(0)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUD_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.VID_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRUP_DMAIN)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISS_SUBJECTS)
                            ));
                    list.add(timetable);
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }
}
