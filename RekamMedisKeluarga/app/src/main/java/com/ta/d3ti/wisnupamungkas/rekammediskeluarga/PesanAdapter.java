package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PesanAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Pesan> data_pesan=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public PesanAdapter(Activity a, ArrayList<Pesan> d) {
        activity = a; data_pesan = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data_pesan.size();
    }
    public Object getItem(int position) {
        return data_pesan.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_pesan, null);
        TextView id_pesan = (TextView) vi.findViewById(R.id.id_pesan);
        TextView tanggal = (TextView) vi.findViewById(R.id.tanggal_pesan);
        TextView anak = (TextView) vi.findViewById(R.id.anak_nama);
        TextView orangtua = (TextView) vi.findViewById(R.id.orangtua_nama);

        Pesan daftar_pesan = data_pesan.get(position);
        id_pesan.setText(daftar_pesan.getId_pesan());
        tanggal.setText(daftar_pesan.getTanggal());
        anak.setText(daftar_pesan.getNama_anak());
        orangtua.setText(daftar_pesan.getNama_orangtua());

        return vi;
    }
}
