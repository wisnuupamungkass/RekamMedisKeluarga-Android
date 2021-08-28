package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PetugasTambahDetailKandunganActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String url_tambah_detail_kandungan = "http://rekammedis.gudangtechno.web.id/android/tambah_detail_kandungan.php";
    String UPLOAD_URL = "http://rekammedis.gudangtechno.web.id/android/upload_kandungan.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_PERIKSA = "tanggal_periksa";
    protected static final String TAG_BULAN = "bulan_hamil";
    protected static final String TAG_KONDISI = "kondisi_janin";
    protected static final String TAG_FOTO = "foto";
    protected static final String TAG_KANDUNGAN = "kandungan_id";
    protected static final String TAG_PETUGAS = "petugas_id";

    protected EditText tanggal_periksa;
    protected EditText bulan_hamil;
    protected EditText kondisi_janin;
    protected String stridkandungan, strpetugas;

    protected Button tambah_detail_kandungan;
    protected Button batal_detail_kandungan;
    protected Button tambah_foto;
    protected ProgressDialog pDialog;

    protected SimpleDateFormat dateFormatter;
    protected DatePickerDialog tanggal_dialog;

    private int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    protected Uri filePath;
    protected String encodedImage, result;

    public  void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_tambah_detail_kandungan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.tanggal_periksa = (EditText) findViewById(R.id.tanggal_pemeriksaan);
        this.tanggal_periksa.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tanggal_periksa.requestFocus();
        setDateTimeField();
        this.bulan_hamil = (EditText) findViewById(R.id.bulan_hamil);
        this.kondisi_janin = (EditText) findViewById(R.id.kondisi_janin);
        this.tambah_detail_kandungan = (Button) findViewById(R.id.btn_tambah_detail_kandungan);
        this.batal_detail_kandungan = (Button) findViewById(R.id.btn_batal_detail_kandungan);
        this.tambah_foto = (Button) findViewById(R.id.buttonChoose);
        this.tambah_foto.setOnClickListener(new showFileChooser());
        imageView = (ImageView) findViewById(R.id.imageView);
        this.tambah_detail_kandungan.setOnClickListener(new tambah_detail());
        this.batal_detail_kandungan.setOnClickListener(new batal_detail());

        Bundle b = getIntent().getExtras();
        stridkandungan = b.getString("kandungan_id");
        Log.e("Error","ID Kandungan = "+stridkandungan);
        strpetugas = b.getString("petugas_id");
        Log.e("Error","ID Petugas = "+strpetugas);
    }

    class showFileChooser implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Pilih Foto USG"), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            String tanggal = tanggal_periksa.getText().toString();
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                PetugasTambahDetailKandunganActivity.this.pDialog = new ProgressDialog(PetugasTambahDetailKandunganActivity.this);
                PetugasTambahDetailKandunganActivity.this.pDialog.setMessage("Loading...");
                PetugasTambahDetailKandunganActivity.this.pDialog.setIndeterminate(false);
                PetugasTambahDetailKandunganActivity.this.pDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDialog.dismiss();
                AlertDialog.Builder pDialog = new AlertDialog.Builder(PetugasTambahDetailKandunganActivity.this);
                pDialog.setTitle("Tambah Detail Kandungan");
                pDialog.setMessage("Data Detail Kandungan Berhasil Tersimpan");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                if(uploadImage  != null) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put(TAG_PERIKSA, tanggal);
                    data.put(TAG_FOTO, uploadImage);
                    result = rh.sendPostRequest(UPLOAD_URL, data);
                }else{
                    Toast.makeText(PetugasTambahDetailKandunganActivity.this, "Pastikan Foto USG Kandungan Terpilih...", Toast.LENGTH_SHORT).show();
                }
                return result;
            }

            class berhasil implements DialogInterface.OnClickListener {
                public void onClick(DialogInterface dialog, int d) {
                    dialog.dismiss();
                    Intent kandungan = new Intent(PetugasTambahDetailKandunganActivity.this, PetugasMenuPerempuanActivity.class);
                    startActivity(kandungan);
                    PetugasTambahDetailKandunganActivity.this.finish();
                }
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public void setDateTimeField() {
        this.tanggal_periksa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PetugasTambahDetailKandunganActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                PetugasTambahDetailKandunganActivity.this.tanggal_periksa.setText(PetugasTambahDetailKandunganActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    class tambah_detail implements View.OnClickListener {
        public void onClick(View v) {
            PetugasTambahDetailKandunganActivity.this.validation();
        }
    }

    class batal_detail implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void validation() {
        String tanggal_periksa = this.tanggal_periksa.getText().toString();
        String bulan_hamil = this.bulan_hamil.getText().toString();
        String kondisi_janin = this.kondisi_janin.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(tanggal_periksa)) {
            this.tanggal_periksa.setError("Harus diisi");
            focusView = this.tanggal_periksa;
            cancel = true;
        }
        if (TextUtils.isEmpty(bulan_hamil)) {
            this.bulan_hamil.setError("Harus diisi");
            focusView = this.bulan_hamil;
            cancel = true;
        }
        if (TextUtils.isEmpty(kondisi_janin)) {
            this.kondisi_janin.setError("Harus diisi");
            focusView = this.kondisi_janin;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new tambahDetailKandungan().execute();
    }

    public class tambahDetailKandungan extends AsyncTask<String, String, String> {
        String strtanggal;
        String strbulan;
        String strkondisi;
        int success;

        public tambahDetailKandungan() {
            this.strtanggal = PetugasTambahDetailKandunganActivity.this.tanggal_periksa.getText().toString();
            this.strbulan = PetugasTambahDetailKandunganActivity.this.bulan_hamil.getText().toString();
            this.strkondisi = PetugasTambahDetailKandunganActivity.this.kondisi_janin.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            return getDetailKandunganList();
        }

        public String getDetailKandunganList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(PetugasTambahDetailKandunganActivity.TAG_PERIKSA, this.strtanggal));
            params.add(new BasicNameValuePair(PetugasTambahDetailKandunganActivity.TAG_BULAN, this.strbulan));
            params.add(new BasicNameValuePair(PetugasTambahDetailKandunganActivity.TAG_KONDISI, this.strkondisi));
            params.add(new BasicNameValuePair(PetugasTambahDetailKandunganActivity.TAG_KANDUNGAN, stridkandungan));
            params.add(new BasicNameValuePair(PetugasTambahDetailKandunganActivity.TAG_PETUGAS, strpetugas));

            JSONObject json = jsonParser.makeHttpRequest(url_tambah_detail_kandungan, "POST", params);
            if (json == null) {
                return "Error Converting";
            }
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("Error Converting")) {
                Toast.makeText(PetugasTambahDetailKandunganActivity.this, "Periksa Koneksi Inernet Anda...", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(PetugasTambahDetailKandunganActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            } else if (success == 1) {
                uploadImage();
            } else if (success == 0) {
                Toast.makeText(PetugasTambahDetailKandunganActivity.this.getApplicationContext(), "Gagal Tambah Data Kandungan...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}