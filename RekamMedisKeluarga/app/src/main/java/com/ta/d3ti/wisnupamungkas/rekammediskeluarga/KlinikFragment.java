package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KlinikFragment extends Fragment {
    JSONParser jsonParser = new JSONParser();
    protected String url_klinik_petugas= "http://rekammedis.gudangtechno.web.id/android/register_klinik.php";

    protected static final String TAG_KLINIK = "nama_klinik";
    protected static final String TAG_IZIN = "izin_praktek";
    protected static final String TAG_EMAIL = "email";

    protected int success;
    protected SessionManager session;
    protected Button simpan;
    protected String email;
    protected EditText klinik;
    protected EditText izin;
    protected ProgressDialog pDialog;

    class register_klinik implements OnClickListener {
        public void onClick(View v) {
            KlinikFragment.this.validation();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewKlinik = inflater.inflate(R.layout.activity_klinik, container, false);
        this.klinik = (EditText) viewKlinik.findViewById(R.id.nama_klinik);
        this.izin = (EditText) viewKlinik.findViewById(R.id.no_klinik);
        this.simpan = (Button) viewKlinik.findViewById(R.id.button_submit);
        this.simpan.setOnClickListener(new register_klinik());
        session = new SessionManager(getActivity().getApplicationContext());
        session.isLoggedInPetugas();
        session.checkLoginPetugas();

        HashMap<String,String> petugas = session.getPetugasDetails();
        email = petugas.get(SessionManager.KEY_EMAIL);
        Log.e("error", "nilai sukses=" + email);
        return viewKlinik;
    }

    public void onStart() {
        super.onStart();
    }

    public void validation() {
        String nama_klinik = KlinikFragment.this.klinik.getText().toString();
        String nomor_izin = KlinikFragment.this.izin.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(nama_klinik)) {
            KlinikFragment.this.klinik.setError("Harus diisi");
            focusView = KlinikFragment.this.klinik;
            cancel = true;
        }
        if (TextUtils.isEmpty(nomor_izin)) {
            KlinikFragment.this.izin.setError("Harus diisi");
            focusView = KlinikFragment.this.izin;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new daftar_klinik().execute();
    }

    public class daftar_klinik extends AsyncTask<String, String, String> {
        String strklinik = KlinikFragment.this.klinik.getText().toString();;
        String strizin = KlinikFragment.this.izin.getText().toString();

        protected void onPreExecute() {
            super.onPreExecute();
            KlinikFragment.this.pDialog = new ProgressDialog(KlinikFragment.this.getActivity());
            KlinikFragment.this.pDialog.setMessage("Loading...");
            KlinikFragment.this.pDialog.setIndeterminate(false);
            KlinikFragment.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getKlinikList();
        }

        public String getKlinikList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(KlinikFragment.TAG_KLINIK, this.strklinik));
            params.add(new BasicNameValuePair(KlinikFragment.TAG_IZIN, this.strizin));
            params.add(new BasicNameValuePair(KlinikFragment.TAG_EMAIL, email));

            JSONObject json = jsonParser.makeHttpRequest(url_klinik_petugas, "POST", params);

            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            KlinikFragment.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(KlinikFragment.this.getActivity(), "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            } else if (success == 1) {
                AlertDialog.Builder pDialog = new AlertDialog.Builder(KlinikFragment.this.getActivity());
                pDialog.setTitle("Registrasi Klinik");
                pDialog.setMessage("Data Berhasil Disimpan");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            } else if (success == 0) {
                Toast.makeText(KlinikFragment.this.getActivity().getApplicationContext(), "Data Telah Terdaftar... ", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            berhasil() {
            }

            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                FragmentTransaction fragmentTransaction;
                TungguFragment fragment = new TungguFragment();
                fragmentTransaction = KlinikFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        }
    }
}

