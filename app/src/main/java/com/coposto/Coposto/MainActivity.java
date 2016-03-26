package com.coposto.Coposto;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coposto.R;
import com.coposto.RegistrationProcess.LoginActivity;
import com.coposto.RegistrationProcess.SignUpActivity;
import com.coposto.adapters.NavListAdapter;
import com.coposto.fragments.MyAbout;
import com.coposto.fragments.MyHome;
import com.coposto.fragments.MyProfile;
import com.coposto.fragments.MySettings;
import com.coposto.models.NavItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static Boolean on_login = false;
    public static String Data_choosen;
    public static DrawerLayout drawerLayout;
    public static RelativeLayout drawerPane;
    public static ListView lvNav;
    public static TextView USER_name;
    public static List<NavItem> listNavItems;
    public static List<Fragment> listFragments;
    public static Boolean photo_upload_onclick=true;
    private static final int RESULT_LOAD_IMAGE=1;
    CircleImageView profile_pic;

    ActionBarDrawerToggle actionBarDrawerToggle;

    public void openDrawePanel()
    {
        drawerLayout.openDrawer(drawerPane);
    }
    public void listItemisClicked(AdapterView<?> parent, View view, int position, long id)
    {
        // replace the fragment with the selection correspondingly:
        if (position==3) {
            startActivity(new Intent(MainActivity.this, MyProfile.class));
        }
        else
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, listFragments.get(position))
                    .commit();

            setTitle(listNavItems.get(position).getTitle());
            lvNav.setItemChecked(position, true);
            drawerLayout.closeDrawer(drawerPane);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.);
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_second)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        on_login = true;
        //String passedArg = getIntent().getExtras().getString("arg");
        //enteredValue.setText(passedArg);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.getBackground().setAlpha(0);
        //setSupportActionBar(myToolbar);

        profile_pic = (CircleImageView) findViewById(R.id.icon);



        USER_name = (TextView) findViewById(R.id.user_name);
        USER_name.setText("Coposto User"/*passedArg*/);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lvNav = (ListView) findViewById(R.id.nav_list);

        //drawerLayout.setBackgroundResource(R.drawable.background_send_smooth);

        listNavItems = new ArrayList<NavItem>();
        listNavItems.add(new NavItem("Home", "MyHome page", R.drawable.home_icon));
        listNavItems.add(new NavItem("Settings", "Change something", R.drawable.setting_icon));
        listNavItems.add(new NavItem("About", "Author's information", R.drawable.about_icon));
        listNavItems.add(new NavItem("Profile", "MyProfile information", R.drawable.profile_icon));
        listNavItems.add(new NavItem("Logut", "Login page", R.drawable.login_icon));
        //listNavItems.add(new NavItem("Sign Up", "Sign Up page", R.drawable.signup_icon));
        NavListAdapter navListAdapter = new NavListAdapter(getApplicationContext(), R.layout.item_nav_list, listNavItems);

        lvNav.setAdapter(navListAdapter);


        listFragments = new ArrayList<>();
        listFragments.add(new MyHome(this, lvNav));
        listFragments.add(new MySettings());
        listFragments.add(new MyAbout());
        //listFragments.add(new MyProfile());
        //listFragments.add(new MyLogin());
        //listFragments.add(new MySignup());
        // load first fragment as default:


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(0)).commit();

        setTitle(listNavItems.get(0).getTitle());
        lvNav.setItemChecked(0, true);
        drawerLayout.closeDrawer(drawerPane);

        // set listener for navigation items:


        // create listener for drawer layout
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.drawer_opened, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };

    }
    public void setData(String data){Data_choosen = data;}
    public String getData(){
        return Data_choosen;
    }
    public DrawerLayout getDrawerLayout(){
          return drawerLayout;
    }
    public RelativeLayout  getRelativeLayout(){
        return drawerPane;
    }
    public ListView getListView(){
        return lvNav;
    }
    public List<NavItem> getListNavItem(){
        return listNavItems;
    }
    public List<Fragment> getListFragment(){
        return listFragments;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 1) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Close COPOSTO?")
                .setMessage("Do you want to exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the MyHome/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selected_image = data.getData();
            profile_pic.setImageURI(selected_image);
        }

    }
    public void uploadPic(View view)
    {
        Intent gallery = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);

    }
}
