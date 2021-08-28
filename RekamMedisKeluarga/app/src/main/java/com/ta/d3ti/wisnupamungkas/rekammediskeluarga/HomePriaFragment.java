package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

public class HomePriaFragment extends Fragment {

    SessionManager session;
    protected String email;
    protected ImageButton profil;
    protected ImageButton medis;

    public void onStart() {
        super.onStart();
        session = new SessionManager(getActivity().getApplicationContext());
        session.isLoggedInUser();
        session.checkLoginPria();

        HashMap<String,String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_home_pria, container, false);
        this.profil = (ImageButton) myView.findViewById(R.id.profil_pria);
        this.medis = (ImageButton) myView.findViewById(R.id.menu_rekam_medis);
        this.profil.setOnClickListener(new menu_profil());
        this.medis.setOnClickListener(new menu_medis());
        return myView;
    }

    private class menu_profil implements View.OnClickListener {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction;
            ProfilPriaFragment fragment = new ProfilPriaFragment();
            fragmentTransaction = HomePriaFragment.this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            getActivity().setTitle("Profil Pengguna");
        }
    }

    private class menu_medis implements View.OnClickListener {
        public void onClick(View v) {
            HomePriaFragment.this.startActivity(new Intent(HomePriaFragment.this.getActivity(), ListRekamMedisPriaActivity.class));
        }
    }
}
