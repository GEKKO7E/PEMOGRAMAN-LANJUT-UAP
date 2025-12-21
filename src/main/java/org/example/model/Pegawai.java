package org.example.model;

public class Pegawai {

    private String id;
    private String nama;
    private String jabatan;
    private int gaji;

    public Pegawai(String id, String nama, String jabatan, int gaji) {
        this.id = id;
        this.nama = nama;
        this.jabatan = jabatan;
        this.gaji = gaji;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJabatan() {
        return jabatan;
    }
    public int getGaji() {
        return gaji;
    }
}
