package org.example.service;

import org.example.model.Pegawai;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PegawaiService {

    private final String FILE_NAME = "pegawai.csv";
    private final List<Pegawai> data = new ArrayList<>();

    public PegawaiService() {
        loadData();
        sortById();
    }

    public List<Pegawai> getAll() {
        return data;
    }

    public void add(Pegawai p) {
        data.add(p);
        sortById();
        saveData();
    }

    public void update(int index, Pegawai p) {
        data.set(index, p);
        sortById();
        saveData();
    }

    public void delete(int index) {
        data.remove(index);
        saveData();
    }

    // ===== SORTING =====
    private void sortById() {
        data.sort((a, b) -> a.getId().compareTo(b.getId()));
    }

    //CSV SAVE
    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Pegawai p : data) {
                pw.println(
                        p.getId() + "," +
                                p.getNama() + "," +
                                p.getJabatan() + "," +
                                p.getGaji()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CSV LOAD
    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                data.add(new Pegawai(
                        d[0],
                        d[1],
                        d[2],
                        Integer.parseInt(d[3])
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
