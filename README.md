# Sistem Manajemen Data Pegawai

## 📌 Deskripsi
Sistem Manajemen Data Pegawai adalah aplikasi desktop berbasis Java Swing yang dirancang untuk mengelola data pegawai secara terstruktur dan efisien. Aplikasi ini mendukung proses login pengguna, manajemen data pegawai (CRUD), serta laporan statistik pegawai yang terintegrasi langsung dengan data sistem.

Aplikasi ini menerapkan konsep pemisahan tanggung jawab (layered architecture) sehingga logika bisnis, tampilan antarmuka, dan model data saling terpisah dengan baik.

---

## 🔗 Repository Project
GitHub Repository:
https://github.com/GEKKO7E/PEMOGRAMAN-LANJUT-UAP

---

## 🎯 Tujuan Aplikasi
- Memudahkan pengelolaan data pegawai
- Menyediakan tampilan UI yang modern dan user-friendly
- Menampilkan laporan ringkasan data pegawai secara real-time
- Menjadi media pembelajaran penerapan Java Swing dan konsep MVC

---

## 🧩 Fitur Utama

### 1. Login Sistem
- Autentikasi pengguna sebelum masuk ke sistem
- Validasi username dan password
- Pesan kesalahan jika kredensial salah

### 2. Dashboard
- Halaman utama setelah login
- Navigasi ke fitur sistem
- Menu yang tersedia:
    - Manajemen Pegawai
    - Laporan Pegawai
    - Logout Sistem

### 3. Manajemen Data Pegawai (CRUD)
- Menambah data pegawai
- Mengubah data pegawai
- Menghapus data pegawai
- Validasi input dan exception handling
- Menampilkan data pegawai dalam bentuk daftar modern

### 4. Laporan Pegawai
- Menampilkan ringkasan data pegawai secara otomatis
- Informasi yang ditampilkan:
    - Total pegawai
    - Total gaji pegawai
    - Rata-rata gaji pegawai
- Data laporan selalu sinkron dengan hasil CRUD

---

## 🏗️ Arsitektur Aplikasi
Aplikasi ini menerapkan konsep MVC (Model – View – Controller):

Model:
- Pegawai (merepresentasikan data pegawai)

Service / Business Logic:
- PegawaiService (mengelola data pegawai: tambah, ubah, hapus, dan tampilkan data)

View (UI):
- LoginUI
- DashboardUI
- SistemKepegawaianUI
- LaporanPegawaiUI

Pendekatan ini memastikan aplikasi mudah dikembangkan dan dipelihara.

---

## 📂 Struktur Package

org.example
│
├── model
│   └── Pegawai.java
│
├── service
│   └── PegawaiService.java
│
└── ui
├── LoginUI.java
├── DashboardUI.java
├── SistemKepegawaianUI.java
└── LaporanPegawaiUI.java

---

## 🚀 Cara Menjalankan Aplikasi
1. Pastikan Java JDK sudah terpasang
2. Buka project menggunakan IDE (IntelliJ IDEA / NetBeans)
3. Jalankan file LoginUI.java
4. Masukkan username dan password
5. Gunakan dashboard untuk mengakses fitur sistem

---

## 🔐 Kredensial Login (Default)
Username : Danish  
Password : 12345

---

## 📊 Catatan Teknis
- Aplikasi dibangun menggunakan Java Swing
- Tidak menggunakan library eksternal
- Data pegawai disimpan sementara (in-memory)
- Laporan pegawai mengambil data langsung dari PegawaiService
- Validasi input dan exception handling diterapkan untuk mencegah kesalahan

---

## 📚 Kesimpulan
Sistem Manajemen Data Pegawai merupakan aplikasi desktop yang sederhana namun fungsional. Dengan fitur CRUD, dashboard interaktif, serta laporan pegawai real-time, aplikasi ini cocok digunakan sebagai proyek akademik untuk memahami konsep OOP, Java Swing, dan arsitektur MVC.

---

## 👥 Tim Pengembang
- Maulana Rayhan Zulkarnaen (170)
- Danishwara Eka Putra Widiyanto (181)
