package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

public class Anak {

    private String no_register;
    private String nama;
    private String tgl_lahir;
    private String jam_lahir;
    private String bulan;
    private String gender;
    private String berat_badan;
    private String panjang_badan;
    private String lingkar_kepala;
    private String ktp_no;
    private String gizi;
    private String imunisasi;
    private String token_no;
    private String gambar;


    public void setAnakId (String no_register)
    {
        this.no_register = no_register;
    }

    public String getAnakId()
    {
        return no_register;
    }

    public void setAnakNama (String nama)
    {
        this.nama = nama;
    }

    public String getAnakNama()
    {
        return nama;
    }

    public void setAnakTgl (String tgl_lahir)
    {
        this.tgl_lahir = tgl_lahir;
    }

    public String getAnakTgl()
    {
        return tgl_lahir;
    }

    public void setAnakFoto (String gambar)
    {
        this.gambar = gambar;
    }

    public String getGambar() { return gambar; }

    public void setAnakJam (String jam_lahir)
    {
        this.jam_lahir = jam_lahir;
    }

    public String getJam()
    {
        return jam_lahir;
    }

    public void setBulan (String bulan)
    {
        this.bulan = bulan;
    }

    public String getBulan()
    {
        return bulan;
    }

    public void setAnakJk (String gender)
    {
        this.gender = gender;
    }

    public String getJk()
    {
        return gender;
    }

    public void setAnakBerat (String berat_badan)
    {
        this.berat_badan = berat_badan;
    }

    public String getBerat()
    {
        return berat_badan;
    }

    public void setAnakPanjang (String panjang_badan)
    {
        this.panjang_badan = panjang_badan;
    }

    public String getPanjang()
    {
        return panjang_badan;
    }

    public void setAnakLingkar (String lingkar_kepala)
    {
        this.lingkar_kepala = lingkar_kepala;
    }

    public String getLingkar()
    {
        return lingkar_kepala;
    }

    public void setAnakKtp (String ktp_no)
    {
        this.ktp_no = ktp_no;
    }

    public String getKtp()
    {
        return ktp_no;
    }

    public void setAnakGizi (String gizi)
    {
        this.gizi = gizi;
    }

    public String getGizi()
    {
        return gizi;
    }

    public void setAnakImunisasi (String imunisasi)
    {
        this.imunisasi = imunisasi;
    }

    public String getImunisasi()
    {
        return imunisasi;
    }

    public void setAnakToken (String token_no)
    {
        this.token_no = token_no;
    }

    public String getToken()
    {
        return token_no;
    }
}

