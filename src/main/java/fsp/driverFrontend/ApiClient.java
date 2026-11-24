package fsp.driverFrontend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * HTTP client connecting the JavaFX Driver Frontend to the C# API Gateway and Notification Service.
 */
public class ApiClient {

  private static final String GATEWAY_URL = "http://localhost:5266";

  private static final HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(3))
      .build();

  /**
   * Registers a driver account via the C# Gateway.
   */
  public static boolean register(String name, String email, String password) {
    try {
      String json = String.format(
          "{\"fullName\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
          escape(name), escape(email), escape(password)
      );

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(GATEWAY_URL + "/api/driver/register"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .timeout(Duration.ofSeconds(3))
          .build();

      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString()
      );

      return response.statusCode() >= 200 && response.statusCode() < 300;
    } catch (Exception e) {
      System.out.println("[ApiClient] Gateway offline or unreachable: " + e.getMessage());
      return false;
    }
  }

  /**
   * Sends a reservation request to the C# Gateway to trigger confirmation and reminder timer.
   */
  public static boolean reserve(String email, String spotId, int hours) {
    try {
      String json = String.format(
          "{\"email\":\"%s\",\"parkingSpot\":\"%s\",\"hours\":%d}",
          escape(email), escape(spotId), hours
      );

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(GATEWAY_URL + "/api/driver/reserve"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .timeout(Duration.ofSeconds(3))
          .build();

      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString()
      );

      return response.statusCode() >= 200 && response.statusCode() < 300;
    } catch (Exception e) {
      System.out.println("[ApiClient] Gateway offline or unreachable: " + e.getMessage());
      return false;
    }
  }

  /**
   * Sends a cancellation request to the C# Gateway to trigger cancellation notification.
   */
  public static boolean cancelReservation(String email, String spotId) {
    try {
      String url = String.format(
          "%s/api/driver/cancel?email=%s&parkingSpot=%s",
          GATEWAY_URL, escape(email), escape(spotId)
      );

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .DELETE()
          .timeout(Duration.ofSeconds(3))
          .build();

      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString()
      );

      return response.statusCode() >= 200 && response.statusCode() < 300;
    } catch (Exception e) {
      System.out.println("[ApiClient] Gateway offline or unreachable: " + e.getMessage());
      return false;
    }
  }

  /**
   * Fetches active notifications from the C# Notification Service.
   */
  public static List<String> getNotifications(String email) {
    List<String> result = new ArrayList<>();
    try {
      String url = GATEWAY_URL + "/api/Notifications";
      if (email != null && !email.isBlank()) {
        url += "/" + email;
      }

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .GET()
          .timeout(Duration.ofSeconds(3))
          .build();

      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString()
      );

      if (response.statusCode() == 200) {
        String body = response.body();
        int index = 0;
        while ((index = body.indexOf("\"message\":\"", index)) != -1) {
          int start = index + 11;
          int end = body.indexOf("\"", start);
          if (end != -1) {
            result.add(body.substring(start, end));
            index = end;
          } else {
            break;
          }
        }
      }
    } catch (Exception e) {
      System.out.println("[ApiClient] Gateway offline, cannot fetch notifications: " + e.getMessage());
    }
    return result;
  }

  private static String escape(String input) {
    if (input == null) return "";
    return input.replace("\"", "\\\"");
  }
}
