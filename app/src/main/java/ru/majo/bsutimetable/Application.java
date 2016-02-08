package ru.majo.bsutimetable;

import android.content.Context;

import ru.majo.bsutimetable.di.component.AppComponent;
import ru.majo.bsutimetable.di.component.DaggerAppComponent;
import ru.majo.bsutimetable.di.module.AppModule;

/**
 * Created by Majo on 16.01.2016.
 */
public class Application extends android.app.Application {

    private AppComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = createComponent();
    }

    private AppComponent createComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getComponent(Context context) {
        return ((Application)context.getApplicationContext()).mApplicationComponent;
    }

}
