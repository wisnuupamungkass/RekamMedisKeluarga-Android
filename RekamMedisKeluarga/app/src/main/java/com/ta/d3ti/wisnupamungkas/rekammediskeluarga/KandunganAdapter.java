package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class KandunganAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Kandungan> data_kandungan=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public KandunganAdapter(Activity a, ArrayList<Kandungan> d) {
        activity = a; data_kandungan = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data_kandungan.size();
    }
    public Object getItem(int position) {
        return data_kandungan.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null){
            vi = inflater.inflate(R.layout.list_item_kandungan, null);
        }
        TextView id_kandungan = (TextView) vi.findViewById(R.id.id_kandungan);
        TextView kandungan_ke = (TextView) vi.findViewById(R.id.kandungan_ke);
        TextView no_ktp = (TextView) vi.findViewById(R.id.no_ktp);

        Kandungan daftar_kandungan = data_kandungan.get(position);
        id_kandungan.setText(daftar_kandungan.getId_kandungan());
        kandungan_ke.setText(daftar_kandungan.getKandungan_ke());
        no_ktp.setText(daftar_kandungan.getNo_ktp());
        return vi;
    }
}