package org.edu.pradita.main.user;

import org.edu.pradita.main.presenter.PenjualanBulananPresenter;
import org.edu.pradita.main.repository.HibernateSalesReportRepository;
import org.edu.pradita.main.repository.SalesReportRepository;
import org.edu.pradita.main.view.PenjualanBulananView;
import org.edu.pradita.main.util.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class PenjualanBulananApp extends Application {

    public PenjualanBulananApp() {

    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        PenjualanBulananView view = new PenjualanBulananView();
        // Inisialisasi Repository (implementasi konkret)
        SalesReportRepository repository = new HibernateSalesReportRepository();
        // Inisialisasi Presenter dengan View dan Repository (Dependency Injection sederhana)
        new PenjualanBulananPresenter(view, repository); // Presenter akan meng-attach event

        primaryStage.setTitle("Report Penjualan Bulanan");
        primaryStage.setScene(view.getScene());
        primaryStage.setOnCloseRequest(event -> HibernateUtil.shutdown()); // Pastikan shutdown dipanggil
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // SessionFactory sudah di-handle oleh setOnCloseRequest atau bisa juga di sini
        // HibernateUtil.shutdown(); // Hati-hati double shutdown jika sudah ada di setOnCloseRequest
        super.stop();
        System.out.println("Application stopped and Hibernate shutdown initiated.");
    }
}