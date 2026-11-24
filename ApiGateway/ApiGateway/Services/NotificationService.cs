namespace ApiGateway.Services;

using ApiGateway.DTOs;

public class NotificationService : INotificationService
{
    private readonly List<NotificationDto> notifications = new();
    private readonly List<ReservationDto> activeReservations = new();

    public List<NotificationDto> GetNotificationsByEmail(string email)
    {
        return notifications
            .Where(n => n.Email.Equals(email, StringComparison.OrdinalIgnoreCase))
            .ToList();
    }

    // Use Case 5 (Trigger A): Reservation confirmation and start tracking time
    public NotificationDto SendReservationConfirmation(string email, string parkingSpot, int hours = 1)
    {
        int durationHours = hours > 0 ? hours : 1;

        ReservationDto trackedReservation = new()
        {
            Id = activeReservations.Count + 1,
            Email = email,
            ParkingSpot = parkingSpot,
            Hours = durationHours,
            EndTime = DateTime.Now.AddHours(durationHours),
            ReminderSent = false
        };
        activeReservations.Add(trackedReservation);

        NotificationDto notification = new()
        {
            Id = notifications.Count + 1,
            Email = email,
            Message = $"Reservation successful for parking spot {parkingSpot}.",
            CreatedAt = DateTime.Now.ToString("dd.MM.yyyy HH:mm")
        };

        notifications.Add(notification);
        return notification;
    }

    // Use Case 6: Cancellation notification
    public NotificationDto SendCancellationNotification(string email, string parkingSpot)
    {
        activeReservations.RemoveAll(r =>
            r.Email.Equals(email, StringComparison.OrdinalIgnoreCase) &&
            r.ParkingSpot.Equals(parkingSpot, StringComparison.OrdinalIgnoreCase));

        NotificationDto notification = new()
        {
            Id = notifications.Count + 1,
            Email = email,
            Message = $"Reservation for parking spot {parkingSpot} was cancelled.",
            CreatedAt = DateTime.Now.ToString("dd.MM.yyyy HH:mm")
        };

        notifications.Add(notification);
        return notification;
    }

    // Use Case 5 (Trigger B): Expiration reminder
    public NotificationDto SendExpirationReminder(string email, string parkingSpot, int minutesLeft)
    {
        NotificationDto notification = new()
        {
            Id = notifications.Count + 1,
            Email = email,
            Message = $"Reminder: You have {minutesLeft} minutes left for parking spot {parkingSpot}.",
            CreatedAt = DateTime.Now.ToString("dd.MM.yyyy HH:mm")
        };

        notifications.Add(notification);
        return notification;
    }

    // Checks active reservations and sends reminder when 15 minutes or less remain
    public void CheckExpiringReservations()
    {
        foreach (ReservationDto reservation in activeReservations)
        {
            if (reservation.ReminderSent)
            {
                continue;
            }

            double minutesLeft = (reservation.EndTime - DateTime.Now).TotalMinutes;
            if (minutesLeft > 0 && minutesLeft <= 15)
            {
                int roundedMinutes = (int)Math.Ceiling(minutesLeft);
                SendExpirationReminder(reservation.Email, reservation.ParkingSpot, roundedMinutes);
                reservation.ReminderSent = true;
            }
        }
    }
}
