package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

public class DetailRekamMedisActivity extends AppCompatActivity {

    EditText tanggal_pemeriksaan;
    EditText keluhan;
    EditText riwayat_penyakit;
    EditText diagnosa;
    EditText tindakan_medis;
    EditText obat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rekam_medis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.tanggal_pemeriksaan = (EditText) findViewById(R.id.tanggal);
        this.keluhan = (EditText) findViewById(R.id.keluhan);
        this.riwayat_penyakit = (EditText) findViewById(R.id.riwayat_penyakit);
        this.diagnosa = (EditText) findViewById(R.id.diagnosa);
        this.tindakan_medis = (EditText) findViewById(R.id.tindakan_medis);
        this.obat = (EditText) findViewById(R.id.obat);

        Bundle b = getIntent().getExtras();
        String isi_tanggal = b.getString("tanggal");
        String isi_keluhan= b.getString("keluhan");
        String isi_penyakit= b.getString("riwayat_penyakit");
        String isi_diagnosa= b.getString("diagnosa");
        String isi_tindakan= b.getString("tindakan_medis");
        String isi_obat= b.getString("obat");

        this.tanggal_pemeriksaan.setText(isi_tanggal);
        this.keluhan.setText(isi_keluhan);
        this.riwayat_penyakit.setText(isi_penyakit);
        this.diagnosa.setText(isi_diagnosa);
        this.tindakan_medis.setText(isi_tindakan);
        this.obat.setText(isi_obat);
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
