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

public class PetugasMenuPriaActivity extends AppCompatActivity {
    protected SessionManager session;
    protected String strktp;
    protected String strpetugas;

    ImageButton profil_user;
    ImageButton medis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_menu_pria);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_perempuan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetugasMenuPriaActivity.this);
                builder.setMessage("Keluar Dari Menu Pemeriksaan?");
                builder.setCancelable(false);
                builder.setPositiveButton("YA", new keluar());
                builder.setNegativeButton("TIDAK", new batal());
                builder.create().show();
            }
        });
        this.profil_user =(ImageButton) findViewById(R.id.profil_pria);
        this.medis =(ImageButton) findViewById(R.id.menu_rekam_medis);

        this.profil_user.setOnClickListener(new menu_profil_user());
        this.medis.setOnClickListener(new menu_medis());

        PetugasMenuPriaActivity.this.session = new SessionManager(getApplicationContext());
        session.isLoggedInToken();
        session.checkLoginToken();

        this.strktp = this.session.getTokenDetails().get(SessionManager.KEY_ID);
        Log.e("Error", "ID Ortu = "+ strktp);
        this.strpetugas = this.session.getPetugasDetails().get(SessionManager.KEY_PETUGAS);
        Log.e("Error", "Petugas = "+ strpetugas);
    }

    private class menu_profil_user implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuPriaActivity.this, PetugasLihatProfilOrangtuaActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", strktp);
            i.putExtras(b);
            startActivity(i);
        }
    }

    private class menu_medis implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(PetugasMenuPriaActivity.this, PetugasListRekamMedisOrangtua2Activity.class);
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
            PetugasMenuPriaActivity.this.finish();
            Intent intent = new Intent(PetugasMenuPriaActivity.this, MainPetugasActivity.class);
            startActivity(intent);
        }
    }

    class batal implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }
}
