package org.edu.pradita.cabang.cabangs.factory;

import org.edu.pradita.cabang.cabangs.dao.CabangDao;
import org.edu.pradita.cabang.cabangs.dao.CabangDaoImpl;
import org.edu.pradita.cabang.cabangs.service.CabangService;
import org.edu.pradita.cabang.cabangs.service.CabangServiceImpl;

public class ServiceFactory {

    private ServiceFactory() {}

    // Factory method BUAT CabangService
    public static CabangService createCabangService() {

        CabangDao cabangDao = new CabangDaoImpl();
        return new CabangServiceImpl(cabangDao);
    }
}
