package ru.majo.bsutimetable.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import ru.majo.bsutimetable.di.module.AppModule;
import ru.majo.bsutimetable.di.module.DatabaseModule;
import ru.majo.bsutimetable.di.module.NetworkModule;
import ru.majo.bsutimetable.provider.TimetableWidgetProvider;
import ru.majo.bsutimetable.receiver.BootReceiver;
import ru.majo.bsutimetable.service.AlarmService;
import ru.majo.bsutimetable.service.TimetableWidgetFactory;
import ru.majo.bsutimetable.ui.MainActivity;
import ru.majo.bsutimetable.ui.adapter.FavoriteAdapter;
import ru.majo.bsutimetable.ui.adapter.PickValueAdapter;
import ru.majo.bsutimetable.ui.fragment.FavoriteFragment;
import ru.majo.bsutimetable.ui.fragment.FullTimeLessonsFragment;
import ru.majo.bsutimetable.ui.fragment.LoginFragment;
import ru.majo.bsutimetable.ui.fragment.PartTimeLessonsFragment;
import ru.majo.bsutimetable.ui.fragment.PickValueFragment;
import ru.majo.bsutimetable.ui.fragment.SettingsFragment;
import ru.majo.bsutimetable.ui.fragment.TimetableFragment;

/**
 * Created by Majo on 16.01.2016.
 */
@Singleton
@Component(modules = {AppModule.class,
        DatabaseModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(Application app);
    void inject(MainActivity activity);
    void inject(TimetableFragment fragment);
    void inject(LoginFragment fragment);
    void inject(FavoriteFragment fragment);
    void inject(PickValueFragment fragment);
    void inject(PickValueAdapter adapter);
    void inject(FavoriteAdapter adapter);
    void inject(FullTimeLessonsFragment fragment);
    void inject(PartTimeLessonsFragment fragment);
    void inject(TimetableWidgetProvider provider);
    void inject(TimetableWidgetFactory factory);
    void inject(AlarmService service);
    void inject(BootReceiver receiver);
    void inject(SettingsFragment fragment);
}
