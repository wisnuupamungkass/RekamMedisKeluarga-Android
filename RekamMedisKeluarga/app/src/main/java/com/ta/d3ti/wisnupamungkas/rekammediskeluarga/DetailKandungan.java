package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

public class DetailKandungan {
    private String id_detail;
    private String tanggal_periksa;
    private String bulan_hamil;
    private String kondisi_janin;
    private String foto;
    private String kandungan_id;
    private String petugas_id;

    public void setId_detail (String id_detail)
    {
        this.id_detail = id_detail;
    }

    public String getId_detail()
    {
        return id_detail;
    }

    public void setTanggal_periksa (String tanggal_periksa)
    {
        this.tanggal_periksa = tanggal_periksa;
    }

    public String getTanggal_periksa()
    {
        return tanggal_periksa;
    }

    public void setBulan_hamil (String  bulan_hamil){
        this.bulan_hamil = bulan_hamil;
    }

    public String getBulan_hamil(){
        return bulan_hamil;
    }

    public void setKondisi_janin (String  kondisi_janin){
        this.kondisi_janin = kondisi_janin;
    }

    public String getKondisi_janin(){
        return kondisi_janin;
    }

    public void setFoto (String  foto){
        this.foto = foto;
    }

    public String getFoto(){
        return foto;
    }

    public void setKandungan_id (String  kandungan_id){
        this.kandungan_id = kandungan_id;
    }

    public String getKandungan_id(){
        return kandungan_id;
    }

    public void setPetugas_id (String  kandungan_id){
        this.petugas_id = petugas_id;
    }

    public String getPetugas_id(){
        return petugas_id;
    }


}
