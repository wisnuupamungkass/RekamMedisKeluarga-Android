package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

public class Pertumbuhan {
    private String id_pertumbuhan;
    private String tanggal;
    private String lingkar_kepala;
    private String berat_badan;
    private String tinggi_badan;
    private String no_register;
    private String nama_petugas;

    public void setId_pertumbuhan (String id_pertumbuhan)
    {
        this.id_pertumbuhan = id_pertumbuhan;
    }

    public String getId_pertumbuhan()
    {
        return id_pertumbuhan;
    }

    public void setTanggal (String tanggal)
    {
        this.tanggal = tanggal;
    }

    public String getTanggal()
    {
        return tanggal;
    }

    public void setLingkar_kepala (String lingkar_kepala){this.lingkar_kepala = lingkar_kepala;}

    public String getLingkar_kepala(){return  lingkar_kepala;}

    public void setBerat_badan (String berat_badan){this.berat_badan = berat_badan;}

    public String getBerat_badan(){return  berat_badan;}

    public void setTinggi_badan (String tinggi_badan){this.tinggi_badan = tinggi_badan;}

    public String getTinggi_badan(){return  tinggi_badan;}

    public void setNo_register (String no_register){this.no_register = no_register;}

    public String getNo_register(){return  no_register;}

    public void setNama_petugas (String nama_petugas){this.nama_petugas = nama_petugas;}

    public String getNama_petugas(){return  nama_petugas;}

}
