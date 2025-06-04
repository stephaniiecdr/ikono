module org.edu.pradita.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jakarta.persistence;
    requires jakarta.transaction;
    requires jakarta.cdi;
    requires jakarta.xml.bind;
    requires com.fasterxml.classmate;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires java.naming;
    requires java.sql;
    requires org.jboss.logging;

    opens org.edu.pradita.main.view to javafx.fxml;
    opens org.edu.pradita.main.model.dto to javafx.base;
    opens org.edu.pradita.main.model to org.hibernate.orm.core;
    opens org.edu.pradita.main.user to javafx.fxml, javafx.graphics;
    exports org.edu.pradita.main.user;
}