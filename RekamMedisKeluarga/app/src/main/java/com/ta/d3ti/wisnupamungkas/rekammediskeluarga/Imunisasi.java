package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

public class Imunisasi {
    private String id_imunisasi;
    private String tanggal;
    private String no_register;
    private String nama_petugas;
    private String nama_imunisasi;

    public void setId_imunisasi (String id_imunisasi)
    {
        this.id_imunisasi = id_imunisasi;
    }

    public String getId_imunisasi()
    {
        return id_imunisasi;
    }

    public void setTanggal (String tanggal)
    {
        this.tanggal = tanggal;
    }

    public String getTanggal()
    {
        return tanggal;
    }

    public void setNo_register (String no_register)
    {
        this.no_register = no_register;
    }

    public String getNo_register()
    {
        return no_register;
    }

    public void setNama_petugas (String nama_petugas)
    {
        this.nama_petugas = nama_petugas;
    }

    public String getNama_petugas()
    {
        return nama_petugas;
    }

    public void setNama_imunisasi (String nama_imunisasi)
    {
        this.nama_imunisasi = nama_imunisasi;
    }

    public String getNama_imunisasi()
    {
        return nama_imunisasi;
    }
}