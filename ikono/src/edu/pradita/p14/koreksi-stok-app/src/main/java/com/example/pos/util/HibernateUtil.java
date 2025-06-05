package com.example.pos.util;

import com.example.pos.model.KoreksiStok; // Import entitas
import com.example.pos.model.Produk;     // Import entitas
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            System.out.println("HibernateUtil: Mencoba membuat SessionFactory...");
            try {
                Configuration configuration = new Configuration().configure(); // Memuat hibernate.cfg.xml

                if (configuration != null) {
                    System.out.println("HibernateUtil: Konfigurasi Hibernate (hibernate.cfg.xml) berhasil dimuat.");
                } else {
                    System.err.println("HibernateUtil: GAGAL memuat konfigurasi Hibernate (hibernate.cfg.xml).");
                    throw new RuntimeException("Gagal memuat hibernate.cfg.xml");
                }

                // Bangun ServiceRegistry dari konfigurasi
                StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                System.out.println("HibernateUtil: ServiceRegistry berhasil dibuat.");

                // Gunakan MetadataSources untuk membangun Metadata
                // DAN TAMBAHKAN ENTITAS SECARA PROGRAMATIK DI SINI
                MetadataSources metadataSources = new MetadataSources(serviceRegistry);
                System.out.println("HibernateUtil: Menambahkan entitas secara programatik...");
                metadataSources.addAnnotatedClass(com.example.pos.model.Produk.class);
                metadataSources.addAnnotatedClass(com.example.pos.model.KoreksiStok.class);
                System.out.println("HibernateUtil: Entitas Produk dan KoreksiStok telah ditambahkan ke MetadataSources.");


                Metadata metadata = metadataSources.buildMetadata();

                // Cek entitas yang terdaftar
                Set<String> entityNames = metadata.getEntityBindings().stream()
                        .map(pc -> pc.getEntityName()) // pc adalah PersistentClass
                        .collect(Collectors.toSet());
                System.out.println("HibernateUtil: Entitas yang terdaftar oleh Hibernate: " + entityNames);

                if (!entityNames.contains("com.example.pos.model.Produk") && !entityNames.contains(Produk.class.getName())) {
                    System.err.println("HibernateUtil: PERINGATAN! Entitas Produk (com.example.pos.model.Produk) MASIH TIDAK TERDAFTAR.");
                } else {
                    System.out.println("HibernateUtil: Entitas Produk (com.example.pos.model.Produk) BERHASIL TERDAFTAR.");
                }
                if (!entityNames.contains("com.example.pos.model.KoreksiStok") && !entityNames.contains(KoreksiStok.class.getName())) {
                    System.err.println("HibernateUtil: PERINGATAN! Entitas KoreksiStok (com.example.pos.model.KoreksiStok) MASIH TIDAK TERDAFTAR.");
                } else {
                    System.out.println("HibernateUtil: Entitas KoreksiStok (com.example.pos.model.KoreksiStok) BERHASIL TERDAFTAR.");
                }


                sessionFactory = metadata.buildSessionFactory();
                System.out.println("HibernateUtil: SessionFactory berhasil dibuat.");

            } catch (Throwable ex) {
                System.err.println("HibernateUtil: GAGAL membuat SessionFactory awal.");
                ex.printStackTrace();
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            System.out.println("HibernateUtil: Menutup SessionFactory...");
            getSessionFactory().close(); // Ambil instance yang ada untuk ditutup
            System.out.println("HibernateUtil: SessionFactory berhasil ditutup.");
        }
    }
}
