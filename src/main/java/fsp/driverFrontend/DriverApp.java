
package fsp.driverFrontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DriverApp extends Application {

  // JavaFX poziva start() kada se aplikacija pokrene.
  @Override
  public void start(Stage stage) throws Exception {

    // Ucitavamo Login ekran iz FXML fajla.
    FXMLLoader loader = new FXMLLoader(
        DriverApp.class.getResource("/login.fxml")
    );

    Scene scene = new Scene(loader.load());

    // Podesavamo glavni prozor aplikacije.
    stage.setTitle("Fancy Smart Parking");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  // Glavna metoda za pokretanje aplikacije.
  public static void main(String[] args) {
    launch(args);
  }
}
