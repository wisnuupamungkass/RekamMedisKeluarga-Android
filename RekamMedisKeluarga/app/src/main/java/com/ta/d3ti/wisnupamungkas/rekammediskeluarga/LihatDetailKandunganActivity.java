package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

public class LihatDetailKandunganActivity extends AppCompatActivity {
    protected EditText tanggal_periksa;
    protected EditText bulan_hamil;
    protected EditText kondisi_janin;
    protected String isi_id_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_detail_kandungan);
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

        this.tanggal_periksa = (EditText) findViewById(R.id.tgl_periksa);
        this.bulan_hamil = (EditText) findViewById(R.id.bln_hamil);
        this.kondisi_janin = (EditText) findViewById(R.id.kondisi);

        Bundle b = getIntent().getExtras();
        isi_id_detail = b.getString("id_detail");
        String isi_tanggal = b.getString("tanggal_periksa");
        String isi_bulan = b.getString("bulan_hamil");
        String isi_kondisi = b.getString("kondisi_janin");

        this.tanggal_periksa.setText(isi_tanggal);
        this.bulan_hamil.setText(isi_bulan);
        this.kondisi_janin.setText(isi_kondisi);
    }

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

