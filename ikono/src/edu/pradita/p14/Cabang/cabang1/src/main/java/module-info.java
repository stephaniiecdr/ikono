module org.edu.pradita.cabang.cabangs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    requires org.controlsfx.controls;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires net.bytebuddy;
    requires java.sql;
    requires org.slf4j;
    requires org.jboss.logging;
    requires jakarta.xml.bind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.classmate;
    requires jakarta.transaction;
    requires jakarta.interceptor;

    opens org.edu.pradita.cabang.cabangs to javafx.fxml, org.hibernate.orm.core;
    opens org.edu.pradita.cabang.cabangs.Controller to javafx.fxml;


    exports org.edu.pradita.cabang.cabangs;
    exports org.edu.pradita.cabang.cabangs.Controller;

}

