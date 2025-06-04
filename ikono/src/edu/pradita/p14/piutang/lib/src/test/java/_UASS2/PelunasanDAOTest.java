package _UASS2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PelunasanDAOTest {

    private static SessionFactory sessionFactory;
    private Session session;
    private PelunasanDAO pelunasanDAO;
    private PiutangDAO piutangDAO;
    private PembeliDAO pembeliDAO;
    private Pembeli testPembeli;
    private Piutang testPiutang;

    @BeforeAll
    static void setUpAll() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        session = sessionFactory.openSession();
        session.beginTransaction();

        pelunasanDAO = new PelunasanDAO();
        piutangDAO = new PiutangDAO();
        pembeliDAO = new PembeliDAO();

        // Create a default pembeli and piutang for tests
        testPembeli = new Pembeli("Paying Customer", "1 Bank St", "Cashville", "54321", "555-PAY", "pay@customer.com", "Perempuan", LocalDate.now(), "Aktif");
        pembeliDAO.save(testPembeli);

        testPiutang = new Piutang(testPembeli, null, new BigDecimal("2500.00"), LocalDate.now(), LocalDate.now().plusDays(30));
        piutangDAO.save(testPiutang);
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void testSaveAndFindById() {
        Pelunasan pelunasan = new Pelunasan(testPiutang, new BigDecimal("500.00"), LocalDate.now());
        pelunasanDAO.save(pelunasan);

        Pelunasan found = pelunasanDAO.findById(pelunasan.getIdPelunasan());
        assertNotNull(found);
        assertEquals(testPiutang.getIdPiutang(), found.getPiutang().getIdPiutang());
        assertEquals(0, new BigDecimal("500.00").compareTo(found.getJumlahBayar()));
    }

    @Test
    void testDeleteByPiutangId() {
        // Payments for the test piutang
        pelunasanDAO.save(new Pelunasan(testPiutang, new BigDecimal("100.00"), LocalDate.now()));
        pelunasanDAO.save(new Pelunasan(testPiutang, new BigDecimal("200.00"), LocalDate.now()));

        // Create another customer and piutang to ensure they are not deleted
        Pembeli otherPembeli = new Pembeli("Other Customer", "2 Other St", "Othertown", "98765", "555-OTH", "other@customer.com", "Laki-laki", LocalDate.now(), "Aktif");
        pembeliDAO.save(otherPembeli);
        Piutang otherPiutang = new Piutang(otherPembeli, null, new BigDecimal("1000.00"), LocalDate.now(), LocalDate.now().plusDays(10));
        piutangDAO.save(otherPiutang);
        pelunasanDAO.save(new Pelunasan(otherPiutang, new BigDecimal("50.00"), LocalDate.now()));

        // Action: Delete pelunasan related to testPiutang
        pelunasanDAO.deleteByPiutangId(testPiutang.getIdPiutang());
        
        // Assert
        assertEquals(0, pelunasanDAO.findByPiutangId(testPiutang.getIdPiutang()).size());
        assertEquals(1, pelunasanDAO.findByPiutangId(otherPiutang.getIdPiutang()).size());
    }

    @Test
    void getTotalPaymentByPiutang() {
        pelunasanDAO.save(new Pelunasan(testPiutang, new BigDecimal("300.00"), LocalDate.now()));
        pelunasanDAO.save(new Pelunasan(testPiutang, new BigDecimal("450.50"), LocalDate.now()));
        
        BigDecimal total = pelunasanDAO.getTotalPaymentByPiutang(testPiutang.getIdPiutang());
        
        assertEquals(0, new BigDecimal("750.50").compareTo(total));
    }
}
