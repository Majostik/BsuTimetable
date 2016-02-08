package ru.majo.bsutimetable.di.module;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.NetworkManager;

/**
 * Created by Majo on 02.02.2016.
 */
@Module
public class NetworkModule {

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(){
        return new OkHttpClient();
    }

    @Singleton
    @Provides
    public NetworkManager provideNetworkManager(Context context, OkHttpClient okHttpClient,
                                                DatabaseManager databaseManager){
        return new NetworkManager(context, okHttpClient, databaseManager);
    }
}
