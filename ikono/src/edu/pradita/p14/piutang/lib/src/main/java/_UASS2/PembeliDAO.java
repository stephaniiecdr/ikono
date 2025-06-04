package _UASS2;

import _UASS2.Pembeli;
import _UASS2.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PembeliDAO implements GenericDAO<Pembeli, Integer> {
    
    @Override
    public void save(Pembeli pembeli) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.save(pembeli);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(Pembeli pembeli) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.update(pembeli);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(Pembeli pembeli) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.delete(pembeli);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            Pembeli pembeli = session.get(Pembeli.class, id);
            if (pembeli != null) {
                session.delete(pembeli);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public Pembeli findById(Integer id) {
        try (Session session = HibernateUtil.openSession()) {
            return session.get(Pembeli.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Pembeli> findAll() {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("FROM Pembeli", Pembeli.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Pembeli> findByProperty(String propertyName, Object value) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pembeli WHERE " + propertyName + " = :value";
            Query<Pembeli> query = session.createQuery(hql, Pembeli.class);
            query.setParameter("value", value);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Pembeli> findByNamaLengkap(String namaLengkap) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pembeli WHERE namaLengkap LIKE :nama";
            Query<Pembeli> query = session.createQuery(hql, Pembeli.class);
            query.setParameter("nama", "%" + namaLengkap + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}