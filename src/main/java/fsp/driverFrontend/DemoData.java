
package fsp.driverFrontend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DemoData {

  // Email trenutno prijavljenog korisnika.
  public static String currentDriverEmail = "";

  // Testna parking mesta dostupna u demo rezimu.
  public static final List<ParkingSpot> spots = List.of(
      new ParkingSpot("A1", "City Center", 2.50),
      new ParkingSpot("A2", "City Center", 2.50),
      new ParkingSpot("B1", "Shopping Mall", 1.80),
      new ParkingSpot("B2", "Shopping Mall", 1.80),
      new ParkingSpot("C1", "University", 1.20),
      new ParkingSpot("C2", "University", 1.20)
  );

  // Rezervacije postoje samo dok je aplikacija otvorena.
  public static final List<Reservation> reservations =
      new ArrayList<>();

  // Demo obavestenja za trenutnog korisnika.
  public static final List<String> notifications =
      new ArrayList<>();

  // Format datuma koji prikazujemo korisniku.
  private static final DateTimeFormatter FORMAT =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

  // Predstavlja jedno parking mesto.
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

  // Predstavlja jednu rezervaciju vozaca.
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

    // Ukupna cena zavisi od broja sati.
    public double getTotalPrice() {
      return spot.pricePerHour * hours;
    }

    // Kraj rezervacije racunamo iz pocetka i trajanja.
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

  // Vraca rezervacije samo trenutno prijavljenog vozaca.
  public static List<Reservation> myReservations() {

    return reservations.stream()
        .filter(r -> r.driverEmail.equals(currentDriverEmail))
        .toList();
  }

  // Proverava preklapanje termina za isto parking mesto.
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

      // Dva termina se preklapaju ako novi pocinje
      // pre kraja starog i zavrsava se posle njegovog pocetka.
      boolean overlaps =
          start.isBefore(r.getEnd())
              && end.isAfter(r.start);

      if (overlaps) {
        return false;
      }
    }

    return true;
  }

  // Kreira rezervaciju ako je mesto slobodno.
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

  // Brise sesiju korisnika, ali zadrzava demo rezervacije.
  public static void logout() {
    currentDriverEmail = "";
  }
}
