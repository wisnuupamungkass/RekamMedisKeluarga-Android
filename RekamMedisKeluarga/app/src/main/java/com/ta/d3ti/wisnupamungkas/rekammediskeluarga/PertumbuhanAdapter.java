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

public class PertumbuhanAdapter extends BaseAdapter{
    private Activity activity;
    ImageView gambar;
    private ArrayList<Pertumbuhan> data_pertumbuhan=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public PertumbuhanAdapter(Activity a, ArrayList<Pertumbuhan> d) {
        activity = a; data_pertumbuhan = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data_pertumbuhan.size();
    }
    public Object getItem(int position) {
        return data_pertumbuhan.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_pertumbuhan, null);
        TextView id_pertumbuhan = (TextView) vi.findViewById(R.id.id_pertumbuhan);
        TextView tanggal = (TextView) vi.findViewById(R.id.tanggal);
        TextView lingkar_kepala = (TextView) vi.findViewById(R.id.lingkar);
        TextView berat_badan = (TextView) vi.findViewById(R.id.berat);
        TextView tinggi_badan = (TextView) vi.findViewById(R.id.tinggi);
        TextView no_register = (TextView) vi.findViewById(R.id.no_register);
        TextView nama_petugas = (TextView) vi.findViewById(R.id.nama_petugas);
        gambar=(ImageView) vi.findViewById(R.id.gambar);

        Pertumbuhan daftar_pertumbuhan = data_pertumbuhan.get(position);
        id_pertumbuhan.setText(daftar_pertumbuhan.getId_pertumbuhan());
        tanggal.setText(daftar_pertumbuhan.getTanggal());
        lingkar_kepala.setText(daftar_pertumbuhan.getLingkar_kepala());
        berat_badan.setText(daftar_pertumbuhan.getBerat_badan());
        tinggi_badan.setText(daftar_pertumbuhan.getTinggi_badan());
        no_register.setText(daftar_pertumbuhan.getNo_register());
        nama_petugas.setText(daftar_pertumbuhan.getNama_petugas());

        return vi;
    }
}
