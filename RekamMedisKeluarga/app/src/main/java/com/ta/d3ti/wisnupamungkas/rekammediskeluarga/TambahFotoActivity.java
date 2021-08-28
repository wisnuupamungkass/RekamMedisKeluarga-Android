package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class TambahFotoActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String UPLOAD_URL = "http://rekammedis.gudangtechno.web.id/android/upload_foto_anak.php";
    public static final String UPLOAD_KEY = "foto";
    public static final String TAG_KET= "keterangan";
    public static final String KEY_REGISTER = "no_register";

    ImageView imageView;
    EditText keterangan;
    Button btncamera;
    Button buttonUpload;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    protected String encodedImage, result;
    String isi_no_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_foto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.btncamera = (Button) findViewById(R.id.btn_camera);
        this.buttonUpload = (Button) findViewById(R.id.buttonUpload);
        this.imageView = (ImageView) findViewById(R.id.image_anak);
        this.keterangan = (EditText) findViewById(R.id.keterangan);

        buttonUpload.setOnClickListener(this);
        btncamera.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        isi_no_register = b.getString("no_register");
        Log.e("Error","No Register = " +isi_no_register);
    }

    private void showCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            String strketerangan = TambahFotoActivity.this.keterangan.getText().toString();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TambahFotoActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(KEY_REGISTER, isi_no_register);
                data.put(TAG_KET, strketerangan);
                data.put(UPLOAD_KEY, uploadImage);
                result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == btncamera) {
            showCamera();
        }

        if(v == buttonUpload){
            String keterangan = this.keterangan.getText().toString();
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(keterangan)) {
                this.keterangan.setError("Harus diisi");
                focusView = this.keterangan;
                cancel = true;
            }else{
                uploadImage();
            }

            if (cancel) {
                focusView.requestFocus();
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