package com.pasien.ui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class pasienUi {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Manajemen Data Pasien");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        panel.add(new JLabel("Id Pasien"));
        panel.add(new JTextField());

        panel.add(new JLabel("Nama Lengkap"));
        panel.add(new JTextField());

        panel.add(new JLabel("Tanggal Lahir:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Jenis Kelamin"));
        panel.add(new JTextField());

        panel.add(new JLabel("Alamat"));
        panel.add(new JTextField());

        panel.add(new JLabel("Nomor Telepon"));
        panel.add(new JTextField());

        panel.add(new JLabel("Email"));
        panel.add(new JTextField());

        panel.add(new JLabel("Golongan darah"));
        panel.add(new JTextField());

        panel.add(new JLabel("Status Pernikahan"));
        panel.add(new JTextField());

        panel.add(new JLabel("Riwayat Penyakit"));
        panel.add(new JTextField());

        panel.add(new JLabel("Alergi"));
        panel.add(new JTextField());

        panel.add(new JLabel("Tanggal Registrasi"));
        panel.add(new JTextField());

        // Tambahkan field lain sesuai kebutuhan

        frame.add(panel);
        frame.setVisible(true);
    }   
}
