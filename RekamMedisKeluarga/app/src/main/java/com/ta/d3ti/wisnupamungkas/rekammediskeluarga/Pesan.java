package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

public class Pesan {
    private String id_pesan;
    private String tanggal;
    private String nama_anak;
    private String nama_orangtua;

    public void setId_pesan (String id_pesan)
    {
        this.id_pesan = id_pesan;
    }

    public String getId_pesan()
    {
        return id_pesan;
    }

    public void setNama_anak (String nama_anak)
    {
        this.nama_anak = nama_anak;
    }

    public String getNama_anak()
    {
        return nama_anak;
    }

    public void setTanggal (String tanggal)
    {
        this.tanggal = tanggal;
    }

    public String getTanggal()
    {
        return tanggal;
    }

    public void setNama_orangtua (String nama_orangtua)
    {
        this.nama_orangtua = nama_orangtua;
    }

    public String getNama_orangtua()
    {
        return nama_orangtua;
    }
}
