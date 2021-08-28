package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AnakAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Anak> data_anak=new ArrayList<>();
    protected ImageLoader imageLoader;
    ImageView gambar;
    private static LayoutInflater inflater = null;

    public AnakAdapter(Activity a, ArrayList<Anak> d) {
        activity = a; data_anak = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data_anak.size();
    }
    public Object getItem(int position) {
        return data_anak.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        View vi = convertView;
        if (convertView == null){
            vi = inflater.inflate(R.layout.list_item_anak, null);
        }
        TextView no_register = (TextView) vi.findViewById(R.id.no_register);
        TextView nama = (TextView) vi.findViewById(R.id.nama_anak);
        TextView tgl_lahir = (TextView) vi.findViewById(R.id.tgl_lahir_anak);
        TextView jam = (TextView) vi.findViewById(R.id.jam_lahir_anak);
        TextView bulan = (TextView) vi.findViewById(R.id.bulan);
        TextView jk = (TextView) vi.findViewById(R.id.jk_anak);
        TextView berat = (TextView) vi.findViewById(R.id.berat_badan);
        TextView panjang = (TextView) vi.findViewById(R.id.panjang_anak);
        TextView lingkar = (TextView) vi.findViewById(R.id.lingkar_kepala);
        TextView ktp = (TextView) vi.findViewById(R.id.ktp_no);
        TextView gizi = (TextView) vi.findViewById(R.id.gizi_anak);
        TextView imunisasi = (TextView) vi.findViewById(R.id.imunisasi_anak);
        TextView token_no = (TextView) vi.findViewById(R.id.token_no);
        TextView foto = (TextView) vi.findViewById(R.id.foto_anak);
        gambar=(ImageView) vi.findViewById(R.id.gambar);

        Anak daftar_anak = data_anak.get(position);
        no_register.setText(daftar_anak.getAnakId());
        nama.setText(daftar_anak.getAnakNama());
        tgl_lahir.setText(daftar_anak.getAnakTgl());
        jam.setText(daftar_anak.getJam());
        bulan.setText(daftar_anak.getBulan());
        jk.setText(daftar_anak.getJk());
        berat.setText(daftar_anak.getBerat());
        panjang.setText(daftar_anak.getPanjang());
        lingkar.setText(daftar_anak.getLingkar());
        ktp.setText(daftar_anak.getKtp());
        gizi.setText(daftar_anak.getGizi());
        imunisasi.setText(daftar_anak.getImunisasi());
        token_no.setText(daftar_anak.getToken());
        foto.setText(daftar_anak.getGambar());

        ImageLoader.getInstance().displayImage(daftar_anak.getGambar(),gambar);
        foto.setText(daftar_anak.getGambar());
        return vi;
    }
}
