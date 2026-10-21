
package fsp.driverFrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigator {

  // Privatni konstruktor: ne pravimo objekte ove klase.
  private Navigator() {
  }

  // Menja trenutni FXML ekran kada korisnik klikne dugme.
  public static void goTo(ActionEvent event, String fxmlFile) {

    try {
      // Pronalazimo prozor preko dugmeta koje je kliknuto.
      Node source = (Node) event.getSource();
      Stage stage = (Stage) source.getScene().getWindow();

      // Ucitavamo novi ekran.
      FXMLLoader loader = new FXMLLoader(
          Navigator.class.getResource("/" + fxmlFile)
      );

      Parent root = loader.load();

      // Koristimo isti prozor, bez otvaranja novih prozora.
      stage.getScene().setRoot(root);

    } catch (IOException e) {

      // U terminalu vidimo tacnu gresku ako FXML ne postoji.
      e.printStackTrace();

      throw new IllegalStateException(
          "Cannot open screen: " + fxmlFile, e
      );
    }
  }
}
