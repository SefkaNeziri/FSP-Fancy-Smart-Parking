
package fsp.driverFrontend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DemoData {

  // Email address of the currently logged-in user.
  public static String currentDriverEmail = "";

  // Test parking spaces available in demo mode.
  public static final List<ParkingSpot> spots = List.of(
      new ParkingSpot("A1", "City Center", 2.50),
      new ParkingSpot("A2", "City Center", 2.50),
      new ParkingSpot("B1", "Shopping Mall", 1.80),
      new ParkingSpot("B2", "Shopping Mall", 1.80),
      new ParkingSpot("C1", "University", 1.20),
      new ParkingSpot("C2", "University", 1.20)
  );

  // Reservations exist only while the application is running.
  public static final List<Reservation> reservations =
      new ArrayList<>();

  // Demo notifications for the current user.
  public static final List<String> notifications =
      new ArrayList<>();

  // Date format displayed to the user.
  private static final DateTimeFormatter FORMAT =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

  // Represents a single parking space.
  public static class ParkingSpot {

    public final String id;
    public final String location;
    public final double pricePerHour;

    public ParkingSpot(
        String id,
        String location,
        double pricePerHour
    ) {
      this.id = id;
      this.location = location;
      this.pricePerHour = pricePerHour;
    }

    @Override
    public String toString() {
      return location + " - Spot " + id
          + " (€" + String.format("%.2f", pricePerHour)
          + "/hour)";
    }
  }

  // Represents a driver's reservation.
  public static class Reservation {

    public final int id;
    public final String driverEmail;
    public final ParkingSpot spot;
    public final LocalDateTime start;
    public final int hours;

    public boolean cancelled = false;
    public boolean paid = false;

    public Reservation(
        int id,
        String driverEmail,
        ParkingSpot spot,
        LocalDateTime start,
        int hours
    ) {
      this.id = id;
      this.driverEmail = driverEmail;
      this.spot = spot;
      this.start = start;
      this.hours = hours;
    }

    // The total price depends on the number of hours.
    public double getTotalPrice() {
      return spot.pricePerHour * hours;
    }

    // Calculates the reservation end time based on the start time and duration.
    public LocalDateTime getEnd() {
      return start.plusHours(hours);
    }

    @Override
    public String toString() {

      String status = cancelled
          ? "CANCELLED"
          : paid ? "PAID" : "UNPAID";

      return "#" + id
          + " | " + spot.location
          + " | Spot " + spot.id
          + " | " + start.format(FORMAT)
          + " | " + hours + "h"
          + " | €" + String.format("%.2f", getTotalPrice())
          + " | " + status;
    }
  }

  // Returns reservations only for the currently logged-in driver.
  public static List<Reservation> myReservations() {

    return reservations.stream()
        .filter(r -> r.driverEmail.equals(currentDriverEmail))
        .toList();
  }

  // Checks for overlapping reservations for the same parking space.
  public static boolean isSpotAvailable(
      ParkingSpot spot,
      LocalDateTime start,
      int hours
  ) {

    LocalDateTime end = start.plusHours(hours);

    for (Reservation r : reservations) {

      if (r.cancelled || !r.spot.id.equals(spot.id)) {
        continue;
      }

      // Two time slots overlap if the new one starts
      // before the previous one ends and ends after it starts.
      boolean overlaps =
          start.isBefore(r.getEnd())
              && end.isAfter(r.start);

      if (overlaps) {
        return false;
      }
    }

    return true;
  }

  // Creates a reservation if the parking space is available.
  public static synchronized Reservation reserve(
      ParkingSpot spot,
      LocalDateTime start,
      int hours
  ) {

    if (!isSpotAvailable(spot, start, hours)) {
      return null;
    }

    Reservation reservation = new Reservation(
        reservations.size() + 1,
        currentDriverEmail,
        spot,
        start,
        hours
    );

    reservations.add(reservation);

    notifications.add(
        currentDriverEmail
            + ": Reservation #" + reservation.id
            + " was created successfully."
    );

    return reservation;
  }

  // Clears the user session but keeps the demo reservations.
  public static void logout() {
    currentDriverEmail = "";
  }
}
