package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImunisasiAdapter extends BaseAdapter {
    private Activity activity;
    ImageView gambar;
    private ArrayList<Imunisasi> data_imunisasi=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public ImunisasiAdapter(Activity a, ArrayList<Imunisasi> d) {
        activity = a; data_imunisasi = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data_imunisasi.size();
    }
    public Object getItem(int position) {
        return data_imunisasi.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_imunisasi, null);
        }
        TextView id_imunisasi = (TextView) vi.findViewById(R.id.id_imunisasi);
        TextView tanggal = (TextView) vi.findViewById(R.id.tanggal);
        TextView nama_imunisasi = (TextView) vi.findViewById(R.id.nama_imunisasi);
        TextView no_register = (TextView) vi.findViewById(R.id.no_register);
        TextView nama_petugas = (TextView) vi.findViewById(R.id.nama_petugas);
        gambar=(ImageView) vi.findViewById(R.id.gambar);

        Imunisasi daftar_imunisasi = data_imunisasi.get(position);
        id_imunisasi.setText(daftar_imunisasi.getId_imunisasi());
        tanggal.setText(daftar_imunisasi.getTanggal());
        nama_imunisasi.setText(daftar_imunisasi.getNama_imunisasi());
        no_register.setText(daftar_imunisasi.getNo_register());
        nama_petugas.setText(daftar_imunisasi.getNama_petugas());

        return vi;
    }
}
