
package fsp.driverFrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigator {

  // Private constructor: We don't create objects of this class.
  private Navigator() {
  }

  // Changes the current FXML screen when the user clicks a button.
  public static void goTo(ActionEvent event, String fxmlFile) {

    try {
      // Finds the window through the button that was clicked.
      Node source = (Node) event.getSource();
      Stage stage = (Stage) source.getScene().getWindow();

      // Loads the new screen.
      FXMLLoader loader = new FXMLLoader(
          Navigator.class.getResource("/" + fxmlFile)
      );

      Parent root = loader.load();

      // Uses the same window without opening new windows.
      stage.getScene().setRoot(root);

    } catch (IOException e) {

      // Displays the exact error in the terminal if the FXML file does not exist.
      e.printStackTrace();

      throw new IllegalStateException(
          "Cannot open screen: " + fxmlFile, e
      );
    }
  }
}
