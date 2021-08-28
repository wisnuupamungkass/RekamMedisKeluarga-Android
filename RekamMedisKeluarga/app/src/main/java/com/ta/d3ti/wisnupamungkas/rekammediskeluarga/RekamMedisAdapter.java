package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import java.util.ArrayList;

import com.ta.d3ti.wisnupamungkas.rekammediskeluarga.RekamMedis;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RekamMedisAdapter extends BaseAdapter {
    protected Activity activity;
    private ArrayList<RekamMedis> data_rekam_medis=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public RekamMedisAdapter(Activity a, ArrayList<RekamMedis> d) {
        activity = a; data_rekam_medis = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data_rekam_medis.size();
    }
    public Object getItem(int position) {
        return data_rekam_medis.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_rekammedis, null);
        }
        TextView id_periksa = (TextView) vi.findViewById(R.id.id_periksa);
        TextView tanggal = (TextView) vi.findViewById(R.id.tanggal);
        TextView keluhan = (TextView) vi.findViewById(R.id.keluhan);
        TextView riwayat_penyakit = (TextView) vi.findViewById(R.id.riwayat_penyakit);
        TextView diagnosa = (TextView) vi.findViewById(R.id.diagnosa);
        TextView tindakan = (TextView) vi.findViewById(R.id.tindakan_medis);
        TextView obat = (TextView) vi.findViewById(R.id.obat);
        TextView no_ktp = (TextView) vi.findViewById(R.id.no_ktp);
        TextView no_register = (TextView) vi.findViewById(R.id.no_register);
        TextView id_petugas = (TextView) vi.findViewById(R.id.id_petugas);

        RekamMedis daftar_rekammedis = data_rekam_medis.get(position);
        id_periksa.setText(daftar_rekammedis.getId_periksa());
        tanggal.setText(daftar_rekammedis.getTanggal());
        keluhan.setText(daftar_rekammedis.getKeluhan());
        riwayat_penyakit.setText(daftar_rekammedis.getRiwayat_penyakit());
        diagnosa.setText(daftar_rekammedis.getDiagnosa());
        tindakan.setText(daftar_rekammedis.getTindakan_medis());
        obat.setText(daftar_rekammedis.getObat());
        no_ktp.setText(daftar_rekammedis.getNo_ktp());
        no_register.setText(daftar_rekammedis.getNo_register());
        id_petugas.setText(daftar_rekammedis.getNo_register());
        return vi;
    }
}
