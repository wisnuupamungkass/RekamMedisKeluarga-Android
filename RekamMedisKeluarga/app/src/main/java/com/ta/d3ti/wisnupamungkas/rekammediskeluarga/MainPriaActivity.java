package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainPriaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected JSONParser jsonParser;
    protected int success;
    protected TextView email_drawer;
    protected ImageView foto_profil;
    protected String stremail;

    protected ImageLoader imageLoader;
    SessionManager session;

    public void onStart(){
        super.onStart();
        this.session = new SessionManager(getApplicationContext());
        session.isLoggedInUser();
        this.session.checkLoginPria();

        this.stremail = this.session.getUserDetails().get(SessionManager.KEY_EMAIL);

        new drawer_pria().execute();
    }

    public class drawer_pria extends AsyncTask<String,Void,String> {
        String strfoto;
        String url_drawer_pria;

        public drawer_pria(){
            this.url_drawer_pria= "http://rekammedis.gudangtechno.web.id/android/profil_orangtua.php?email="+MainPriaActivity.this.stremail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = MainPriaActivity.this.jsonParser.getJSONFromUrl(url_drawer_pria);
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("orangtua");
                if (success == 1) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        this.strfoto = c.getString("foto").trim();
                        Log.e("OK", " Ambil Data");
                    }
                }
            } catch (JSONException e) {
                Log.e("Error", " Tidak Bisa Ambil Data");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (success == 1) {
                if (this.strfoto == null) {
                    MainPriaActivity.this.foto_profil.setImageBitmap(BitmapFactory.decodeResource(MainPriaActivity.this.getResources(), R.drawable.pria));
                } else {
                    ImageLoader.getInstance().displayImage(this.strfoto, MainPriaActivity.this.foto_profil);
                }
                MainPriaActivity.this.email_drawer.setText(stremail);
            }
        }
    }

    public MainPriaActivity(){
        this.jsonParser = new JSONParser();
    }

    class keluar implements OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            MainPriaActivity.this.finish();
        }
    }

    class batal implements OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       session = new SessionManager(getApplicationContext());
        session.isLoggedInUser();
        session.checkLoginPria();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.foto_profil = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.foto_drawer_pria);
        this.email_drawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_drawer);

        FragmentTransaction fragmentTransaction;
        HomePriaFragment fragment = new HomePriaFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("Home Pria");
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
        getMenuInflater().inflate(R.menu.main_perempuan, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction;

        if (id == R.id.nav_home_pria) {
            HomePriaFragment fragment1 = new HomePriaFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment1);
            fragmentTransaction.commit();
            setTitle("Home Pria");
        }

        else if (id == R.id.nav_profil_pria) {
            ProfilPriaFragment fragment2 = new ProfilPriaFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment2);
            fragmentTransaction.commit();
            setTitle("Profil Pengguna");
        }

        else if (id == R.id.nav_bantuan_pria) {
            BantuanFragment fragment3 = new BantuanFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment3);
            fragmentTransaction.commit();
            setTitle("Pusat Bantuan");
        }

        else if (id == R.id.nav_rekammedis_pria) {
            MainPriaActivity.this.startActivity(new Intent(MainPriaActivity.this, ListRekamMedisPriaActivity.class));
        }

        else if(id == R.id.nav_logout_pria){
            Builder builder = new Builder(this);
            builder.setMessage("Apakah Ingin Keluar Aplikasi ?");
            builder.setCancelable(false);
            builder.setPositiveButton("YA", new keluar());
            builder.setNegativeButton("TIDAK", new batal());
            builder.create().show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}