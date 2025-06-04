package _UASS2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PiutangDAOTest {
    private static SessionFactory sessionFactory;
    private Session session;
    private PiutangDAO piutangDAO;
    private PembeliDAO pembeliDAO;
    private Pembeli testPembeli;

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
        piutangDAO = new PiutangDAO();
        pembeliDAO = new PembeliDAO();

        // Create a default pembeli for tests
        testPembeli = new Pembeli("Test Customer", "1 Test St", "Testville", "12345", "555-TEST", "test@customer.com", "Laki-laki", LocalDate.now(), "Aktif");
        pembeliDAO.save(testPembeli);
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void testSaveAndFindById() {
        Piutang piutang = new Piutang(testPembeli, null, new BigDecimal("1000.00"), LocalDate.now(), LocalDate.now().plusDays(30));
        piutangDAO.save(piutang);

        Piutang found = piutangDAO.findById(piutang.getIdPiutang());
        assertNotNull(found);
        assertEquals(0, new BigDecimal("1000.00").compareTo(found.getJumlah()));
        assertEquals(testPembeli.getIdPembeli(), found.getPembeli().getIdPembeli());
    }

    @Test
    void testReduceAmount() {
        Piutang piutang = new Piutang(testPembeli, null, new BigDecimal("500.00"), LocalDate.now(), LocalDate.now().plusDays(30));
        piutangDAO.save(piutang);
        
        piutangDAO.reduceAmount(piutang.getIdPiutang(), new BigDecimal("200.00"));
        
        Piutang updated = piutangDAO.findById(piutang.getIdPiutang());
        assertEquals(0, new BigDecimal("300.00").compareTo(updated.getJumlah()));
        assertFalse(updated.getStatusLunas());
    }

    @Test
    void testReduceAmountToZeroAndMarkAsPaid() {
        Piutang piutang = new Piutang(testPembeli, null, new BigDecimal("500.00"), LocalDate.now(), LocalDate.now().plusDays(30));
        piutangDAO.save(piutang);

        piutangDAO.reduceAmount(piutang.getIdPiutang(), new BigDecimal("500.00"));

        Piutang updated = piutangDAO.findById(piutang.getIdPiutang());
        assertEquals(0, BigDecimal.ZERO.compareTo(updated.getJumlah()));
        assertTrue(updated.getStatusLunas());
    }

    @Test
    void findByNamaPelanggan() {
        Piutang piutang = new Piutang(testPembeli, null, new BigDecimal("150.00"), LocalDate.now(), LocalDate.now().plusDays(30));
        piutangDAO.save(piutang);

        List<Piutang> results = piutangDAO.findByNamaPelanggan("Customer");
        assertEquals(1, results.size());
        assertEquals(testPembeli.getIdPembeli(), results.get(0).getPembeli().getIdPembeli());
    }

    @Test
    void findOverduePiutang() {
        // Overdue piutang
        Piutang overdue = new Piutang(testPembeli, null, new BigDecimal("100.00"), LocalDate.now().minusDays(60), LocalDate.now().minusDays(30));
        piutangDAO.save(overdue);

        // Not overdue piutang
        Piutang notOverdue = new Piutang(testPembeli, null, new BigDecimal("200.00"), LocalDate.now(), LocalDate.now().plusDays(30));
        piutangDAO.save(notOverdue);

        List<Piutang> overdues = piutangDAO.findOverduePiutang();
        assertEquals(1, overdues.size());
        assertEquals(overdue.getIdPiutang(), overdues.get(0).getIdPiutang());
    }
}