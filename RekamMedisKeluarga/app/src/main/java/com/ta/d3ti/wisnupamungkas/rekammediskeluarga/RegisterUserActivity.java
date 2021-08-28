package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RegisterUserActivity extends Activity {
    JSONParser jsonParser = new JSONParser();
    String url_register_user= "http://rekammedis.gudangtechno.web.id/android/register_user.php";

    // JSON Node names, ini harus sesuai yang di API
    protected static final String TAG_NIK = "no_ktp";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tgl_lahir";
    protected static final String TAG_JK = "jenis_kelamin";
    protected static final String TAG_ID_PROV = "id_provinsi";
    protected static final String TAG_ID_KAB = "id_kabupaten";
    protected static final String TAG_HP = "no_hp";
    protected static final String TAG_ALAMAT = "alamat";
    protected static final String TAG_EMAIL = "email";
    protected static final String TAG_UID = "uid";

    protected Spinner spinnerProvinsi;
    protected Spinner spinnerKabupaten;
    protected String strid_provinsi;
    protected String strid_kabupaten;
    protected SimpleDateFormat dateFormatter;
    protected String strjk;
    protected EditText nik;
    protected EditText tgl_lahir;
    protected EditText no_hp;
    protected EditText alamat;
    protected Button register;
    protected Button cancel;
    protected ProgressDialog pDialog;
    protected RadioGroup rGroup;
    protected DatePickerDialog tanggal_dialog;
    protected String isi_uid,isi_nama,isi_email;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.nik = (EditText) findViewById(R.id.nik);
        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.tgl_lahir = (EditText) findViewById(R.id.tgl_lahir);
        this.tgl_lahir.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tgl_lahir.requestFocus();
        setDateTimeField();
        this.no_hp = (EditText) findViewById(R.id.no_hp);
        this.spinnerProvinsi = (Spinner) findViewById(R.id.provinsi);
        this.spinnerKabupaten = (Spinner) findViewById(R.id.kabupaten);
        this.alamat = (EditText) findViewById(R.id.alamat);
        this.register = (Button) findViewById(R.id.button_register);
        this.cancel = (Button) findViewById(R.id.button_cancel);
        this.rGroup = (RadioGroup) findViewById(R.id.radioJk);

        Bundle b = getIntent().getExtras();
        isi_uid = b.getString("uid");
        isi_nama = b.getString("nama");
        isi_email = b.getString("email");

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                RadioButton rb = (RadioButton) rGroup.findViewById(i);

                switch (i){
                    case R.id.radioPria :
                        strjk = "L";
                        break;
                    case R.id.radioPerempuan :
                        strjk = "P";
                        break;
                    default:
                        strjk = null;
                        break;
                }
            }
        });

        try {

            JSONArray data = new JSONArray(getJSONUrl("http://rekammedis.gudangtechno.web.id/android/getProvinsi.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_provinsi", c.getString("id_provinsi"));
                map.put("nama_provinsi", c.getString("nama_provinsi"));
                MyArrList.add(map);

            }
            spinnerProvinsi.setAdapter(new SimpleAdapter(this, MyArrList, R.layout.activity_spinner_provinsi, new String[] {"id_provinsi", "nama_provinsi"}, new int[] {R.id.id_provinsi, R.id.nama_provinsi}));

            spinnerProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    strid_provinsi = MyArrList.get(position).get("id_provinsi");
                    Log.e("Error",""+strid_provinsi);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(RegisterUserActivity.this, "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            JSONArray data = new JSONArray(getJSONUrl("http://rekammedis.gudangtechno.web.id/android/getKabupaten.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_kabupaten", c.getString("id_kabupaten"));
                map.put("nama_kabupaten", c.getString("nama_kabupaten"));
                MyArrList.add(map);

            }
            spinnerKabupaten.setAdapter(new SimpleAdapter(this, MyArrList, R.layout.activity_spinner_kabupaten, new String[] {"id_kabupaten", "nama_kabupaten"}, new int[] {R.id.id_kabupaten, R.id.nama_kabupaten}));

            spinnerKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    strid_kabupaten = MyArrList.get(position).get("id_kabupaten");
                    Log.e("Error",""+strid_kabupaten);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(RegisterUserActivity.this, "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.register.setOnClickListener(new register());
        this.cancel.setOnClickListener(new cancel());
    }

    public void setDateTimeField() {
        this.tgl_lahir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RegisterUserActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                RegisterUserActivity.this.tgl_lahir.setText(RegisterUserActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    class register implements View.OnClickListener {
        public void onClick(View v) {
            RegisterUserActivity.this.validation();
        }
    }

    class cancel implements View.OnClickListener {
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            finish();
        }
    }

    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        finish();
    }

    public void validation() {
        String ktp = this.nik.getText().toString();
        String tanggal = this.tgl_lahir.getText().toString();
        String hp = this.no_hp.getText().toString();
        String address = this.alamat.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(ktp)) {
            this.nik.setError("Harus diisi");
            focusView = this.nik;
            cancel = true;
        }
        if (TextUtils.isEmpty(tanggal)) {
            this.tgl_lahir.setError("Harus diisi");
            focusView = this.tgl_lahir;
            cancel = true;
        }
        if (TextUtils.isEmpty(hp)) {
            this.no_hp.setError("Harus diisi");
            focusView = this.no_hp;
            cancel = true;
        }
        if (TextUtils.isEmpty(address)) {
            this.alamat.setError("Harus diisi");
            focusView = this.alamat;
            cancel = true;
        }
        if(strjk == null){
            Toast.makeText(RegisterUserActivity.this, "Jenis Kelamin Belum Dipilih...",  Toast.LENGTH_SHORT).show();
            focusView = rGroup;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new registerUser().execute();
    }

    public class registerUser extends AsyncTask<String, String, String> {
        String strnik;
        String strtgl_lahir;
        String strno_hp;
        String stralamat;
        int success;

        public registerUser() {
            this.strnik = RegisterUserActivity.this.nik.getText().toString();
            this.strtgl_lahir = RegisterUserActivity.this.tgl_lahir.getText().toString();
            this.strno_hp = RegisterUserActivity.this.no_hp.getText().toString();
            this.stralamat = RegisterUserActivity.this.alamat.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            RegisterUserActivity.this.pDialog = new ProgressDialog(RegisterUserActivity.this);
            RegisterUserActivity.this.pDialog.setMessage("Loading...");
            RegisterUserActivity.this.pDialog.setIndeterminate(false);
            RegisterUserActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getUserList();
        }

        public String getUserList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_NIK, this.strnik));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_NAMA, isi_nama));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_TANGGAL, this.strtgl_lahir));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_JK, strjk));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_HP, this.strno_hp));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_ID_PROV, strid_provinsi));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_ID_KAB, strid_kabupaten));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_ALAMAT, this.stralamat));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_EMAIL, isi_email));
            params.add(new BasicNameValuePair(RegisterUserActivity.TAG_UID, isi_uid));

            JSONObject json = jsonParser.makeHttpRequest(url_register_user, "POST", params);

            if (json == null) {
                return "Error Converting";
            }
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
            RegisterUserActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(RegisterUserActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(RegisterUserActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                Builder pDialog = new Builder(RegisterUserActivity.this);
                pDialog.setTitle("Informasi Account");
                pDialog.setMessage("Akun Berhasil Dibuat");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else if (success == 0) {
                Toast.makeText(RegisterUserActivity.this.getApplicationContext(), "Gagal Register Account...", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                RegisterUserActivity.this.finish();
                RegisterUserActivity.this.startActivity(new Intent(RegisterUserActivity.this, MainActivity.class));
            }
        }
    }
}
