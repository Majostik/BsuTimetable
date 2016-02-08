package ru.majo.bsutimetable.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;

/**
 * Created by Majo on 16.01.2016.
 */
@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application mApp) {
        this.mApp = mApp;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return mApp.getApplicationContext();
    }

    @Singleton
    @Provides
    public SharedPreferenceHelper provideSharedPreferenceHelper(Context context, DatabaseManager databaseManager){
        return new SharedPreferenceHelper(context,databaseManager);
    }



}
