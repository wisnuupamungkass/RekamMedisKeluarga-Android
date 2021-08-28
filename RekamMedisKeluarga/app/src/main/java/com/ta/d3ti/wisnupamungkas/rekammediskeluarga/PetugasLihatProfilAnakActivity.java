package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PetugasLihatProfilAnakActivity extends AppCompatActivity {

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ANAK = "anak";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_NAMA = "nama_anak";
    protected static final String TAG_TANGGAL = "tanggal_lahir";
    protected static final String TAG_JAM = "jam_lahir";
    protected static final String TAG_BULAN = "bulan";
    protected static final String TAG_JK = "gender";
    protected static final String TAG_BERAT = "berat_badan";
    protected static final String TAG_PANJANG = "panjang_badan";
    protected static final String TAG_LINGKAR = "lingkar_kepala";
    protected static final String TAG_KTP = "nama_ortu";
    protected static final String TAG_FOTO = "foto";
    protected static final String TAG_TOKEN = "token_no";

    protected JSONParser jsonParser;
    protected int success;

    protected TextView no_register;
    protected TextView nama;
    protected TextView nama_ortu;
    protected TextView gender;
    protected TextView tanggal;
    protected TextView jam;
    protected TextView bulan;
    protected TextView berat;
    protected TextView panjang;
    protected TextView lingkar;
    protected TextView token;
    protected ImageView foto_profil;
    protected ImageLoader imageLoader;

    protected String strregister;

    public void onStart(){
        super.onStart();
        Bundle b = getIntent().getExtras();
        strregister = b.getString("no_register");
        Log.e("Error", "ID Anak=" + strregister);
        new profil_anak().execute();
    }

    public PetugasLihatProfilAnakActivity(){
        this.jsonParser = new JSONParser();
    }

    public class profil_anak extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        String strregister;
        String strnama;
        String strortu;
        String strgender;
        String strtgl_lahir;
        String strjam;
        String strbulan;
        String strberat;
        String strpanjang;
        String strlingkar;
        String strfoto;
        String strtoken;
        String url_profil_anak;

        public profil_anak(){
            this.url_profil_anak= "http://rekammedis.gudangtechno.web.id/android/petugas_profil_anak.php?no_register="+PetugasLihatProfilAnakActivity.this.strregister;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PetugasLihatProfilAnakActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = PetugasLihatProfilAnakActivity.this.jsonParser.getJSONFromUrl(url_profil_anak);
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray(TAG_ANAK);
                if (success == 1) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        this.strregister = c.getString(TAG_REGISTER).trim();
                        this.strnama = c.getString(TAG_NAMA).trim();
                        this.strortu = c.getString(TAG_KTP).trim();
                        this.strgender = c.getString(TAG_JK).trim();
                        this.strtgl_lahir = c.getString(TAG_TANGGAL).trim();
                        this.strjam = c.getString(TAG_JAM).trim();
                        this.strbulan = c.getString(TAG_BULAN).trim();
                        this.strberat = c.getString(TAG_BERAT).trim();
                        this.strpanjang = c.getString(TAG_PANJANG).trim();
                        this.strlingkar = c.getString(TAG_LINGKAR).trim();
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
                    PetugasLihatProfilAnakActivity.this.foto_profil.setImageBitmap(BitmapFactory.decodeResource(PetugasLihatProfilAnakActivity.this.getResources(), R.drawable.bayi));
                } else {
                    ImageLoader.getInstance().displayImage(strfoto, PetugasLihatProfilAnakActivity.this.foto_profil);
                }
                String jk_pria="Laki-Laki";
                String jk_perempuan="Perempuan";
                PetugasLihatProfilAnakActivity.this.no_register.setText(this.strregister);
                PetugasLihatProfilAnakActivity.this.nama.setText(this.strnama);
                PetugasLihatProfilAnakActivity.this.nama_ortu.setText(this.strortu);
                PetugasLihatProfilAnakActivity.this.tanggal.setText(this.strtgl_lahir);
                if(this.strgender.equalsIgnoreCase("L")){
                    PetugasLihatProfilAnakActivity.this.gender.setText(jk_pria);
                }else if(this.strgender.equalsIgnoreCase("P")){
                    PetugasLihatProfilAnakActivity.this.gender.setText(jk_perempuan);
                }
                PetugasLihatProfilAnakActivity.this.jam.setText(this.strjam);
                PetugasLihatProfilAnakActivity.this.bulan.setText(this.strbulan);
                PetugasLihatProfilAnakActivity.this.panjang.setText(this.strpanjang);
                PetugasLihatProfilAnakActivity.this.berat.setText(this.strberat);
                PetugasLihatProfilAnakActivity.this.lingkar.setText(this.strlingkar);
                PetugasLihatProfilAnakActivity.this.token.setText(this.strtoken);
            } else {
                Toast.makeText(PetugasLihatProfilAnakActivity.this.getApplicationContext(), "Mohon Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_lihat_profil_anak);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getApplication()));

        PetugasLihatProfilAnakActivity.this.no_register = (TextView) findViewById(R.id.no_register);
        PetugasLihatProfilAnakActivity.this.nama = (TextView) findViewById(R.id.nama_anak);
        PetugasLihatProfilAnakActivity.this.nama_ortu = (TextView) findViewById(R.id.nama_ibu);
        PetugasLihatProfilAnakActivity.this.gender = (TextView) findViewById(R.id.jk_anak);
        PetugasLihatProfilAnakActivity.this.tanggal = (TextView) findViewById(R.id.tgl_anak);
        PetugasLihatProfilAnakActivity.this.jam = (TextView) findViewById(R.id.jam_anak);
        PetugasLihatProfilAnakActivity.this.bulan = (TextView) findViewById(R.id.umur);
        PetugasLihatProfilAnakActivity.this.berat = (TextView) findViewById(R.id.berat_badan);
        PetugasLihatProfilAnakActivity.this.panjang = (TextView) findViewById(R.id.panjang);
        PetugasLihatProfilAnakActivity.this.lingkar = (TextView) findViewById(R.id.lingkar);
        PetugasLihatProfilAnakActivity.this.token = (TextView) findViewById(R.id.token_no);
        PetugasLihatProfilAnakActivity.this.foto_profil = (ImageView) findViewById(R.id.foto_anak);
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