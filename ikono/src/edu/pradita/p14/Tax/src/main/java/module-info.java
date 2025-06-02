module org.edu.pradita.pos.tax {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Untuk Hibernate
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires net.bytebuddy;
    requires com.fasterxml.classmate;
    requires org.jboss.logging;
    requires org.glassfish.jaxb.runtime;
    requires jakarta.transaction;
    requires jakarta.cdi;
    requires org.hibernate.commons.annotations;


//    uses jakarta.persistence.spi.PersistenceProvider;
//    provides jakarta.persistence.spi.PersistenceProvider with org.hibernate.jpa.HibernatePersistenceProvider;

    // Karena org.edu.pradita.pos.tax adalah root, maka Main dan HibernateUtil ada di dalamnya
    opens edu.pradita.p14.Tax to javafx.fxml, javafx.graphics;

    // MEMBUKA PAKET CONTROLLER KE JAVAFX.FXML
    // Jalurnya sekarang relatif terhadap org.edu.pradita.pos.tax
    opens edu.pradita.p14.Tax.controller to javafx.fxml;
    // Paket tax (yang berisi entitas Tax)
    opens edu.pradita.p14.Tax.tax to org.hibernate.orm.core, javafx.base;

    // Mengekspor paket utama agar dapat diakses modul lain
    exports edu.pradita.p14.Tax;
    exports edu.pradita.p14.Tax.controller;
    exports edu.pradita.p14.Tax.tax; // Ekspor paket tax (untuk Tax.java, TaxRepository.java)
    exports edu.pradita.p14.Tax.service; // Ekspor paket service
    exports edu.pradita.p14.Tax.decorator;
}
