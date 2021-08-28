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

public class ProfilPerempuanFragment extends Fragment {
    protected static final String TAG_NIK = "no_ktp";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tgl_lahir";
    protected static final String TAG_ALAMAT = "alamat";
    protected static final String TAG_HP = "no_hp";
    protected static final String TAG_TOKEN = "no_token";

    protected JSONParser jsonParser;
    protected int success;

    protected TextView no_ktp;
    protected TextView nama;
    protected TextView tgl_lahir;
    protected TextView no_telp;
    protected TextView alamat;
    protected TextView no_token;

    protected Button update_profil;
    protected Button update_foto;
    protected Button QRcode;
    protected ImageView foto_profil;
    protected String stremail;

    protected SessionManager session;
    protected ImageLoader imageLoader;

    public ProfilPerempuanFragment(){
        this.jsonParser = new JSONParser();
    }

    public class profil_perempuan extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        String strnik;
        String strnama;
        String strtgl_lahir;
        String stralamat;
        String strno_hp;
        String strfoto;
        String strtoken;
        String url_profil_perempuan;

        public profil_perempuan(){
            this.url_profil_perempuan= "http://rekammedis.gudangtechno.web.id/android/profil_orangtua.php?email="+ProfilPerempuanFragment.this.stremail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProfilPerempuanFragment.this.getActivity());
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = ProfilPerempuanFragment.this.jsonParser.getJSONFromUrl(url_profil_perempuan);
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("orangtua");
                if (success == 1) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        this.strnik = c.getString(TAG_NIK).trim();
                        this.strnama = c.getString(TAG_NAMA).trim();
                        this.strtgl_lahir = c.getString(TAG_TANGGAL).trim();
                        this.stralamat = c.getString(TAG_ALAMAT).trim();
                        this.strno_hp = c.getString(TAG_HP).trim();
                        this.strtoken = c.getString(TAG_TOKEN).trim();
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
                if (this.strfoto == null) {
                    ProfilPerempuanFragment.this.foto_profil.setImageBitmap(BitmapFactory.decodeResource(ProfilPerempuanFragment.this.getResources(), R.drawable.doctor));
                } else {
                    ImageLoader.getInstance().displayImage(strfoto, ProfilPerempuanFragment.this.foto_profil);
                }
                ProfilPerempuanFragment.this.no_ktp.setText(this.strnik);
                ProfilPerempuanFragment.this.nama.setText(this.strnama);
                ProfilPerempuanFragment.this.tgl_lahir.setText(this.strtgl_lahir);
                ProfilPerempuanFragment.this.no_telp.setText(this.strno_hp);
                ProfilPerempuanFragment.this.alamat.setText(this.stralamat);
                ProfilPerempuanFragment.this.no_token.setText(this.strtoken);
            } else {
                Toast.makeText(ProfilPerempuanFragment.this.getActivity().getApplicationContext(), "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onStart(){
        super.onStart();
        this.session = new SessionManager(getActivity());
        this.session.checkLoginPerempuan();

        stremail = this.session.getUserDetails().get(SessionManager.KEY_EMAIL);
        new profil_perempuan().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_profil_perempuan, container, false);
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        ProfilPerempuanFragment.this.no_ktp = (TextView) myView.findViewById(R.id.ktp_perempuan);
        ProfilPerempuanFragment.this.nama = (TextView) myView.findViewById(R.id.nama_perempuan);
        ProfilPerempuanFragment.this.tgl_lahir = (TextView) myView.findViewById(R.id.tgl_perempuan);
        ProfilPerempuanFragment.this.no_telp = (TextView) myView.findViewById(R.id.telepon_perempuan);
        ProfilPerempuanFragment.this.alamat = (TextView) myView.findViewById(R.id.alamat_perempuan);
        ProfilPerempuanFragment.this.no_token = (TextView) myView.findViewById(R.id.no_token);
        ProfilPerempuanFragment.this.foto_profil = (ImageView) myView.findViewById(R.id.foto_perempuan);
        ProfilPerempuanFragment.this.update_profil = (Button) myView.findViewById(R.id.btn_profil_perempuan);
        ProfilPerempuanFragment.this.update_foto = (Button) myView.findViewById(R.id.btn_foto_perempuan);
        ProfilPerempuanFragment.this.QRcode = (Button) myView.findViewById(R.id.button_qrcode);

        this.update_profil.setOnClickListener(new profil_update());
        this.update_foto.setOnClickListener(new foto_update());
        this.QRcode.setOnClickListener(new lihat_barcode());

        return myView;
    }

    class profil_update implements OnClickListener {
        public void onClick(View v) {
            String no_ktp = ProfilPerempuanFragment.this.no_ktp.getText().toString();
            String name = ProfilPerempuanFragment.this.nama.getText().toString();
            String tanggal = ProfilPerempuanFragment.this.tgl_lahir.getText().toString();
            String hp = ProfilPerempuanFragment.this.no_telp.getText().toString();
            String rumah = ProfilPerempuanFragment.this.alamat.getText().toString();
            String token = ProfilPerempuanFragment.this.no_token.getText().toString();
            Intent i = new Intent(ProfilPerempuanFragment.this.getActivity(), UpdateProfilPerempuanActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", no_ktp);
            b.putString("nama", name);
            b.putString("tanggal", tanggal);
            b.putString("no_hp", hp);
            b.putString("alamat", rumah);
            b.putString("no_token", token);
            b.putString("email", stremail);
            i.putExtras(b);
            ProfilPerempuanFragment.this.startActivity(i);
        }
    }

    class foto_update implements OnClickListener {
        public void onClick(View v) {
            String no_ktp = ProfilPerempuanFragment.this.no_ktp.getText().toString();
            Intent i = new Intent(ProfilPerempuanFragment.this.getActivity(), UpdateFotoPerempuanActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", no_ktp);
            i.putExtras(b);
            ProfilPerempuanFragment.this.startActivity(i);
        }
    }

    class lihat_barcode implements OnClickListener {
        public void onClick(View v) {
            String name = ProfilPerempuanFragment.this.nama.getText().toString();
            String token = ProfilPerempuanFragment.this.no_token.getText().toString();
            Intent i = new Intent(ProfilPerempuanFragment.this.getActivity(), LihatQRCodeActivity.class);
            Bundle b = new Bundle();
            b.putString("nama", name);
            b.putString("no_token", token);
            i.putExtras(b);
            ProfilPerempuanFragment.this.startActivity(i);
        }
    }
}