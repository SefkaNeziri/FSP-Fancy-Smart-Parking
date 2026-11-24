
package fsp.driverFrontend.Controller;

import fsp.driverFrontend.DemoData;
import fsp.driverFrontend.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class NotificationsController {

  @FXML
  private ListView<String> notificationList;

  // Displays notifications from C# Gateway / local store for this driver.
  @FXML
  private void initialize() {

    // First attempt to fetch live notifications from C# Gateway
    java.util.List<String> remoteNotes =
        fsp.driverFrontend.ApiClient.getNotifications(DemoData.currentDriverEmail);

    if (remoteNotes != null && !remoteNotes.isEmpty()) {
      notificationList.getItems().setAll(remoteNotes);
      return;
    }

    // Fallback to local notification store
    String prefix = DemoData.currentDriverEmail + ":";

    notificationList.getItems().setAll(
        DemoData.notifications.stream()
            .filter(message -> message.startsWith(prefix))
            .toList()
    );

    if (notificationList.getItems().isEmpty()) {
      notificationList.getItems().add(
          "No notifications yet."
      );
    }
  }

  @FXML
  private void handleBack(ActionEvent event) {
    Navigator.goTo(event, "dashboard.fxml");
  }
}
