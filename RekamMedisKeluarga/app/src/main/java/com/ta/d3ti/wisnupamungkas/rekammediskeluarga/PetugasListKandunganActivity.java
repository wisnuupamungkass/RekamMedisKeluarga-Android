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

public class PetugasListKandunganActivity extends AppCompatActivity {
    Button tambah_kandungan;
    ListView list_kandungan;

    ArrayList<Kandungan> daftar_kandungan = new ArrayList<>();
    JSONArray daftarkandungan = null;
    protected JSONParser jsonParser;
    int success;

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ID = "id_kandungan";
    protected static final String TAG_KANDUNGAN = "kandungan_ke";
    protected static final String TAG_KTP = "no_ktp";

    protected String strktp,strpetugas;

    public PetugasListKandunganActivity() {
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_list_kandungan);
        tambah_kandungan = (Button) findViewById(R.id.button_tambah_kandungan);
        list_kandungan = (ListView) findViewById(R.id.listview_kandungan);

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
        strktp = b.getString("no_ktp");
        Log.e("Error", "No KTP = " + strktp);
        strpetugas = b.getString("id_petugas");
        Log.e("Error", "Petugas= " + strpetugas);

        ReadKandunganTask m = (ReadKandunganTask) new ReadKandunganTask().execute();

        list_kandungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_kandungan = ((TextView) view.findViewById(R.id.id_kandungan)).getText().toString();
                String kandungan_ke = ((TextView) view.findViewById(R.id.kandungan_ke)).getText().toString();

                Intent i = new Intent(PetugasListKandunganActivity.this, PetugasDetailKandunganActivity.class);
                Bundle b = new Bundle();
                b.putString("id_kandungan", id_kandungan);
                b.putString("kandungan_ke", kandungan_ke);
                b.putString("no_ktp", strktp);
                b.putString("id_petugas", strpetugas);
                i.putExtras(b);
                startActivity(i);
            }
        });

        tambah_kandungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kandungan = new Intent(PetugasListKandunganActivity.this, PetugasTambahKandunganActivity.class);
                Bundle b = new Bundle();
                b.putString("no_ktp", strktp);
                kandungan.putExtras(b);
                startActivity(kandungan);
            }
        });
    }

    class ReadKandunganTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String url_tampil_kandungan;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PetugasListKandunganActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            url_tampil_kandungan = "http://rekammedis.gudangtechno.web.id/android/tampil_kandungan.php?no_ktp=" + PetugasListKandunganActivity.this.strktp;

            Kandungan tempKandungan = new Kandungan();
            try {
                JSONObject json = PetugasListKandunganActivity.this.jsonParser.getJSONFromUrl(url_tampil_kandungan);

                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftarkandungan = json.getJSONArray("kandungan");
                    for (int i = 0; i < daftarkandungan.length(); i++) {
                        JSONObject c = daftarkandungan.getJSONObject(i);
                        tempKandungan = new Kandungan();
                        tempKandungan.setId_kandungan(c.getString(TAG_ID));
                        tempKandungan.setKandungan_ke(c.getString(TAG_KANDUNGAN));
                        tempKandungan.setNo_ktp(c.getString(TAG_KTP));
                        daftar_kandungan.add(tempKandungan);
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
                Toast.makeText(PetugasListKandunganActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(PetugasListKandunganActivity.this, "Data Kandungan Masih Kosong", Toast.LENGTH_LONG).show();
            }
            if (success == 1) {
                list_kandungan.setAdapter(new KandunganAdapter(PetugasListKandunganActivity.this, PetugasListKandunganActivity.this.daftar_kandungan));
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
