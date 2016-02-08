package ru.majo.bsutimetable.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.majo.bsutimetable.database.DatabaseHelper;
import ru.majo.bsutimetable.manager.DatabaseManager;

/**
 * Created by Majo on 02.02.2016.
 */
@Module
public class DatabaseModule {

    @Singleton
    @Provides
    public DatabaseHelper provideDatabaseHelper(Context context){
        return new DatabaseHelper(context);
    }

    @Singleton
    @Provides
    public DatabaseManager provideDatabaseManager(Context context, DatabaseHelper databaseHelper){
        return new DatabaseManager(context,databaseHelper);
    }
}
