package ru.majo.bsutimetable.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import rx.Observable;

/**
 * Created by Majo on 31.01.2016.
 */
public class NetworkManager {

    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private DatabaseManager mDatabaseManager;

    private static final String mUrlDmain = "http://www.bsu.ru/content/rasp/rasp_dmain.json";
    private static final String mUrlMain = "http://www.bsu.ru/content/rasp/rasp_main.json";
    private static final String mUrlTeacher = "http://www.bsu.ru/content/rasp/rasp_teachers.json";
    private static final String mUrlSubjects = "http://www.bsu.ru/content/rasp/rasp_subjects.json";
    private String mUrlGroup = "http://www.bsu.ru/content/rasp/rasp_group.json";
    private String mUrlAuditory = "http://www.bsu.ru/content/rasp/rasp_auditory.json";

    public NetworkManager(Context context, OkHttpClient okHttpClient,DatabaseManager databaseManager){
        mContext = context;
        mOkHttpClient = okHttpClient;
        mDatabaseManager = databaseManager;
    }

    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        NetworkInfo niw = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (ni == null)
            return false;
        else if (niw.isConnected() || ni!=null)
            return true;
        else
            return false;

    }

    //Need a normal REST API
    public Observable<Boolean> donwloadFullDatabase(){
        return Observable.create(subscriber -> {
            Request request = new Request.Builder()
                    .url(mUrlDmain)
                    .build();
            mDatabaseManager.clearDataBase();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    OutputStream outputStream = new FileOutputStream(
                            String.format("%s/rasp_dmain.json", mContext.getFilesDir().getAbsolutePath()));
                    byte data[] = new byte[8192];
                    int count;
                    while((count = response.body().byteStream().read(data)) != -1){
                        outputStream.write(data, 0, count);
                    }
                    Log.e("NETWORK", "DMAIN START");
                    mDatabaseManager.writeDMainTable();

                }else {
                    subscriber.onError(new IOException());
                }

            } catch (IOException e) {
                subscriber.onError(new IOException());
            }

            request = new Request.Builder()
                    .url(mUrlMain)
                    .build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    OutputStream outputStream = new FileOutputStream(
                            String.format("%s/rasp_main.json", mContext.getFilesDir().getAbsolutePath()));
                    byte data[] = new byte[8192];
                    int count;
                    while((count = response.body().byteStream().read(data)) != -1){
                        outputStream.write(data, 0, count);
                    }
                    Log.e("NETWORK","MAIN START");
                    mDatabaseManager.writeMainTable();

                }else {
                    subscriber.onError(new IOException());
                }

            } catch (IOException e) {
                subscriber.onError(new IOException());
            }

            request = new Request.Builder()
                    .url(mUrlTeacher)
                    .build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    OutputStream outputStream = new FileOutputStream(
                            String.format("%s/rasp_teachers.json", mContext.getFilesDir().getAbsolutePath()));
                    byte data[] = new byte[8192];
                    int count;
                    while((count = response.body().byteStream().read(data)) != -1){
                        outputStream.write(data, 0, count);
                    }
                    Log.e("NETWORK","TEACHERS START");
                    mDatabaseManager.writeTeachersTable();

                }else {
                    subscriber.onError(new IOException());
                }

            } catch (IOException e) {
                subscriber.onError(new IOException());
            }

            request = new Request.Builder()
                    .url(mUrlSubjects)
                    .build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    OutputStream outputStream = new FileOutputStream(
                            String.format("%s/rasp_subjects.json", mContext.getFilesDir().getAbsolutePath()));
                    byte data[] = new byte[8192];
                    int count;
                    while((count = response.body().byteStream().read(data)) != -1){
                        outputStream.write(data, 0, count);
                    }
                    Log.e("NETWORK","SUBJECTS START");
                    mDatabaseManager.writeSubjectsTable();

                }else {
                    subscriber.onError(new IOException());
                }

            } catch (IOException e) {
                subscriber.onError(new IOException());
            }

            request = new Request.Builder()
                    .url(mUrlGroup)
                    .build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    OutputStream outputStream = new FileOutputStream(
                            String.format("%s/rasp_group.json", mContext.getFilesDir().getAbsolutePath()));
                    byte data[] = new byte[8192];
                    int count;
                    while((count = response.body().byteStream().read(data)) != -1){
                        outputStream.write(data, 0, count);
                    }
                    mDatabaseManager.writeGroupTable();

                }else {
                    subscriber.onError(new IOException());
                }

            } catch (IOException e) {
                subscriber.onError(new IOException());
            }

            request = new Request.Builder()
                    .url(mUrlAuditory)
                    .build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    OutputStream outputStream = new FileOutputStream(
                            String.format("%s/rasp_auditory.json", mContext.getFilesDir().getAbsolutePath()));
                    byte data[] = new byte[8192];
                    int count;
                    while((count = response.body().byteStream().read(data)) != -1){
                        outputStream.write(data, 0, count);
                    }
                    mDatabaseManager.writeGroupAuditory();

                }else {
                    subscriber.onError(new IOException());
                }

            } catch (IOException e) {
                subscriber.onError(new IOException());
            }


            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }
}
