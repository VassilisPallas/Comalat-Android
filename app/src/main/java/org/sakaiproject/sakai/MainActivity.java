package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.user.data.Profile;
import org.sakaiproject.api.user.data.User;

import java.net.CookieHandler;
import java.net.CookieManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            selectFragment(welcomeFragment, R.id.content_frame, "Welcome");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NetWork.isConnected(this);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        User.nullInstance();
        Profile.nullInstance();
        Connection.nullSessionId();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectFragment(Fragment f, int id, String title) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, f);
        fragmentTransaction.commit();
        setTitle(title);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.welcome:
                WelcomeFragment welcomeFragment = new WelcomeFragment();
                selectFragment(welcomeFragment, R.id.content_frame, "Welcome");
                break;
            case R.id.about:
                break;
            case R.id.features:
                break;
            case R.id.site_browser:
                break;
            case R.id.training:
                break;
            case R.id.acknowledgements:
                break;
            case R.id.help:
                WebViewFragment webViewFragment = new WebViewFragment();
                selectFragment(webViewFragment, R.id.content_frame, "Help");
                break;
            case R.id.login:
                LoginFragment loginFragment = new LoginFragment();
                selectFragment(loginFragment, R.id.content_frame, "OnlineLogin");
                break;
            case R.id.new_user:
                SignupFragment signupFragment = new SignupFragment();
                selectFragment(signupFragment, R.id.content_frame, "New Account");
                break;
            case R.id.reset_password:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
