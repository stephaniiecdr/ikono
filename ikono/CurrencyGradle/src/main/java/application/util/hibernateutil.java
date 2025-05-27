package application.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // 1. Instance statis privat dari SessionFactory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    // 2. Konstruktor privat untuk mencegah instansiasi dari luar (Kunci Singleton)
    private HibernateUtil() {
        // Mencegah utilitas kelas ini diinstansiasi
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Membuat SessionFactory dari hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // 3. Metode statis publik untuk mendapatkan instance SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            getSessionFactory().close();
            System.out.println("Hibernate SessionFactory shutdown.");
        }
    }
}
