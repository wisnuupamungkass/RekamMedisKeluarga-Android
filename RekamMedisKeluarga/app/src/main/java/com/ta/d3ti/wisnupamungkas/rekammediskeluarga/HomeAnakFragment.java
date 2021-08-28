package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeAnakFragment extends Fragment {
    ListView list_anak;
    Button button_anak;

    JSONParser jParser = new JSONParser();
    ArrayList<Anak> daftar_anak = new ArrayList<>();
    JSONArray daftarAnak = null;
    SessionManager session;
    protected JSONParser jsonParser;

    int success;

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ANAK = "anak";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tanggal_lahir";
    protected static final String TAG_JAM = "jam_lahir";
    protected static final String TAG_BULAN = "bulan";
    protected static final String TAG_JK = "gender";
    protected static final String TAG_BERAT = "berat_badan";
    protected static final String TAG_PANJANG = "panjang_badan";
    protected static final String TAG_LINGKAR = "lingkar_kepala";
    protected static final String TAG_KTP = "ktp_no";
    protected static final String TAG_GIZI = "status_gizi";
    protected static final String TAG_FOTO = "foto";
    protected static final String TAG_IMUNISASI = "status_imunisasi";
    protected static final String TAG_TOKEN = "token_no";

    protected String strktp;

    public HomeAnakFragment(){
        this.jsonParser = new JSONParser();
    }

    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_home_anak, container, false);

        this.session = new SessionManager(getActivity());
        this.session.checkLoginPerempuan();
        strktp = this.session.getUserDetails().get(SessionManager.KEY_KTP);

        ReadAnakTask m= (ReadAnakTask) new ReadAnakTask().execute();

        list_anak = (ListView) myView.findViewById(R.id.listview_anak);
        button_anak = (Button) myView.findViewById(R.id.button_tambah);

        list_anak.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String no_register = ((TextView) view.findViewById(R.id.no_register)).getText().toString();
                String nama_anak = ((TextView) view.findViewById(R.id.nama_anak)).getText().toString();
                String tgl_lahir = ((TextView) view.findViewById(R.id.tgl_lahir_anak)).getText().toString();
                String jam_lahir = ((TextView) view.findViewById(R.id.jam_lahir_anak)).getText().toString();
                String bulan = ((TextView) view.findViewById(R.id.bulan)).getText().toString();
                String gender = ((TextView) view.findViewById(R.id.jk_anak)).getText().toString();
                String berat_badan = ((TextView) view.findViewById(R.id.berat_badan)).getText().toString();
                String panjang_badan = ((TextView) view.findViewById(R.id.panjang_anak)).getText().toString();
                String lingkar_kepala = ((TextView) view.findViewById(R.id.lingkar_kepala)).getText().toString();
                String ktp_no = ((TextView) view.findViewById(R.id.ktp_no)).getText().toString();
                String gizi = ((TextView) view.findViewById(R.id.gizi_anak)).getText().toString();
                String foto = ((TextView) view.findViewById(R.id.foto_anak)).getText().toString();
                String imunisasi = ((TextView) view.findViewById(R.id.imunisasi_anak)).getText().toString();
                String token_no = ((TextView) view.findViewById(R.id.token_no)).getText().toString();

                Intent i = null;
                i = new Intent(HomeAnakFragment.this.getActivity(), MenuAnakActivity.class);
                Bundle b = new Bundle();
                b.putString("no_register", no_register);
                b.putString("nama_anak", nama_anak);
                b.putString("tanggal_lahir", tgl_lahir);
                b.putString("jam_lahir", jam_lahir);
                b.putString("gender", gender);
                b.putString("berat_badan", berat_badan);
                b.putString("panjang_badan", panjang_badan);
                b.putString("lingkar_kepala", lingkar_kepala);
                b.putString("ktp_no", ktp_no);
                b.putString("status_gizi", gizi);
                b.putString("foto_anak", foto);
                b.putString("imunisasi", imunisasi);
                b.putString("token_no", token_no);
                i.putExtras(b);
                startActivity(i);
            }
        });

        button_anak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTambahAnak();
            }
        });

        session = new SessionManager(getActivity().getApplicationContext());
        return myView;
    }

    private void goTambahAnak(){
        Intent intent = new Intent(this.getActivity(), TambahAnakActivity.class);
        startActivity(intent);
    }

    class ReadAnakTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        String url_tampil_anak;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeAnakFragment.this.getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            url_tampil_anak= "http://rekammedis.gudangtechno.web.id/android/tampil_anak.php?ktp_no="+HomeAnakFragment.this.strktp;
            Anak tempAnak = new Anak();
            try {
                JSONObject json = HomeAnakFragment.this.jsonParser.getJSONFromUrl(url_tampil_anak);

                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftarAnak = json.getJSONArray(TAG_ANAK);
                    for (int i = 0; i < daftarAnak.length() ; i++){
                        JSONObject c = daftarAnak.getJSONObject(i);
                        tempAnak = new Anak();
                        tempAnak.setAnakId(c.getString(TAG_REGISTER));
                        tempAnak.setAnakNama(c.getString(TAG_NAMA));
                        tempAnak.setAnakTgl(c.getString(TAG_TANGGAL));
                        tempAnak.setAnakJam(c.getString(TAG_JAM));
                        tempAnak.setBulan(c.getString(TAG_BULAN));
                        tempAnak.setAnakJk(c.getString(TAG_JK));
                        tempAnak.setAnakBerat(c.getString(TAG_BERAT));
                        tempAnak.setAnakPanjang(c.getString(TAG_PANJANG));
                        tempAnak.setAnakLingkar(c.getString(TAG_LINGKAR));
                        tempAnak.setAnakKtp(c.getString(TAG_KTP));
                        tempAnak.setAnakFoto(c.getString(TAG_FOTO));
                        tempAnak.setAnakGizi(c.getString(TAG_GIZI));
                        tempAnak.setAnakImunisasi(c.getString(TAG_IMUNISASI));
                        tempAnak.setAnakToken(c.getString(TAG_TOKEN));
                        daftar_anak.add(tempAnak);
                    }
                    return "OK";
                }
                else {
                    return "no results";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(result.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(HomeAnakFragment.this.getActivity(), "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }

            if(result.equalsIgnoreCase("no results"))
            {
                Toast.makeText(HomeAnakFragment.this.getActivity(), "List Daftar Anak Masih Kosong", Toast.LENGTH_LONG).show();
            }
            if(success == 1)
            {
                list_anak.setAdapter(new AnakAdapter(HomeAnakFragment.this.getActivity(),HomeAnakFragment.this.daftar_anak));
            }
        }
    }
}
