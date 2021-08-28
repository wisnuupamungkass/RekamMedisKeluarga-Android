package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainPerempuanActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected JSONParser jsonParser;
    protected int success;
    protected TextView email_drawer;
    protected ImageView foto_profil_perempuan;
    protected String stremail;
    protected String regId;
    //protected String strktp;

    private static final String TAG = MainPerempuanActivity.class.getSimpleName();
    //String url_update_firebase= "http://rekammediskeluarga.hol.es/android/update_firebase.php";

    //protected static final String TAG_FIREBASE = "id_firebase";
    //protected static final String TAG_KTP = "no_ktp";

    protected ImageLoader imageLoader;
    SessionManager session;

    public void onStart(){
        super.onStart();
        this.session = new SessionManager(getApplicationContext());
        session.isLoggedInUser();
        this.session.checkLoginPerempuan();

        this.stremail = this.session.getUserDetails().get(SessionManager.KEY_EMAIL);
        /*this.strktp = this.session.getUserDetails().get(SessionManager.KEY_KTP);
        Log.e("Error", "KTP : "+strktp);

        if(strktp!=null){
            new update_idfirebase().execute();
        }*/

        if(stremail!=null) {
            new drawer_perempuan().execute();
        }
    }

    public MainPerempuanActivity(){
        this.jsonParser = new JSONParser();
    }

    public class drawer_perempuan extends AsyncTask<String,Void,String> {
        String strfoto;
        String url_drawer_perempuan;

        public drawer_perempuan(){
            this.url_drawer_perempuan= "http://rekammedis.gudangtechno.web.id/android/profil_orangtua.php?email="+MainPerempuanActivity.this.stremail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = MainPerempuanActivity.this.jsonParser.getJSONFromUrl(url_drawer_perempuan);
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
                    MainPerempuanActivity.this.foto_profil_perempuan.setImageBitmap(BitmapFactory.decodeResource(MainPerempuanActivity.this.getResources(), R.drawable.perempuan));
                } else {
                    ImageLoader.getInstance().displayImage(this.strfoto, MainPerempuanActivity.this.foto_profil_perempuan);
                }
                MainPerempuanActivity.this.email_drawer.setText(stremail);
            }
        }
    }

    class keluar implements OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            MainPerempuanActivity.this.finish();
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
        setContentView(R.layout.activity_main_perempuan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.foto_profil_perempuan = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.foto_drawer_perempuan);
        this.email_drawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_drawer);

        FragmentTransaction fragmentTransaction;
        HomePerempuanFragment fragment = new HomePerempuanFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("Home Perempuan");
    }

    /*public class update_idfirebase extends AsyncTask<String, String, String> {
        int success;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            return getIdFirebaseList();
        }

        public String getIdFirebaseList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(MainPerempuanActivity.TAG_FIREBASE, regId));
            params.add(new BasicNameValuePair(MainPerempuanActivity.TAG_KTP, strktp));

            JSONObject json = jsonParser.makeHttpRequest(url_update_firebase, "POST", params);

            if (json == null) {
                return "Error Converting";
            }
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (success == 1) {
                Toast.makeText(MainPerempuanActivity.this.getApplicationContext(), "ID Firebase Updated...", Toast.LENGTH_SHORT);
            } else if (success == 0) {
                Toast.makeText(MainPerempuanActivity.this.getApplicationContext(), "Gagal Registrasi ID Firebase...", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            /*Builder builder = new Builder(this);
            builder.setMessage("Are you sure to exit ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new keluar());
            builder.setNegativeButton("No", new batal());
            builder.create().show();*/
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

        if (id == R.id.nav_home_perempuan) {
            HomePerempuanFragment fragment1 = new HomePerempuanFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment1);
            fragmentTransaction.commit();
            setTitle("Home Perempuan");
        }

        else if (id == R.id.nav_profil_perempuan) {
            ProfilPerempuanFragment fragment2 = new ProfilPerempuanFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment2);
            fragmentTransaction.commit();
            setTitle("Profil Pengguna");
        }

        else if (id == R.id.nav_bantuan_perempuan) {
            BantuanFragment fragment3 = new BantuanFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment3);
            fragmentTransaction.commit();
            setTitle("Pusat Bantuan");
        }

        else if (id == R.id.nav_anak) {
            HomeAnakFragment fragment4 = new HomeAnakFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment4);
            fragmentTransaction.commit();
            setTitle("Home Daftar Anak");
        }

        else if (id == R.id.nav_kandungan){
            Intent intent = new Intent(MainPerempuanActivity.this, ListKandunganActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.nav_rekammedis_perempuan){
            MainPerempuanActivity.this.startActivity(new Intent(MainPerempuanActivity.this, ListRekamMedisPerempuanActivity.class));
        }

        else if(id == R.id.nav_notifikasi){
            MainPerempuanActivity.this.startActivity(new Intent(MainPerempuanActivity.this, NotifikasiPesanActivity.class));
        }

        else if(id == R.id.nav_logout_perempuan){
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