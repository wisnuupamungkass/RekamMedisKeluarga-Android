package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PetugasLihatProfilOrangtuaActivity extends AppCompatActivity {
    protected static final String TAG_NIK = "no_ktp";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tgl_lahir";
    protected static final String TAG_ALAMAT = "alamat";
    protected static final String TAG_HP = "no_hp";
    protected static final String TAG_FOTO = "foto";
    protected static final String TAG_TOKEN = "no_token";

    protected JSONParser jsonParser;
    protected int success;

    protected TextView no_ktp;
    protected TextView nama;
    protected TextView tgl_lahir;
    protected TextView no_telp;
    protected TextView alamat;
    protected TextView token;

    protected ImageView foto_profil;
    protected String strktp;

    protected SessionManager session;
    protected ImageLoader imageLoader;

    public PetugasLihatProfilOrangtuaActivity(){
        this.jsonParser = new JSONParser();
    }

    public class profil_orangtua extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        String strnik;
        String strnama;
        String strtgl_lahir;
        String stralamat;
        String strno_hp;
        String strfoto;
        String strtoken;
        String url_profil_ortu;

        public profil_orangtua(){
            this.url_profil_ortu= "http://rekammedis.gudangtechno.web.id/android/petugas_profil_orangtua.php?no_ktp="+PetugasLihatProfilOrangtuaActivity.this.strktp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PetugasLihatProfilOrangtuaActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = PetugasLihatProfilOrangtuaActivity.this.jsonParser.getJSONFromUrl(url_profil_ortu);
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("orangtua");
                if (success == 1) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        this.strnik = c.getString(TAG_NIK).trim();
                        this.strnama = c.getString(TAG_NAMA).trim();
                        this.strtgl_lahir = c.getString(TAG_TANGGAL).trim();
                        this.stralamat = c.getString(TAG_ALAMAT).trim();
                        this.strno_hp = c.getString(TAG_HP).trim();
                        this.strfoto = c.getString(TAG_FOTO).trim();
                        this.strtoken = c.getString(TAG_TOKEN).trim();
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
            dialog.dismiss();
            if (success == 1) {
                if (this.strfoto == null) {
                    PetugasLihatProfilOrangtuaActivity.this.foto_profil.setImageBitmap(BitmapFactory.decodeResource(PetugasLihatProfilOrangtuaActivity.this.getResources(), R.drawable.perempuan));
                } else {
                    ImageLoader.getInstance().displayImage(strfoto, PetugasLihatProfilOrangtuaActivity.this.foto_profil);
                }
                PetugasLihatProfilOrangtuaActivity.this.no_ktp.setText(this.strnik);
                PetugasLihatProfilOrangtuaActivity.this.nama.setText(this.strnama);
                PetugasLihatProfilOrangtuaActivity.this.tgl_lahir.setText(this.strtgl_lahir);
                PetugasLihatProfilOrangtuaActivity.this.no_telp.setText(this.strno_hp);
                PetugasLihatProfilOrangtuaActivity.this.alamat.setText(this.stralamat);
                PetugasLihatProfilOrangtuaActivity.this.token.setText(this.strtoken);
            } else {
                Toast.makeText(PetugasLihatProfilOrangtuaActivity.this.getApplicationContext(), "Mohon Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onStart(){
        super.onStart();
        this.session = new SessionManager(getApplication());
        this.session.checkLoginToken();

        strktp = this.session.getTokenDetails().get(SessionManager.KEY_ID);
        Log.e("Error","ID User = "+strktp);
        new profil_orangtua().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_lihat_profil_orangtua);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getApplication()));

        PetugasLihatProfilOrangtuaActivity.this.no_ktp = (TextView) findViewById(R.id.ktp_user);
        PetugasLihatProfilOrangtuaActivity.this.nama = (TextView) findViewById(R.id.nama_user);
        PetugasLihatProfilOrangtuaActivity.this.tgl_lahir = (TextView) findViewById(R.id.tgl_user);
        PetugasLihatProfilOrangtuaActivity.this.no_telp = (TextView) findViewById(R.id.telepon_user);
        PetugasLihatProfilOrangtuaActivity.this.alamat = (TextView) findViewById(R.id.alamat_user);
        PetugasLihatProfilOrangtuaActivity.this.token = (TextView) findViewById(R.id.token_user);
        PetugasLihatProfilOrangtuaActivity.this.foto_profil = (ImageView) findViewById(R.id.foto_user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
