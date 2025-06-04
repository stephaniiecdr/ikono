package _UASS2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PembeliDAOTest {

    private static SessionFactory sessionFactory;
    private Session session;
    private PembeliDAO pembeliDAO;

    @BeforeAll
    static void setUpAll() {
        // The test hibernate.cfg.xml will be used automatically
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
        pembeliDAO = new PembeliDAO();
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void testSaveAndFindById() {
        Pembeli pembeli = new Pembeli("John Doe", "123 Main St", "Anytown", "12345", "555-0101", "john.doe@test.com", "Laki-laki", LocalDate.now(), "Aktif");
        pembeliDAO.save(pembeli);

        Pembeli found = pembeliDAO.findById(pembeli.getIdPembeli());
        assertNotNull(found);
        assertEquals("John Doe", found.getNamaLengkap());
    }

    @Test
    void testUpdate() {
        Pembeli pembeli = new Pembeli("Jane Doe", "456 Oak Ave", "Otherville", "54321", "555-0102", "jane.doe@test.com", "Perempuan", LocalDate.now(), "Aktif");
        pembeliDAO.save(pembeli);

        pembeli.setAlamat("789 Pine Ln");
        pembeliDAO.update(pembeli);

        Pembeli updated = pembeliDAO.findById(pembeli.getIdPembeli());
        assertEquals("789 Pine Ln", updated.getAlamat());
    }

    @Test
    void testDelete() {
        Pembeli pembeli = new Pembeli("Delete Me", "1 Erase St", "Nowhere", "00000", "555-0103", "delete.me@test.com", "Laki-laki", LocalDate.now(), "Tidak Aktif");
        pembeliDAO.save(pembeli);
        Integer id = pembeli.getIdPembeli();

        pembeliDAO.delete(pembeli);

        assertNull(pembeliDAO.findById(id));
    }
    
    @Test
    void testDeleteById() {
        Pembeli pembeli = new Pembeli("Delete Me By Id", "1 Erase St", "Nowhere", "00000", "555-0104", "delete.id@test.com", "Laki-laki", LocalDate.now(), "Tidak Aktif");
        pembeliDAO.save(pembeli);
        Integer id = pembeli.getIdPembeli();

        pembeliDAO.deleteById(id);

        assertNull(pembeliDAO.findById(id));
    }

    @Test
    void testFindAll() {
        pembeliDAO.save(new Pembeli("Alice", "Street 1", "City A", "11111", "555-1111", "alice@test.com", "Perempuan", LocalDate.now(), "Aktif"));
        pembeliDAO.save(new Pembeli("Bob", "Street 2", "City B", "22222", "555-2222", "bob@test.com", "Laki-laki", LocalDate.now(), "Aktif"));

        List<Pembeli> allPembeli = pembeliDAO.findAll();
        assertEquals(2, allPembeli.size());
    }
    
    @Test
    void testFindByNamaLengkap() {
        pembeliDAO.save(new Pembeli("Charlie Brown", "Street 3", "City C", "33333", "555-3333", "charlie@test.com", "Laki-laki", LocalDate.now(), "Aktif"));
        pembeliDAO.save(new Pembeli("Charles Xavier", "Street 4", "City D", "44444", "555-4444", "xavier@test.com", "Laki-laki", LocalDate.now(), "Aktif"));
        
        List<Pembeli> results = pembeliDAO.findByNamaLengkap("Char");
        assertEquals(2, results.size());
        
        List<Pembeli> results2 = pembeliDAO.findByNamaLengkap("Brown");
        assertEquals(1, results2.size());
        assertEquals("Charlie Brown", results2.get(0).getNamaLengkap());
    }
}