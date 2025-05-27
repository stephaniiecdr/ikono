package application.service;

import application.Currency;
import application.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class HibernateCurrencyService implements CurrencyService {

    @Override
    public List<Currency> getAllCurrencies() {
        Transaction transaction = null;
        List<Currency> currencies = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<Currency> query = session.createQuery("FROM Currency ORDER BY curID", Currency.class);
            currencies = query.list();
            transaction.commit();
            return currencies;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Sebaiknya gunakan logger yang lebih baik
            return Collections.emptyList(); // Kembalikan list kosong jika gagal
        }
    }

    @Override
    public Currency findCurrencyById(String curID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Currency.class, curID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addCurrency(Currency currency) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Cek apakah ID sudah ada bisa dilakukan di sini atau di UI sebelum memanggil
            // Untuk kesederhanaan, kita asumsikan ID unik atau penanganan duplikat ada di UI/service layer yang lebih tinggi
            session.save(currency);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCurrency(Currency currency) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(currency);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCurrency(String curID) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Currency currencyToDelete = session.get(Currency.class, curID);
            if (currencyToDelete != null) {
                session.delete(currencyToDelete);
                transaction.commit();
                return true;
            }
            // Jika tidak ditemukan, bisa dianggap berhasil (tidak ada yang dihapus) atau gagal.
            // Di sini kita anggap gagal jika tidak ditemukan.
            if (transaction.isActive()) transaction.rollback(); // Jika tidak ditemukan, rollback
            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
