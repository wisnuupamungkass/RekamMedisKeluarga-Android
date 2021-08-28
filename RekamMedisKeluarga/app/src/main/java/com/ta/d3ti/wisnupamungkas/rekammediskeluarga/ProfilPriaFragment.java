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

public class ProfilPriaFragment extends Fragment {
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

    public ProfilPriaFragment(){
        this.jsonParser = new JSONParser();
    }

    public class profil_pria extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        String strnik;
        String strnama;
        String strtgl_lahir;
        String stralamat;
        String strno_hp;
        String strfoto;
        String strtoken;
        String url_profil_pria;

        public profil_pria(){
            this.url_profil_pria= "http://rekammedis.gudangtechno.web.id/android/profil_orangtua.php?email="+ProfilPriaFragment.this.stremail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProfilPriaFragment.this.getActivity());
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = ProfilPriaFragment.this.jsonParser.getJSONFromUrl(url_profil_pria);
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
                    ProfilPriaFragment.this.foto_profil.setImageBitmap(BitmapFactory.decodeResource(ProfilPriaFragment.this.getResources(), R.drawable.doctor));
                } else {
                    ImageLoader.getInstance().displayImage(strfoto, ProfilPriaFragment.this.foto_profil);
                }
                ProfilPriaFragment.this.no_ktp.setText(this.strnik);
                ProfilPriaFragment.this.nama.setText(this.strnama);
                ProfilPriaFragment.this.tgl_lahir.setText(this.strtgl_lahir);
                ProfilPriaFragment.this.no_telp.setText(this.strno_hp);
                ProfilPriaFragment.this.alamat.setText(this.stralamat);
                ProfilPriaFragment.this.no_token.setText(this.strtoken);
            } else {
                Toast.makeText(ProfilPriaFragment.this.getActivity().getApplicationContext(), "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onStart(){
        super.onStart();
        this.session = new SessionManager(getActivity());
        this.session.checkLoginPria();

        stremail = this.session.getUserDetails().get(SessionManager.KEY_EMAIL);
        new profil_pria().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_profil_pria, container, false);
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        ProfilPriaFragment.this.no_ktp = (TextView) myView.findViewById(R.id.ktp_pria);
        ProfilPriaFragment.this.nama = (TextView) myView.findViewById(R.id.nama_pria);
        ProfilPriaFragment.this.tgl_lahir = (TextView) myView.findViewById(R.id.tgl_pria);
        ProfilPriaFragment.this.no_telp = (TextView) myView.findViewById(R.id.telepon_pria);
        ProfilPriaFragment.this.alamat = (TextView) myView.findViewById(R.id.alamat_pria);
        ProfilPriaFragment.this.no_token = (TextView) myView.findViewById(R.id.no_token);
        ProfilPriaFragment.this.foto_profil = (ImageView) myView.findViewById(R.id.foto_pria);
        ProfilPriaFragment.this.update_profil = (Button) myView.findViewById(R.id.btn_profil_pria);
        ProfilPriaFragment.this.update_foto = (Button) myView.findViewById(R.id.btn_foto_pria);
        ProfilPriaFragment.this.QRcode = (Button) myView.findViewById(R.id.button_qrcode);

        this.update_profil.setOnClickListener(new profil_update());
        this.update_foto.setOnClickListener(new foto_update());
        this.QRcode.setOnClickListener(new lihat_barcode());

        return myView;
    }

    class profil_update implements OnClickListener {
        public void onClick(View v) {
            String no_ktp = ProfilPriaFragment.this.no_ktp.getText().toString();
            String name = ProfilPriaFragment.this.nama.getText().toString();
            String tanggal = ProfilPriaFragment.this.tgl_lahir.getText().toString();
            String hp = ProfilPriaFragment.this.no_telp.getText().toString();
            String rumah = ProfilPriaFragment.this.alamat.getText().toString();
            String token = ProfilPriaFragment.this.no_token.getText().toString();
            Intent i = new Intent(ProfilPriaFragment.this.getActivity(), UpdateProfilPriaActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", no_ktp);
            b.putString("nama", name);
            b.putString("tanggal", tanggal);
            b.putString("no_hp", hp);
            b.putString("alamat", rumah);
            b.putString("no_token", token);
            b.putString("email", stremail);
            i.putExtras(b);
            ProfilPriaFragment.this.startActivity(i);
        }
    }

    class foto_update implements OnClickListener {
        public void onClick(View v) {
            String no_ktp = ProfilPriaFragment.this.no_ktp.getText().toString();
            Intent i = new Intent(ProfilPriaFragment.this.getActivity(), UpdateFotoPriaActivity.class);
            Bundle b = new Bundle();
            b.putString("no_ktp", no_ktp);
            i.putExtras(b);
            ProfilPriaFragment.this.startActivity(i);
        }
    }

    class lihat_barcode implements OnClickListener {
        public void onClick(View v) {
            String name = ProfilPriaFragment.this.nama.getText().toString();
            String token = ProfilPriaFragment.this.no_token.getText().toString();
            Intent i = new Intent(ProfilPriaFragment.this.getActivity(), LihatQRCodeActivity.class);
            Bundle b = new Bundle();
            b.putString("nama", name);
            b.putString("no_token", token);
            i.putExtras(b);
            ProfilPriaFragment.this.startActivity(i);
        }
    }
}