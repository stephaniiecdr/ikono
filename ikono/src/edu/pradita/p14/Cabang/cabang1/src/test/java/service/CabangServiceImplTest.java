package service;

import org.edu.pradita.cabang.cabangs.dao.CabangDao;
import org.edu.pradita.cabang.cabangs.Cabang;
import org.edu.pradita.cabang.cabangs.service.CabangServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CabangServiceImplTest {
    @Mock // membuat objek tiruan untuk CabangDao
    private CabangDao mockCabangDao;

    @InjectMocks
    private CabangServiceImpl cabangService;

    private Cabang cabangContoh;

    @BeforeEach
    void setUp() {
        cabangContoh = new Cabang("Cabang Mock", "Alamat Mock", "12345");
        cabangContoh.setIdCabang(1L);
    }

    @Test
    void createCabang_suksesJikaNamaValidDanBelumAda() throws Exception {

        String namaBaru = "Cabang Baru";
        when(mockCabangDao.findByNama(namaBaru)).thenReturn(Optional.empty());

        Cabang result = cabangService.createCabang(namaBaru, "Alamat", "000");


        assertNotNull(result);
        assertEquals(namaBaru, result.getNamaCabang());
        verify(mockCabangDao, times(1)).save(any(Cabang.class));
        verify(mockCabangDao, times(1)).findByNama(namaBaru);
    }

    @Test
    void createCabang_gagalJikaNamaKosong() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cabangService.createCabang("", "Alamat", "000");
        });


        assertEquals("Nama cabang tidak boleh kosong.", exception.getMessage());
        verify(mockCabangDao, never()).save(any(Cabang.class));
    }

    @Test
    void createCabang_gagalJikaNamaSudahAda() {

        String namaSudahAda = "Cabang Ada";
        when(mockCabangDao.findByNama(namaSudahAda)).thenReturn(Optional.of(new Cabang(namaSudahAda, "", "")));

        Exception exception = assertThrows(Exception.class, () -> {
            cabangService.createCabang(namaSudahAda, "Alamat", "000");
        });
        assertTrue(exception.getMessage().contains("sudah ada"));
        verify(mockCabangDao, never()).save(any(Cabang.class));
    }

    @Test
    void getCabangById_adaJikaIdDitemukan() {

        when(mockCabangDao.findById(1L)).thenReturn(Optional.of(cabangContoh));

        Optional<Cabang> result = cabangService.getCabangById(1L);

        assertTrue(result.isPresent());
        assertEquals(cabangContoh.getNamaCabang(), result.get().getNamaCabang());
        verify(mockCabangDao, times(1)).findById(1L);
    }

    @Test
    void getCabangById_kosongJikaIdTidakDitemukan() {

        when(mockCabangDao.findById(99L)).thenReturn(Optional.empty());

        Optional<Cabang> result = cabangService.getCabangById(99L);

        assertFalse(result.isPresent());
        verify(mockCabangDao, times(1)).findById(99L);
    }

    @Test
    void getAllCabang_mengembalikanListCabang() {

        List<Cabang> listCabang = new ArrayList<>();
        listCabang.add(cabangContoh);
        listCabang.add(new Cabang("Cabang Mock 2", "Alamat 2", "67890"));
        when(mockCabangDao.findAll()).thenReturn(listCabang);


        List<Cabang> result = cabangService.getAllCabang();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockCabangDao, times(1)).findAll();
    }

    @Test
    void updateCabang_suksesJikaValid() throws Exception {

        Long idUpdate = 1L;
        String namaUpdate = "Nama Update";
        Cabang cabangExisting = new Cabang("Nama Lama", "Alamat Lama", "111");
        cabangExisting.setIdCabang(idUpdate);

        when(mockCabangDao.findById(idUpdate)).thenReturn(Optional.of(cabangExisting));

        when(mockCabangDao.findByNama(namaUpdate)).thenReturn(Optional.empty());

        Cabang result = cabangService.updateCabang(idUpdate, namaUpdate, "Alamat Update", "222");


        assertNotNull(result);
        assertEquals(namaUpdate, result.getNamaCabang());
        verify(mockCabangDao, times(1)).findById(idUpdate);
        verify(mockCabangDao, times(1)).findByNama(namaUpdate);
        verify(mockCabangDao, times(1)).update(any(Cabang.class));
    }

    @Test
    void updateCabang_gagalJikaIdTidakDitemukan() {

        Long idTidakAda = 99L;
        when(mockCabangDao.findById(idTidakAda)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            cabangService.updateCabang(idTidakAda, "Nama Update", "Alamat", "000");
        });
        assertTrue(exception.getMessage().contains("tidak ditemukan"));
        verify(mockCabangDao, never()).update(any(Cabang.class));
    }

    @Test
    void deleteCabang_suksesJikaIdDitemukan() throws Exception {

        Long idHapus = 1L;
        when(mockCabangDao.findById(idHapus)).thenReturn(Optional.of(cabangContoh));


        cabangService.deleteCabang(idHapus);

        verify(mockCabangDao, times(1)).findById(idHapus);
        verify(mockCabangDao, times(1)).deleteById(idHapus);
    }

    @Test
    void deleteCabang_gagalJikaIdTidakDitemukan() {

        Long idTidakAda = 99L;
        when(mockCabangDao.findById(idTidakAda)).thenReturn(Optional.empty());


        Exception exception = assertThrows(Exception.class, () -> {
            cabangService.deleteCabang(idTidakAda);
        });
        assertTrue(exception.getMessage().contains("tidak ditemukan untuk dihapus"));
        verify(mockCabangDao, never()).deleteById(anyLong());
    }
}
