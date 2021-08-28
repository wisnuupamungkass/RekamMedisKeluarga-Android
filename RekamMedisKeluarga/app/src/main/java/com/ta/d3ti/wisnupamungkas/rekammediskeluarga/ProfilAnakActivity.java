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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfilAnakActivity extends AppCompatActivity {

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ANAK = "anak";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tanggal_lahir";
    protected static final String TAG_JAM = "jam_lahir";
    protected static final String TAG_BULAN = "bulan";
    protected static final String TAG_JK = "gender";
    protected static final String TAG_BERAT = "berat_badan";
    protected static final String TAG_PANJANG = "panjang_badan";
    protected static final String TAG_LINGKAR = "lingkar_kepala";
    protected static final String TAG_TOKEN = "token_no";

    protected static final String TAG_KTP = "ktp_no";
    protected static final String TAG_FOTO = "foto";

    protected JSONParser jsonParser;
    protected int success;

    protected TextView no_register;
    protected TextView nama;
    protected TextView gender;
    protected TextView tanggal;
    protected TextView jam;
    protected TextView bulan;
    protected TextView berat;
    protected TextView panjang;
    protected TextView lingkar;
    protected TextView token_no;
    protected Button update_profil;
    protected Button update_foto;
    protected Button QRcode;
    protected ImageView foto_profil;
    protected ImageLoader imageLoader;

    protected String strregister;

    public void onStart(){
        super.onStart();
        Bundle b = getIntent().getExtras();
        strregister = b.getString("no_register");
        new profil_anak().execute();
    }

    public ProfilAnakActivity(){
        this.jsonParser = new JSONParser();
    }

    public class profil_anak extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        String strregister;
        String strnama;
        String strgender;
        String strtgl_lahir;
        String strjam;
        String strbulan;
        String strberat;
        String strpanjang;
        String strlingkar;
        String strfoto;
        String strtokenno;
        String url_profil_anak;

        public profil_anak(){
            this.url_profil_anak= "http://rekammedis.gudangtechno.web.id/android/profil_anak.php?no_register="+ProfilAnakActivity.this.strregister;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProfilAnakActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = ProfilAnakActivity.this.jsonParser.getJSONFromUrl(url_profil_anak);
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray(TAG_ANAK);
                if (success == 1) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        this.strregister = c.getString(TAG_REGISTER).trim();
                        this.strnama = c.getString(TAG_NAMA).trim();
                        this.strgender = c.getString(TAG_JK).trim();
                        this.strtgl_lahir = c.getString(TAG_TANGGAL).trim();
                        this.strjam = c.getString(TAG_JAM).trim();
                        this.strbulan = c.getString(TAG_BULAN).trim();
                        this.strberat = c.getString(TAG_BERAT).trim();
                        this.strpanjang = c.getString(TAG_PANJANG).trim();
                        this.strlingkar = c.getString(TAG_LINGKAR).trim();
                        this.strtokenno = c.getString(TAG_TOKEN).trim();
                        this.strfoto = c.getString(TAG_FOTO).trim();
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
                    ProfilAnakActivity.this.foto_profil.setImageBitmap(BitmapFactory.decodeResource(ProfilAnakActivity.this.getResources(), R.drawable.bayi));
                } else {
                    ImageLoader.getInstance().displayImage(strfoto, ProfilAnakActivity.this.foto_profil);
                }
                String jk_pria="Laki-Laki";
                String jk_perempuan="Perempuan";
                ProfilAnakActivity.this.no_register.setText(this.strregister);
                ProfilAnakActivity.this.nama.setText(this.strnama);
                ProfilAnakActivity.this.tanggal.setText(this.strtgl_lahir);
                if(this.strgender.equalsIgnoreCase("L")){
                    ProfilAnakActivity.this.gender.setText(jk_pria);
                }else if(this.strgender.equalsIgnoreCase("P")){
                    ProfilAnakActivity.this.gender.setText(jk_perempuan);
                }
                ProfilAnakActivity.this.jam.setText(this.strjam);
                ProfilAnakActivity.this.bulan.setText(this.strbulan);
                ProfilAnakActivity.this.panjang.setText(this.strpanjang);
                ProfilAnakActivity.this.berat.setText(this.strberat);
                ProfilAnakActivity.this.lingkar.setText(this.strlingkar);
                ProfilAnakActivity.this.token_no.setText(this.strtokenno);
            } else {
                Toast.makeText(ProfilAnakActivity.this.getApplicationContext(), "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_anak);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getApplication()));

        ProfilAnakActivity.this.no_register = (TextView) findViewById(R.id.no_register);
        ProfilAnakActivity.this.nama = (TextView) findViewById(R.id.nama_anak);
        ProfilAnakActivity.this.gender = (TextView) findViewById(R.id.jk_anak);
        ProfilAnakActivity.this.tanggal = (TextView) findViewById(R.id.tgl_anak);
        ProfilAnakActivity.this.jam = (TextView) findViewById(R.id.jam_anak);
        ProfilAnakActivity.this.bulan = (TextView) findViewById(R.id.umur);
        ProfilAnakActivity.this.berat = (TextView) findViewById(R.id.berat_badan);
        ProfilAnakActivity.this.panjang = (TextView) findViewById(R.id.panjang);
        ProfilAnakActivity.this.lingkar = (TextView) findViewById(R.id.lingkar);
        ProfilAnakActivity.this.token_no = (TextView) findViewById(R.id.token_no);
        ProfilAnakActivity.this.update_profil = (Button) findViewById(R.id.btn_profil_anak);
        ProfilAnakActivity.this.update_foto = (Button) findViewById(R.id.btn_foto_anak);
        ProfilAnakActivity.this.foto_profil = (ImageView) findViewById(R.id.foto_anak);
        ProfilAnakActivity.this.QRcode = (Button) findViewById(R.id.button_qrcode);


        this.update_profil.setOnClickListener(new profil_update());
        this.update_foto.setOnClickListener(new foto_update());
        this.QRcode.setOnClickListener(new lihat_barcode());
    }

    class profil_update implements View.OnClickListener {
        public void onClick(View v) {
            String no_register = ProfilAnakActivity.this.no_register.getText().toString();
            String name = ProfilAnakActivity.this.nama.getText().toString();
            String tanggal = ProfilAnakActivity.this.tanggal.getText().toString();
            String jam = ProfilAnakActivity.this.jam.getText().toString();
            String berat = ProfilAnakActivity.this.berat.getText().toString();
            String panjang = ProfilAnakActivity.this.panjang.getText().toString();
            String lingkar = ProfilAnakActivity.this.lingkar.getText().toString();
            String token_no = ProfilAnakActivity.this.token_no.getText().toString();
            Intent i = new Intent(ProfilAnakActivity.this, UpdateProfilAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", no_register);
            b.putString("nama", name);
            b.putString("tanggal", tanggal);
            b.putString("jam", jam);
            b.putString("berat", berat);
            b.putString("panjang", panjang);
            b.putString("lingkar", lingkar);
            b.putString("token_no", token_no);
            i.putExtras(b);
            ProfilAnakActivity.this.startActivity(i);
        }
    }

    class foto_update implements View.OnClickListener {
        public void onClick(View v) {
            String no_register = ProfilAnakActivity.this.no_register.getText().toString();
            Intent i = new Intent(ProfilAnakActivity.this, UpdateFotoAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", no_register);
            i.putExtras(b);
            ProfilAnakActivity.this.startActivity(i);
        }
    }

    class lihat_barcode implements View.OnClickListener {
        public void onClick(View v) {
            String name = ProfilAnakActivity.this.nama.getText().toString();
            String token = ProfilAnakActivity.this.token_no.getText().toString();
            Intent i = new Intent(ProfilAnakActivity.this, LihatQRCodeActivity.class);
            Bundle b = new Bundle();
            b.putString("nama", name);
            b.putString("no_token", token);
            i.putExtras(b);
            ProfilAnakActivity.this.startActivity(i);
        }
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