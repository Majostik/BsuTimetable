package ru.majo.bsutimetable.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import android.widget.Toast;

import com.jenzz.materialpreference.Preference;
import com.jenzz.materialpreference.SwitchPreference;

import javax.inject.Inject;

import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.NetworkManager;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.ui.MainActivity;
import ru.majo.bsutimetable.utils.AlarmUtils;
import ru.majo.bsutimetable.utils.TimetableUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Majo on 01.02.2015.
 */
public class SettingsFragment extends PreferenceFragment {

    @Inject
    NetworkManager mNetworkManager;
    @Inject
    SharedPreferenceHelper mSharedPreferenceHelper;
    @Inject
    DatabaseManager mDatabaseManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Application.getComponent(getActivity()).inject(this);

        Preference downloadPreference = (Preference)findPreference(getString(R.string.settings_update));
        downloadPreference.setOnPreferenceClickListener(preference -> {
            if (mNetworkManager.checkInternet()) {
                ((MainActivity)getActivity()).onStartProgress();
                mNetworkManager.donwloadFullDatabase()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(aBoolean -> {
                            mSharedPreferenceHelper.setAutomaticUpdateDate(TimetableUtils.todayDate());
                            ((MainActivity)getActivity()).onFinishProgress();
                        },throwable -> {
                            mDatabaseManager.writeDatabase()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(bool -> {
                                        ((MainActivity)getActivity()).onFinishProgress();
                                        ((MainActivity)getActivity()).showErrorToast(getString(R.string.download_error));
                                    });
                        });
            } else
                Toast.makeText(getActivity(), R.string.internet_error, Toast.LENGTH_SHORT).show();
            return false;
        });
        Preference changePreference = (Preference)findPreference(getString(R.string.settings_changevalue));
        changePreference.setSummary(mSharedPreferenceHelper.getUser().getTitleValue());
        changePreference.setOnPreferenceClickListener(preference -> {
            getFragmentManager().beginTransaction().replace(R.id.container,
                    LoginFragment.newInstance(true)).addToBackStack(null).commit();
            return false;
        });
        SwitchPreference autoupdatePreference = (SwitchPreference)findPreference(getString(R.string.settings_autoupdate));
        autoupdatePreference.setDefaultValue(mSharedPreferenceHelper.isAutomaticEnabled());
        autoupdatePreference.setOnPreferenceClickListener(preference -> {
            mSharedPreferenceHelper.setAutomaticEnable(autoupdatePreference.isChecked());
            return false;
        });

        SwitchPreference notifcationPreference = (SwitchPreference)findPreference(getString(R.string.settings_notification));
        notifcationPreference.setDefaultValue(mSharedPreferenceHelper.isNotificationEnabled());
        notifcationPreference.setOnPreferenceClickListener(preference -> {
            if (notifcationPreference.isChecked())
                AlarmUtils.startAlarmService(getActivity());
            else
                AlarmUtils.stopAlarmService(getActivity());
            mSharedPreferenceHelper.setNotificationEnable(notifcationPreference.isChecked());
            return false;
        });

        Preference feedbackPreference = (Preference)findPreference(getString(R.string.settings_feedback));
        feedbackPreference.setOnPreferenceClickListener(preference -> {
            sendFeedBack();
            return false;
        });

        Preference versionPreference = (Preference)findPreference(getString(R.string.settings_version));
        versionPreference.setSummary(getVersion());
    }


    private void sendFeedBack(){
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_mail)});
        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_title));
        startActivity(Intent.createChooser(Email, "Send Feedback:"));
    }

    private String getVersion(){
        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(
                    getActivity().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
