package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailKandunganAdapter extends BaseAdapter{
    private Activity activity;
    private ArrayList<DetailKandungan> data_detail_kandungan=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public DetailKandunganAdapter(Activity a, ArrayList<DetailKandungan> d) {
        activity = a; data_detail_kandungan = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data_detail_kandungan.size();
    }
    public Object getItem(int position) {
        return data_detail_kandungan.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null){
            vi = inflater.inflate(R.layout.list_item_detail_kandungan, null);
        }
        TextView id_detail = (TextView) vi.findViewById(R.id.id_detail);
        TextView tanggal_periksa = (TextView) vi.findViewById(R.id.tanggal_periksa);
        TextView bulan_hamil = (TextView) vi.findViewById(R.id.bulan_hamil);
        TextView kondisi_janin = (TextView) vi.findViewById(R.id.kondisi_janin);
        TextView kandungan_id = (TextView) vi.findViewById(R.id.kandungan_id);
        TextView petugas_id = (TextView) vi.findViewById(R.id.petugas_id);
        TextView foto = (TextView) vi.findViewById(R.id.foto_kandungan);

        DetailKandungan daftar_detail_kandungan = data_detail_kandungan.get(position);
        id_detail.setText(daftar_detail_kandungan.getId_detail());
        tanggal_periksa.setText(daftar_detail_kandungan.getTanggal_periksa());
        bulan_hamil.setText(daftar_detail_kandungan.getBulan_hamil());
        kondisi_janin.setText(daftar_detail_kandungan.getKondisi_janin());
        kandungan_id.setText(daftar_detail_kandungan.getKandungan_id());
        petugas_id.setText(daftar_detail_kandungan.getPetugas_id());
        foto.setText(daftar_detail_kandungan.getFoto());
        return vi;
    }
}
