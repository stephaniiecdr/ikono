package org.edu.pradita.main.user;

import org.edu.pradita.main.presenter.PenjualanHarianPresenter; 
import org.edu.pradita.main.repository.HibernateSalesReportRepository;
import org.edu.pradita.main.repository.SalesReportRepository;
import org.edu.pradita.main.view.PenjualanHarianView;
import org.edu.pradita.main.util.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class PenjualanHarianApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        PenjualanHarianView view = new PenjualanHarianView();
        SalesReportRepository repository = new HibernateSalesReportRepository();
        new PenjualanHarianPresenter(view, repository);

        primaryStage.setTitle("Report Penjualan Harian");
        primaryStage.setScene(view.getScene());
        primaryStage.setOnCloseRequest(event -> HibernateUtil.shutdown());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Application stopped and Hibernate shutdown if not already handled.");
    }
}