package ru.majo.bsutimetable.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.manager.NetworkManager;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.presenter.MainPresenter;
import ru.majo.bsutimetable.ui.fragment.FavoriteFragment;
import ru.majo.bsutimetable.ui.fragment.LoginFragment;
import ru.majo.bsutimetable.ui.fragment.PickTypeFragment;
import ru.majo.bsutimetable.ui.fragment.SettingsFragment;
import ru.majo.bsutimetable.ui.fragment.TimetableFragment;
import ru.majo.bsutimetable.ui.view.MainView;
import ru.majo.bsutimetable.utils.TimetableUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.toolbar_text)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    View mHeaderView;

    private MaterialDialog mMaterialDialog;

    @Inject
    MainPresenter mPresenter;

    @Inject
    NetworkManager mNetworkManager;

    private ActionBarDrawerToggle mToogle;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Application.getComponent(this).inject(this);
        ButterKnife.bind(this);
        initViews();
        if (savedInstanceState==null) {
            mPresenter.onInitFirstFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        clearBackstack();
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.drawer_timetable:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new PickTypeFragment()).commit();
                return true;
            case R.id.drawer_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new FavoriteFragment()).commit();
                return true;
            case R.id.drawer_settings:
                getToolbarTitle().
                        setText(getString(R.string.drawer_settings));
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new SettingsFragment()).commit();
                return true;
            case R.id.drawer_home:
                new Handler().postDelayed(() -> mPresenter.onInitFirstFragment(), 130);
                return true;
            default:
                return true;
        }
    }


    @Override
    public void showLoginFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, LoginFragment.newInstance(false)).commit();
    }

    @Override
    public void showMainFragment(User user) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, TimetableFragment.newInstance(user)).commit();
    }

    @Override
    public void setNavigationHeader(User user) {
        if (mHeaderView!=null) {
            TextView textType = (TextView) mHeaderView.findViewById(R.id.header_text_type);
            TextView textHeader = (TextView) mHeaderView.findViewById(R.id.header_text);

            textType.setText(TimetableUtils.convertType(user.getType()));
            textHeader.setText(user.getTitleValue());
        }
    }

    @Override
    public void onStartProgress() {
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
        mDialog.setContentView(R.layout.dialog_progress);
    }

    @Override
    public void onFinishProgress() {
        if (mDialog!=null){
            mDialog.dismiss();
        }
    }

    @Override
    public void showUpdateDialog() {
        mMaterialDialog = new MaterialDialog(this)
                .setTitle(getString(R.string.update_title))
                .setMessage(getString(R.string.update_desrciption))
                .setPositiveButton(getString(R.string.update_ok), v -> {
                    mMaterialDialog.dismiss();
                    mPresenter.startUpdate();
                })
                .setNegativeButton(getString(R.string.update_cancel), v -> {
                    mMaterialDialog.dismiss();
                });

        mMaterialDialog.show();
    }

    @Override
    public void showErrorToast(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }

    private void initViews(){
        mPresenter.attachView(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (mDrawerLayout!=null) {
            mHeaderView = getLayoutInflater().inflate(R.layout.drawer_header,null);

            mNavigationView.addHeaderView(mHeaderView);
            mPresenter.setNavView();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mToogle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.setDrawerListener(mToogle);
            mToogle.setDrawerIndicatorEnabled(true);
            mToogle.syncState();
            mNavigationView.setNavigationItemSelectedListener(this);
            setBackstackIndicator();
            getSupportFragmentManager().addOnBackStackChangedListener(() -> {
                setBackstackIndicator();
            });
        }
        mPresenter.onWriteDatabase();
        mPresenter.isTodayToUpdate();
    }

    private void setBackstackIndicator() {
        int backCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backCount == 0) {
            setIndicatorEnabled(true);
        } else {
            mToogle.setToolbarNavigationClickListener(v -> onBackPressed());
            setIndicatorEnabled(false);
        }
    }

    public TextView getToolbarTitle(){
        return mToolbarTitle;
    }


    private void setIndicatorEnabled(boolean enabled) {
        if (null != mToogle) {
            mToogle.setDrawerIndicatorEnabled(enabled);
        }
    }

    private void clearBackstack(){
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }


}
