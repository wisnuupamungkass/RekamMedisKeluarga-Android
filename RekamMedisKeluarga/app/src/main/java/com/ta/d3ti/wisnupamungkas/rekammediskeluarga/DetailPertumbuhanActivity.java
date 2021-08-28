package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

public class DetailPertumbuhanActivity extends AppCompatActivity {

    EditText tanggal_pertumbuhan;
    EditText lingkar_kepala;
    EditText berat_badan;
    EditText tinggi_badan;
    EditText nama_petugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertumbuhan);
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

        this.tanggal_pertumbuhan = (EditText) findViewById(R.id.tanggal);
        this.lingkar_kepala = (EditText) findViewById(R.id.lingkar_kepala);
        this.berat_badan = (EditText) findViewById(R.id.berat_badan);
        this.tinggi_badan = (EditText) findViewById(R.id.tinggi_badan);
        this.nama_petugas = (EditText) findViewById(R.id.nama_petugas);

        Bundle b = getIntent().getExtras();
        String isi_tanggal = b.getString("tanggal");
        String isi_lingkar = b.getString("lingkar_kepala");
        String isi_berat = b.getString("berat_badan");
        String isi_tinggi = b.getString("tinggi_badan");
        String isi_nama = b.getString("nama_petugas");

        this.tanggal_pertumbuhan.setText(isi_tanggal);
        this.lingkar_kepala.setText(isi_lingkar);
        this.berat_badan.setText(isi_berat);
        this.tinggi_badan.setText(isi_tinggi);
        this.nama_petugas.setText(isi_nama);
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
