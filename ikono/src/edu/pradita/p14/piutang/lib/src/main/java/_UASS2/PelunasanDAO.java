package _UASS2;

import _UASS2.Pelunasan;
import _UASS2.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PelunasanDAO implements GenericDAO<Pelunasan, Integer> {
    
    @Override
    public void save(Pelunasan pelunasan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.save(pelunasan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(Pelunasan pelunasan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.update(pelunasan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(Pelunasan pelunasan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            session.delete(pelunasan);
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
            Pelunasan pelunasan = session.get(Pelunasan.class, id);
            if (pelunasan != null) {
                session.delete(pelunasan);
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
    public Pelunasan findById(Integer id) {
        try (Session session = HibernateUtil.openSession()) {
            return session.get(Pelunasan.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Pelunasan> findAll() {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli", Pelunasan.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Pelunasan> findByProperty(String propertyName, Object value) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli WHERE p." + propertyName + " = :value";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("value", value);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Custom methods for Pelunasan
    
    /**
     * Find all pelunasan by piutang ID
     */
    public List<Pelunasan> findByPiutangId(Integer piutangId) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli WHERE p.piutang.idPiutang = :piutangId";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("piutangId", piutangId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find all pelunasan by pembeli ID
     */
    public List<Pelunasan> findByPembeliId(Integer pembeliId) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli pb WHERE pb.idPembeli = :pembeliId";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("pembeliId", pembeliId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find pelunasan by customer name (partial match)
     */
    public List<Pelunasan> findByNamaPelanggan(String namaPelanggan) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli pb WHERE pb.namaLengkap LIKE :nama";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("nama", "%" + namaPelanggan + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find pelunasan by date range
     */
    public List<Pelunasan> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli WHERE p.tanggalBayar BETWEEN :startDate AND :endDate";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find pelunasan by payment amount range
     */
    public List<Pelunasan> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli WHERE p.jumlahBayar BETWEEN :minAmount AND :maxAmount";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("minAmount", minAmount);
            query.setParameter("maxAmount", maxAmount);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find pelunasan made today
     */
    public List<Pelunasan> findTodayPayments() {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli WHERE p.tanggalBayar = :today";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("today", LocalDate.now());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find pelunasan made this month
     */
    public List<Pelunasan> findThisMonthPayments() {
        try (Session session = HibernateUtil.openSession()) {
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli WHERE p.tanggalBayar BETWEEN :startDate AND :endDate";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("startDate", startOfMonth);
            query.setParameter("endDate", endOfMonth);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get total payment amount for a specific piutang
     */
    public BigDecimal getTotalPaymentByPiutang(Integer piutangId) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "SELECT COALESCE(SUM(p.jumlahBayar), 0) FROM Pelunasan p WHERE p.piutang.idPiutang = :piutangId";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("piutangId", piutangId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Get total payment amount for a specific customer
     */
    public BigDecimal getTotalPaymentByPembeli(Integer pembeliId) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "SELECT COALESCE(SUM(p.jumlahBayar), 0) FROM Pelunasan p JOIN p.piutang pt WHERE pt.pembeli.idPembeli = :pembeliId";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("pembeliId", pembeliId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Get total payment amount for a specific date
     */
    public BigDecimal getTotalPaymentByDate(LocalDate date) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "SELECT COALESCE(SUM(p.jumlahBayar), 0) FROM Pelunasan p WHERE p.tanggalBayar = :date";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("date", date);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Get total payment amount for a date range
     */
    public BigDecimal getTotalPaymentByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "SELECT COALESCE(SUM(p.jumlahBayar), 0) FROM Pelunasan p WHERE p.tanggalBayar BETWEEN :startDate AND :endDate";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Get total payments made today
     */
    public BigDecimal getTotalPaymentToday() {
        return getTotalPaymentByDate(LocalDate.now());
    }
    
    /**
     * Get total payments made this month
     */
    public BigDecimal getTotalPaymentThisMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return getTotalPaymentByDateRange(startOfMonth, endOfMonth);
    }
    
    /**
     * Get payment history for a specific piutang (ordered by date)
     */
    public List<Pelunasan> getPaymentHistory(Integer piutangId) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli WHERE p.piutang.idPiutang = :piutangId ORDER BY p.tanggalBayar DESC";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setParameter("piutangId", piutangId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get recent payments (last N payments)
     */
    public List<Pelunasan> getRecentPayments(int limit) {
        try (Session session = HibernateUtil.openSession()) {
            String hql = "FROM Pelunasan p JOIN FETCH p.piutang pt JOIN FETCH pt.pembeli ORDER BY p.tanggalBayar DESC";
            Query<Pelunasan> query = session.createQuery(hql, Pelunasan.class);
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Delete all pelunasan records for a specific piutang
     */
    public void deleteByPiutangId(Integer piutangId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.openSession()) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM Pelunasan p WHERE p.piutang.idPiutang = :piutangId";
            Query query = session.createQuery(hql);
            query.setParameter("piutangId", piutangId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}