package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailPesanActivity extends AppCompatActivity {

    TextView detail_isi_pesan;
    protected JSONParser jsonParser;
    protected int success;
    String id_pesan, tanggal, nama_anak, nama_orangtua;

    public void onStart() {
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }

    public DetailPesanActivity() {
        this.jsonParser = new JSONParser();
    }

    public class detail_pesan extends AsyncTask<String, Void, String> {
        String url_detail_pesan;

        public detail_pesan() {
            this.url_detail_pesan = "http://rekammedis.gudangtechno.web.id/android/detail_pesan.php?id_pesan=" + DetailPesanActivity.this.id_pesan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = DetailPesanActivity.this.jsonParser.getJSONFromUrl(url_detail_pesan);
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    Log.e("OK", " Updated");
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
                Log.e("OK", " Updated");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle b = getIntent().getExtras();
        id_pesan = b.getString("id_pesan");
        tanggal = b.getString("tanggal");
        nama_anak = b.getString("nama_anak");
        nama_orangtua = b.getString("nama_orangtua");

        this.detail_isi_pesan = (TextView) findViewById(R.id.isi_pesan);

        this.detail_isi_pesan.setText("Mohon Ibu "+nama_orangtua+" dari anak "+nama_anak+" segera melakukan imunisasi pada tanggal "+tanggal);
        new detail_pesan().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NotifikasiPesanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
