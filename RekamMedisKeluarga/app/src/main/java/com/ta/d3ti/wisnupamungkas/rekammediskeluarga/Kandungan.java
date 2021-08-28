package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

public class Kandungan {
    private String id_kandungan;
    private String kandungan_ke;
    private String no_ktp;

    public void setId_kandungan(String id_kandungan){
        this.id_kandungan =id_kandungan;
    }

    public String getId_kandungan(){
        return id_kandungan;
    }

    public void setKandungan_ke(String kandungan_ke){
        this.kandungan_ke=kandungan_ke;
    }

    public String getKandungan_ke(){
        return kandungan_ke;
    }

    public void setNo_ktp(String no_ktp){
        this.no_ktp=no_ktp;
    }

    public String getNo_ktp(){
        return  no_ktp;
    }
}
