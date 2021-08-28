package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class PetugasMenuPerempuanActivity extends AppCompatActivity {
    protected SessionManager session;
    protected String strktp;
    protected String strpetugas;

    ImageButton profil_user;
    ImageButton medis;
    ImageButton kandungan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_menu_perempuan);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_perempuan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetugasMenuPerempuanActivity.this);
                builder.setMessage("Keluar Dari Menu Pemeriksaan?");
                builder.setCancelable(false);
                builder.setPositiveButton("YA", new keluar());
                builder.setNegativeButton("TIDAK", new batal());
                builder.create().show();
            }
        });

        this.profil_user =(ImageButton) findViewById(R.id.profil_perempuan);
        this.medis =(ImageButton) findViewById(R.id.menu_rekam_medis);
        this.kandungan =(ImageButton) findViewById(R.id.menu_kandungan);

        this.profil_user.setOnClickListener(new menu_profil_user());
        this.medis.setOnClickListener(new menu_medis());
        this.kandungan.setOnClickListener(new menu_kandungan());

        PetugasMenuPerempuanActivity.this.session = new SessionManager(getApplicationContext());
        session.isLoggedInToken();
        session.checkLoginToken();

        this.strktp = this.session.getTokenDetails().get(SessionManager.KEY_ID);
        Log.e("Error", "ID Ortu = "+ strktp);
        this.strpetugas = this.session.getPetugasDetails().get(SessionManager.KEY_PETUGAS);
        Log.e("Error", "Petugas = "+ strpetugas);
    }

    private class menu_profil_user implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuPerempuanActivity.this, PetugasLihatProfilOrangtuaActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", strktp);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_medis implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuPerempuanActivity.this, PetugasListRekamMedisOrangtuaActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", strktp);
            b.putString("id_petugas", strpetugas);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_kandungan implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuPerempuanActivity.this, PetugasListKandunganActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", strktp);
            b.putString("id_petugas", strpetugas);
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
            PetugasMenuPerempuanActivity.this.finish();
            Intent intent = new Intent(PetugasMenuPerempuanActivity.this, MainPetugasActivity.class);
            startActivity(intent);
        }
    }

    class batal implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }
}
