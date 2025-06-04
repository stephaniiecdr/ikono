package _UASS2;

import _UASS2.Piutang;
import _UASS2.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PiutangDAO implements GenericDAO<Piutang, Integer> {
    
    @Override
    public void save(Piutang piutang) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.save(piutang);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(Piutang piutang) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.update(piutang);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(Piutang piutang) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.delete(piutang);
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
            Piutang piutang = session.get(Piutang.class, id);
            if (piutang != null) {
                session.delete(piutang);
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
    public Piutang findById(Integer id) {
        try (Session session = HibernateUtil.openSession()) {
            return session.get(Piutang.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Piutang> findAll() {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("FROM Piutang p JOIN FETCH p.pembeli", Piutang.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Piutang> findByProperty(String propertyName, Object value) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli WHERE p." + propertyName + " = :value";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            query.setParameter("value", value);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Custom methods for Piutang
    
    /**
     * Find all piutang by pembeli ID
     */
    public List<Piutang> findByPembeliId(Integer pembeliId) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli WHERE p.pembeli.idPembeli = :pembeliId";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            query.setParameter("pembeliId", pembeliId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find piutang by customer name (partial match)
     */
    public List<Piutang> findByNamaPelanggan(String namaPelanggan) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli pb WHERE pb.namaLengkap LIKE :nama";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            query.setParameter("nama", "%" + namaPelanggan + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find all unpaid piutang
     */
    public List<Piutang> findUnpaidPiutang() {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli WHERE p.statusLunas = false";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find all paid piutang
     */
    public List<Piutang> findPaidPiutang() {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli WHERE p.statusLunas = true";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find overdue piutang (past due date and not paid)
     */
    public List<Piutang> findOverduePiutang() {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli WHERE p.dueDate < :currentDate AND p.statusLunas = false";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            query.setParameter("currentDate", LocalDate.now());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find piutang by amount range
     */
    public List<Piutang> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli WHERE p.jumlah BETWEEN :minAmount AND :maxAmount";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            query.setParameter("minAmount", minAmount);
            query.setParameter("maxAmount", maxAmount);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find piutang by date range
     */
    public List<Piutang> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Piutang p JOIN FETCH p.pembeli WHERE p.tanggalPinjam BETWEEN :startDate AND :endDate";
            Query<Piutang> query = session.createQuery(hql, Piutang.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get total unpaid amount for a specific customer
     */
    public BigDecimal getTotalUnpaidAmountByPembeli(Integer pembeliId) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "SELECT COALESCE(SUM(p.jumlah), 0) FROM Piutang p WHERE p.pembeli.idPembeli = :pembeliId AND p.statusLunas = false";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("pembeliId", pembeliId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Get total unpaid amount across all customers
     */
    public BigDecimal getTotalUnpaidAmount() {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "SELECT COALESCE(SUM(p.jumlah), 0) FROM Piutang p WHERE p.statusLunas = false";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Update piutang status to paid
     */
    public void markAsPaid(Integer piutangId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            Piutang piutang = session.get(Piutang.class, piutangId);
            if (piutang != null) {
                piutang.setStatusLunas(true);
                session.update(piutang);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    /**
     * Reduce piutang amount (for partial payments)
     */
    public void reduceAmount(Integer piutangId, BigDecimal paymentAmount) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            Piutang piutang = session.get(Piutang.class, piutangId);
            if (piutang != null) {
                BigDecimal newAmount = piutang.getJumlah().subtract(paymentAmount);
                piutang.setJumlah(newAmount);
                
                // Mark as paid if amount is zero or negative
                if (newAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    piutang.setStatusLunas(true);
                    piutang.setJumlah(BigDecimal.ZERO);
                }
                
                session.update(piutang);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}