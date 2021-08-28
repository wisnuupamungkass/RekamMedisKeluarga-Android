package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TungguFragment extends Fragment {

    ImageView gambar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_tunggu, container, false);

        gambar = (ImageView) myView.findViewById(R.id.approve);

        return myView;
    }
}
