package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MenuAnakActivity extends AppCompatActivity {

    ImageButton profil_anak;
    ImageButton pertumbuhan;
    ImageButton medis;
    ImageButton imunisasi;
    ImageButton foto;
    ImageButton kms;

    String isi_no_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_anak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.profil_anak =(ImageButton) findViewById(R.id.menu_profil_anak);
        this.pertumbuhan =(ImageButton) findViewById(R.id.menu_pertumbuhan);
        this.medis = (ImageButton) findViewById(R.id.menu_medis_anak);
        this.imunisasi = (ImageButton) findViewById(R.id.menu_imunisasi);
        this.foto = (ImageButton) findViewById(R.id.menu_gallery);
        this.kms = (ImageButton) findViewById(R.id.menu_kms);

        this.profil_anak.setOnClickListener(new menu_profil_anak());
        this.pertumbuhan.setOnClickListener(new menu_pertumbuhan());
        this.medis.setOnClickListener(new menu_medis());
        this.imunisasi.setOnClickListener(new menu_imunisasi());
        this.foto.setOnClickListener(new menu_gallery());
        this.kms.setOnClickListener(new menu_kms());

        Bundle b = getIntent().getExtras();
        isi_no_register = b.getString("no_register");

    }

    private class menu_profil_anak implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(MenuAnakActivity.this, ProfilAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", isi_no_register);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_pertumbuhan implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(MenuAnakActivity.this, ListPertumbuhanActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", isi_no_register);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_medis implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(MenuAnakActivity.this, ListRekamMedisAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", isi_no_register);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_imunisasi implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(MenuAnakActivity.this, ListImunisasiActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", isi_no_register);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_gallery implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(MenuAnakActivity.this, GalleryAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", isi_no_register);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_kms implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(MenuAnakActivity.this, GrafikAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", isi_no_register);
            i.putExtras(b);
            startActivity(i);
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
