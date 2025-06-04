package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    
    private static HibernateUtil instance; 

    
    private HibernateUtil() {
    }


    public static synchronized HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil(); 
        }
        return instance; 
    }

    public SessionFactory getSessionFactory() { 
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                throw new ExceptionInInitializerError("Initial SessionFactory creation failed" + e);
            }
        }
        return sessionFactory;
    }

    public void shutdown() { 
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
            sessionFactory = null; 
            instance = null; 
        }
    }
}
