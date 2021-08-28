package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

public class PetugasMenuAnakActivity extends AppCompatActivity {
    protected SessionManager session;
    protected String strregister;
    protected String strpetugas;

    ImageButton profil_anak;
    ImageButton pertumbuhan;
    ImageButton medis;
    ImageButton imunisasi;
    ImageButton kms;

    public void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_menu_anak);

        this.profil_anak =(ImageButton) findViewById(R.id.menu_profil_anak);
        this.pertumbuhan =(ImageButton) findViewById(R.id.menu_pertumbuhan);
        this.medis = (ImageButton) findViewById(R.id.menu_medis_anak);
        this.imunisasi = (ImageButton) findViewById(R.id.menu_imunisasi);
        this.kms = (ImageButton) findViewById(R.id.menu_kms);

        this.profil_anak.setOnClickListener(new menu_profil_anak());
        this.pertumbuhan.setOnClickListener(new menu_pertumbuhan());
        this.medis.setOnClickListener(new menu_medis());
        this.imunisasi.setOnClickListener(new menu_imunisasi());
        this.kms.setOnClickListener(new menu_kms());

        PetugasMenuAnakActivity.this.session = new SessionManager(getApplicationContext());
        session.isLoggedInToken();
        session.checkLoginToken();

        this.strregister = this.session.getTokenDetails().get(SessionManager.KEY_ID);
        Log.e("Error", "ID Anak=" + strregister);
        this.strpetugas = this.session.getPetugasDetails().get(SessionManager.KEY_PETUGAS);
        Log.e("Error", "Petugas=" + strpetugas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_anak, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PetugasMenuAnakActivity.this);
            builder.setMessage("Keluar Dari Menu Pemeriksaan?");
            builder.setCancelable(false);
            builder.setPositiveButton("YA", new keluar());
            builder.setNegativeButton("TIDAK", new batal());
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    private class menu_profil_anak implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuAnakActivity.this, PetugasLihatProfilAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", strregister);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_pertumbuhan implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuAnakActivity.this, PetugasListPertumbuhanActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", strregister);
            b.putString("id_petugas", strpetugas);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_medis implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuAnakActivity.this, PetugasListRekamMedisAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", strregister);
            b.putString("id_petugas", strpetugas);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_imunisasi implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuAnakActivity.this, PetugasListImunisasiActivity.class);
            Bundle b = new Bundle();
            b.putString("id_petugas", strpetugas);
            b.putString("no_register", strregister);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_kms implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuAnakActivity.this, GrafikAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("no_register", strregister);
            i.putExtras(b);
            startActivity(i);
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Keluar Dari Menu Pemeriksaan?");
        builder.setCancelable(false);
        builder.setPositiveButton("YA", new keluar());
        builder.setNegativeButton("TIDAK", new batal());
        builder.create().show();
    }

    class keluar implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            PetugasMenuAnakActivity.this.finish();
            Intent intent = new Intent(PetugasMenuAnakActivity.this, MainPetugasActivity.class);
            startActivity(intent);
        }
    }

    class batal implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }
}
