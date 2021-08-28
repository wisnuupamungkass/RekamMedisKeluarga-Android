package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailKandunganActivity extends AppCompatActivity {
    Button lihat_usg;
    ListView list_detail_kandungan;

    ArrayList<DetailKandungan> daftar_detail_kandungan = new ArrayList<>();
    JSONArray daftardetailkandungan = null;
    SessionManager session;
    protected JSONParser jsonParser;
    int success;

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_DETAIL = "id_detail";
    protected static final String TAG_TANGGAL = "tanggal_periksa";
    protected static final String TAG_BULAN = "bulan_hamil";
    protected static final String TAG_KONDISI = "kondisi_janin";
    protected static final String TAG_KANDUNGAN = "kandungan_id";
    protected static final String TAG_PETUGAS = "petugas_id";

    protected String stridkandungan,strpetugas;

    public DetailKandunganActivity() {
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kandungan);
        this.list_detail_kandungan = (ListView) findViewById(R.id.listview_detail_kandungan);
        this.lihat_usg = (Button) findViewById(R.id.button_lihat_foto_usg);
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
        stridkandungan = b.getString("id_kandungan");
        Log.e("Error", "ID Kandungan = " + stridkandungan);
        strpetugas =  b.getString("id_petugas");
        Log.e("Error", "ID Petugas = " + strpetugas);

        ReadDetailKandunganTask m = (ReadDetailKandunganTask) new ReadDetailKandunganTask().execute();

        list_detail_kandungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_detail = ((TextView) view.findViewById(R.id.id_detail)).getText().toString();
                String tanggal_periksa = ((TextView) view.findViewById(R.id.tanggal_periksa)).getText().toString();
                String bulan_hamil = ((TextView) view.findViewById(R.id.bulan_hamil)).getText().toString();
                String kondisi_janin = ((TextView) view.findViewById(R.id.kondisi_janin)).getText().toString();

                Intent i = new Intent(DetailKandunganActivity.this, LihatDetailKandunganActivity.class);
                Bundle b = new Bundle();
                b.putString("id_detail", id_detail);
                b.putString("tanggal_periksa", tanggal_periksa);
                b.putString("bulan_hamil", bulan_hamil);
                b.putString("kondisi_janin", kondisi_janin);
                b.putString("kandungan_id", stridkandungan);
                b.putString("petugas_id", strpetugas);
                i.putExtras(b);
                startActivity(i);
            }
        });

        lihat_usg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kandungan = new Intent(DetailKandunganActivity.this, PetugasLihatUsgActivity.class);
                Bundle b = new Bundle();
                b.putString("kandungan_id", stridkandungan);
                b.putString("petugas_id", strpetugas);
                kandungan.putExtras(b);
                startActivity(kandungan);
            }
        });
    }

    class ReadDetailKandunganTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String url_tampil_detail_kandungan;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailKandunganActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... sText) {
            url_tampil_detail_kandungan = "http://rekammedis.gudangtechno.web.id/android/tampil_detail_kandungan.php?kandungan_id="+DetailKandunganActivity.this.stridkandungan;

            DetailKandungan tempDetailKandungan = new DetailKandungan();
            try {
                JSONObject json = DetailKandunganActivity.this.jsonParser.getJSONFromUrl(url_tampil_detail_kandungan);

                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftardetailkandungan = json.getJSONArray("detailkandungan");
                    for (int i = 0; i < daftardetailkandungan.length(); i++) {
                        JSONObject c = daftardetailkandungan.getJSONObject(i);
                        tempDetailKandungan = new DetailKandungan();
                        tempDetailKandungan.setId_detail(c.getString(TAG_DETAIL));
                        tempDetailKandungan.setTanggal_periksa(c.getString(TAG_TANGGAL));
                        tempDetailKandungan.setBulan_hamil(c.getString(TAG_BULAN));
                        tempDetailKandungan.setKondisi_janin(c.getString(TAG_KONDISI));
                        tempDetailKandungan.setKandungan_id(c.getString(TAG_KANDUNGAN));
                        tempDetailKandungan.setPetugas_id(c.getString(TAG_PETUGAS));
                        daftar_detail_kandungan.add(tempDetailKandungan);
                    }
                    return "OK";
                } else {
                    return "no results";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(DetailKandunganActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(DetailKandunganActivity.this, "Data Detail Kandungan Masih Kosong", Toast.LENGTH_LONG).show();
            }
            if (success == 1) {
                list_detail_kandungan.setAdapter(new DetailKandunganAdapter(DetailKandunganActivity.this, DetailKandunganActivity.this.daftar_detail_kandungan));
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}