package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainPetugasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected JSONParser jsonParser;
    protected int success;
    protected TextView email_drawer;
    protected ImageView foto_profil_petugas;
    protected String stremail;

    protected ImageLoader imageLoader;
    SessionManager session;

    class keluar implements OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            session.logoutPetugas();
            MainPetugasActivity.this.finish();
        }
    }

    class batal implements OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public MainPetugasActivity(){
        this.jsonParser = new JSONParser();
    }

    public class drawer_petugas extends AsyncTask<String,Void,String> {
        String strfoto;
        String url_drawer_petugas;

        public drawer_petugas(){
            this.url_drawer_petugas= "http://rekammedis.gudangtechno.web.id/android/profil_petugas.php?email="+MainPetugasActivity.this.stremail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = MainPetugasActivity.this.jsonParser.getJSONFromUrl(url_drawer_petugas);
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("petugas");
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
                    MainPetugasActivity.this.foto_profil_petugas.setImageBitmap(BitmapFactory.decodeResource(MainPetugasActivity.this.getResources(), R.drawable.doctor));
                } else {
                    ImageLoader.getInstance().displayImage(this.strfoto, MainPetugasActivity.this.foto_profil_petugas);
                }
                MainPetugasActivity.this.email_drawer.setText(stremail);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_petugas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainPetugasActivity.this.session = new SessionManager(getApplicationContext());
        session.isLoggedInPetugas();
        session.checkLoginPetugas();

        this.stremail = this.session.getPetugasDetails().get(SessionManager.KEY_EMAIL);

        new drawer_petugas().execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.foto_profil_petugas = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.foto_drawer_petugas);
        this.email_drawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_drawer);

        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction fragmentTransaction;
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("Home Petugas");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Builder builder = new Builder(this);
            builder.setMessage("Apakah Ingin Keluar Aplikasi ?");
            builder.setCancelable(false);
            builder.setPositiveButton("YA", new keluar());
            builder.setNegativeButton("TIDAK", new batal());
            builder.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_petugas, menu);
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

        if (id == R.id.nav_home_petugas) {
            HomeFragment fragment = new HomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            setTitle("Home Petugas");
        }

        else if (id == R.id.nav_profil_petugas) {
            ProfilPetugasFragment fragment2 = new ProfilPetugasFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment2);
            fragmentTransaction.commit();
            setTitle("Profil Petugas");
        }

        else if (id == R.id.nav_bantuan) {
            BantuanFragment fragment3 = new BantuanFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment3);
            fragmentTransaction.commit();
            setTitle("Pusat Bantuan");
        }

        else if(id == R.id.nav_logout){
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