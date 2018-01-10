package app.qk.teliver.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.UserBuilder;

import app.qk.teliver.R;
import app.qk.teliver.fragments.FragmentCustomer;
import app.qk.teliver.utils.Utils;

public class ActivityCustomer extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private Toolbar toolbar;

    private FragmentManager fragmentManager;

    private View rootView;

    private Snackbar snackbar;

    private FragmentCustomer fragmentCustomer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_toolbar);
        setSupportActionBar(toolbar);
        Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        rootView = findViewById(R.id.view_root);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        changeFragment(0);
        Teliver.identifyUser(new UserBuilder("test_customer")
                .setUserType(UserBuilder.USER_TYPE.CONSUMER)
                .registerPush()
                .build());
    }

    private void changeFragment(int caseValue) {
        if (caseValue == 0) {
            if (fragmentCustomer == null)
                fragmentCustomer = new FragmentCustomer();
            switchView(fragmentCustomer, getString(R.string.app_name));
        }
    }

    private void switchView(final Fragment fragment, final String title) {
        try {
            toolbar.setTitle(title);
            FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
            Fragment mTempFragment = fragmentManager.findFragmentById(R.id.view_container);
            if (!fragment.equals(mTempFragment)) {
                String className = fragment.getClass().getName();
                boolean isAdded = fragmentManager.popBackStackImmediate(className, 0);
                if (!isAdded) {
                    mFragmentTransaction.addToBackStack(className);
                    mFragmentTransaction.add(R.id.view_container, fragment, title);
                }
            }
            mFragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackStackChanged() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.view_container);
        if (fragment == null)
            return;
        String tag = fragment.getTag();
        toolbar.setTitle(tag);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1)
            fragmentManager.popBackStackImmediate();
        else if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
            finish();
        } else {
            snackbar = Snackbar.make(rootView, R.string.txt_press_back, 3000);
            snackbar.show();
        }
    }

}
