package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TokenFragment extends Fragment{
    JSONParser jsonParser = new JSONParser();
    String url_token = "http://rekammedis.gudangtechno.web.id/android/login_token.php";

    protected static final String TAG_TOKEN= "no_token";
    protected static final String TAG_SUCCESS= "success";

    String strtoken,jenis_kelamin,no_token;
    int success;

    Button submit;
    Button QRCode;
    EditText EdtToken;
    SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_token, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        session = new SessionManager(getActivity());
        submit = (Button) myView.findViewById(R.id.button_submit);
        QRCode = (Button) myView.findViewById(R.id.btn_scanner);
        EdtToken = (EditText) myView.findViewById(R.id.token);

        submit.setOnClickListener(new simpan());
        QRCode.setOnClickListener(new scanner());
        return myView;
    }

    class scanner implements View.OnClickListener {
        public void onClick(View v) {
            try {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            } catch (ActivityNotFoundException anfe) {
                Log.e("Error :", "Error");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                strtoken = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                new SubmitToken().execute();
            }
        }
    }

    class simpan implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TokenFragment.this.session = new SessionManager(TokenFragment.this.getActivity());
            TokenFragment.this.validation();
        }
    }

    public void validation() {
        strtoken = EdtToken.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(this.EdtToken.getText().toString())) {
            this.EdtToken.setError("Harus diisi");
            focusView = this.EdtToken;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            new SubmitToken().execute();
        }
    }

    public class SubmitToken extends AsyncTask<String,String,String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(TokenFragment.this.getActivity());
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TAG_TOKEN, strtoken));

            JSONObject json = jsonParser.makeHttpRequest(url_token, "POST", params);
            if(json == null){
                return "Error Converting";
            }
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if(success == 1) {
                    jenis_kelamin = json.getString("jenis_kelamin");
                    Log.e("Error", "Jenis Kelamin=" + jenis_kelamin);

                    no_token = json.getString("no_token");
                    Log.e("Error", "Token Anak=" + no_token);
                }

                JSONArray hasil = json.getJSONArray("token");

                for (int i = 0; i < hasil.length(); i++) {
                    JSONObject c = hasil.getJSONObject(i);

                    String id_token = c.getString("id_token").trim();

                    session.createLoginToken(id_token);
                    Log.e("Ok", "Ambil Data");
                }
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (success ==1 && jenis_kelamin.equals("L")) {
                TokenFragment.this.startActivity(new Intent(TokenFragment.this.getActivity(), PetugasMenuPriaActivity.class));
                TokenFragment.this.getActivity().finish();
            }
            else if (success ==1 && jenis_kelamin.equals("P")) {
                TokenFragment.this.startActivity(new Intent(TokenFragment.this.getActivity(), PetugasMenuPerempuanActivity.class));
                TokenFragment.this.getActivity().finish();
            }
            else if (success ==1 && no_token !=null){
                TokenFragment.this.startActivity(new Intent(TokenFragment.this.getActivity(), PetugasMenuAnakActivity.class));
                TokenFragment.this.getActivity().finish();
            }
            else if(success ==0){
                Toast.makeText(TokenFragment.this.getActivity(), "No Token Tidak Ditemukan",  Toast.LENGTH_SHORT).show();
            }
            else if(result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(TokenFragment.this.getActivity(), "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }
}
