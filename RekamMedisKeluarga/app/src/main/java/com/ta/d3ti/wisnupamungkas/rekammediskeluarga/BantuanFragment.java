package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class BantuanFragment extends Fragment {

    protected ImageButton btn_callphone;
    protected ImageButton btn_email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_help, container, false);

        btn_callphone =(ImageButton) myView.findViewById(R.id.ButtonPhone);
        this.btn_callphone.setOnClickListener(new panggil());

        btn_email =(ImageButton) myView.findViewById(R.id.ButtonEmail);
        this.btn_email.setOnClickListener(new email());

        return myView;
    }

    private class panggil implements View.OnClickListener {
        public void onClick(View v) {
            dialContactPhone("+6281804561038");
        }
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private class email implements View.OnClickListener {
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"idur.onot@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT   , "");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(BantuanFragment.this.getActivity(), "There Are No Email Clients Installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}