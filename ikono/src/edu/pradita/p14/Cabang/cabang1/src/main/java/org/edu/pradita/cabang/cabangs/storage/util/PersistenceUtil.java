//package org.edu.pradita.cabang.cabangs.storage.util;
//
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//
//public class PersistenceUtil {
//    private static volatile EntityManagerFactory entityManagerFactory; // volatile untuk thread-safety
//
//    // Constructor private biar gk instansiasi di luar
//    private PersistenceUtil() {}
//
//    public static EntityManagerFactory getEntityManagerFactory() {
//        if (entityManagerFactory == null) { // Check pertama (tanpa lock)
//            synchronized (PersistenceUtil.class) { // Blok synchronized
//                if (entityManagerFactory == null) { // Check kedua (dengan lock)
//                    try {
//                        entityManagerFactory = Persistence.createEntityManagerFactory("POS_PU_James");
//                    } catch (Throwable ex) {
//                        System.err.println("Gagal membuat EntityManagerFactory: " + ex);
//                        throw new ExceptionInInitializerError(ex);
//                    }
//                }
//            }
//        }
//        return entityManagerFactory;
//    }
//
//    public static void shutdown() {
//        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
//            entityManagerFactory.close();
//        }
//    }
//
//}
