
package fsp.driverFrontend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class DriverApiClient {

  private static final String BASE_URL =
      "http://localhost:5266/api/driver";

  private final HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(5))
      .build();

  public CompletableFuture<String> getNearbyFacilities(
      double latitude, double longitude) {

    String url = BASE_URL + "/facilities?lat="
        + latitude + "&lng=" + longitude;

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofSeconds(10))
        .GET()
        .build();

    return client.sendAsync(
        request,
        HttpResponse.BodyHandlers.ofString()
    ).thenApply(response -> {

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            "Gateway returned HTTP "
                + response.statusCode()
        );
      }

      return response.body();
    });
  }
}
