package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfilPetugasFragment extends Fragment {
    protected static final String TAG_NIK = "id_petugas";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tgl_lahir";
    protected static final String TAG_JK = "jenis_kelamin";
    protected static final String TAG_HP = "no_hp";
    protected static final String TAG_ALAMAT = "alamat";
    protected static final String TAG_KLINIK = "nama_klinik";
    protected static final String TAG_IZIN = "izin_praktek";

    protected JSONParser jsonParser;
    protected int success;

    protected TextView no_ktp;
    protected TextView nama;
    protected TextView jk;
    protected TextView tgl_lahir;
    protected TextView no_telp;
    protected TextView alamat;
    protected TextView nama_klinik;
    protected TextView no_klinik;
    protected Button update_profil;
    protected Button update_foto;
    protected ImageView foto_profil;
    protected String stremail;

    protected SessionManager session;
    protected ImageLoader imageLoader;

    public ProfilPetugasFragment(){
        this.jsonParser = new JSONParser();
    }

    public class Profile_petugas extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        String strnik;
        String strnama;
        String strtgl_lahir;
        String strjenis_kelamin;
        String stralamat;
        String strno_hp;
        String strklinik;
        String strizin_praktek;
        String strfoto;
        String url_profil_petugas;

        public Profile_petugas(){
            this.url_profil_petugas= "http://rekammedis.gudangtechno.web.id/android/profil_petugas.php?email="+ProfilPetugasFragment.this.stremail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProfilPetugasFragment.this.getActivity());
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = ProfilPetugasFragment.this.jsonParser.getJSONFromUrl(url_profil_petugas);
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("petugas");
                if (success == 1) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        this.strnik = c.getString(TAG_NIK).trim();
                        this.strnama = c.getString(TAG_NAMA).trim();
                        this.strjenis_kelamin = c.getString(TAG_JK).trim();
                        this.strtgl_lahir = c.getString(TAG_TANGGAL).trim();
                        this.stralamat = c.getString(TAG_ALAMAT).trim();
                        this.strno_hp = c.getString(TAG_HP).trim();
                        this.strklinik = c.getString(TAG_KLINIK).trim();
                        this.strizin_praktek = c.getString(TAG_IZIN).trim();
                        this.strfoto = c.getString("foto").trim();
                        Log.e("OK", " Ambil Data");
                    }
                }
            } catch (JSONException e) {
                Log.e("Error", " Tidak Bisa Ambil Data");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (success == 1) {
                Log.e("Error", strfoto);
                if (this.strfoto == null) {
                    ProfilPetugasFragment.this.foto_profil.setImageBitmap(BitmapFactory.decodeResource(ProfilPetugasFragment.this.getResources(), R.drawable.doctor));
                } else {
                    ImageLoader.getInstance().displayImage(strfoto, ProfilPetugasFragment.this.foto_profil);
                }
                String jk_laki="Laki-Laki";
                String jk_perempuan="Perempuan";
                ProfilPetugasFragment.this.no_ktp.setText(this.strnik);
                ProfilPetugasFragment.this.nama.setText(this.strnama);
                if(strjenis_kelamin.equalsIgnoreCase("L")){
                    ProfilPetugasFragment.this.jk.setText(jk_laki);
                }else if(strjenis_kelamin.equalsIgnoreCase("P")){
                    ProfilPetugasFragment.this.jk.setText(jk_perempuan);
                }
                ProfilPetugasFragment.this.tgl_lahir.setText(this.strtgl_lahir);
                ProfilPetugasFragment.this.no_telp.setText(this.strno_hp);
                ProfilPetugasFragment.this.alamat.setText(this.stralamat);
                if((strklinik.equalsIgnoreCase("NULL") && strizin_praktek.equalsIgnoreCase("NULL"))){
                    ProfilPetugasFragment.this.nama_klinik.setText("Belum Divalidasi");
                    ProfilPetugasFragment.this.no_klinik.setText("Belum Divalidasi");
                } else {
                    ProfilPetugasFragment.this.nama_klinik.setText(strklinik);
                    ProfilPetugasFragment.this.no_klinik.setText(strizin_praktek);
                }
            } else {
                Toast.makeText(ProfilPetugasFragment.this.getActivity().getApplicationContext(), "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onStart(){
        super.onStart();
        this.session = new SessionManager(getActivity());
        this.session.checkLoginPetugas();

        stremail = this.session.getPetugasDetails().get(SessionManager.KEY_EMAIL);
        new Profile_petugas().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_profil_petugas, container, false);
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        ProfilPetugasFragment.this.no_ktp = (TextView) myView.findViewById(R.id.ktp_petugas);
        ProfilPetugasFragment.this.nama = (TextView) myView.findViewById(R.id.nama_petugas);
        ProfilPetugasFragment.this.jk = (TextView) myView.findViewById(R.id.jk_petugas);
        ProfilPetugasFragment.this.tgl_lahir = (TextView) myView.findViewById(R.id.tgl_petugas);
        ProfilPetugasFragment.this.no_telp = (TextView) myView.findViewById(R.id.telepon_petugas);
        ProfilPetugasFragment.this.alamat = (TextView) myView.findViewById(R.id.alamat_petugas);
        ProfilPetugasFragment.this.nama_klinik = (TextView) myView.findViewById(R.id.klinik);
        ProfilPetugasFragment.this.no_klinik = (TextView) myView.findViewById(R.id.no_izin);
        ProfilPetugasFragment.this.foto_profil = (ImageView) myView.findViewById(R.id.foto_petugas);
        ProfilPetugasFragment.this.update_profil = (Button) myView.findViewById(R.id.btn_profil_petugas);
        ProfilPetugasFragment.this.update_foto = (Button) myView.findViewById(R.id.btn_foto_petugas);

        this.update_profil.setOnClickListener(new profil_update());
        this.update_foto.setOnClickListener(new foto_update());
        return myView;
    }

    class profil_update implements OnClickListener {
        profil_update() {
        }

        public void onClick(View v) {
            String id_petugas = ProfilPetugasFragment.this.no_ktp.getText().toString();
            String name = ProfilPetugasFragment.this.nama.getText().toString();
            String jekel = ProfilPetugasFragment.this.jk.getText().toString();
            String tanggal = ProfilPetugasFragment.this.tgl_lahir.getText().toString();
            String hp = ProfilPetugasFragment.this.no_telp.getText().toString();
            String rumah = ProfilPetugasFragment.this.alamat.getText().toString();
            String klinik = ProfilPetugasFragment.this.nama_klinik.getText().toString();
            String izin = ProfilPetugasFragment.this.no_klinik.getText().toString();
            Intent i = new Intent(ProfilPetugasFragment.this.getActivity(), UpdateProfilPetugasActivity.class);
            Bundle b = new Bundle();
            b.putString("id_petugas", id_petugas);
            b.putString("nama", name);
            b.putString("tanggal", tanggal);
            b.putString("jenis_kelamin", jekel);
            b.putString("no_hp", hp);
            b.putString("alamat", rumah);
            b.putString("nama_klinik", klinik);
            b.putString("no_izin", izin);
            b.putString("email", stremail);
            i.putExtras(b);
            ProfilPetugasFragment.this.startActivity(i);
        }
    }

    class foto_update implements OnClickListener {
        foto_update() {
        }

        public void onClick(View v) {
            String id_petugas = ProfilPetugasFragment.this.no_ktp.getText().toString();
            Intent i = new Intent(ProfilPetugasFragment.this.getActivity(), UpdateFotoPetugasActivity.class);
            Bundle b = new Bundle();
            b.putString("id_petugas", id_petugas);
            i.putExtras(b);
            ProfilPetugasFragment.this.startActivity(i);
        }
    }
}