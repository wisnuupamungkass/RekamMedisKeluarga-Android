package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

public class DetailImunisasiActivity extends AppCompatActivity {
    EditText tanggal;
    EditText nama_imunisasi;
    EditText nama_petugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_imunisasi);
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

        this.tanggal = (EditText) findViewById(R.id.tanggal);
        this.nama_imunisasi = (EditText) findViewById(R.id.jenis_imunisasi);
        this.nama_petugas = (EditText) findViewById(R.id.nama_petugas);

        Bundle b = getIntent().getExtras();
        String isi_tanggal = b.getString("tanggal");
        String isi_imunisasi = b.getString("nama_imunisasi");
        String isi_petugas = b.getString("nama_petugas");

        this.tanggal.setText(isi_tanggal);
        this.nama_imunisasi.setText(isi_imunisasi);
        this.nama_petugas.setText(isi_petugas);
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
