package com.example.pos.patterns.creational;

import com.example.pos.util.HibernateUtil; // Mengimpor kelas Singleton kita
import org.hibernate.SessionFactory;

public class SingletonPatternDemo {

    public static void main(String[] args) {
        System.out.println("--- Singleton Pattern Demo menggunakan HibernateUtil ---");

        // Mencoba mendapatkan instance SessionFactory beberapa kali
        System.out.println("Mendapatkan instance SessionFactory pertama...");
        SessionFactory sf1 = HibernateUtil.getSessionFactory();

        System.out.println("Mendapatkan instance SessionFactory kedua...");
        SessionFactory sf2 = HibernateUtil.getSessionFactory();

        System.out.println("Mendapatkan instance SessionFactory ketiga...");
        SessionFactory sf3 = HibernateUtil.getSessionFactory();

        // Memeriksa apakah semua referensi menunjuk ke objek yang sama
        if (sf1 == sf2 && sf2 == sf3) {
            System.out.println("\nSemua instance SessionFactory (sf1, sf2, sf3) adalah objek yang SAMA.");
            System.out.println("Ini menunjukkan bahwa pola Singleton bekerja dengan benar.");
            System.out.println("hashCode sf1: " + sf1.hashCode());
            System.out.println("hashCode sf2: " + sf2.hashCode());
            System.out.println("hashCode sf3: " + sf3.hashCode());
        } else {
            System.out.println("\nERROR: Instance SessionFactory tidak sama! Pola Singleton mungkin tidak bekerja.");
        }

        System.out.println("\n--- Demo Selesai ---");
    }
}
