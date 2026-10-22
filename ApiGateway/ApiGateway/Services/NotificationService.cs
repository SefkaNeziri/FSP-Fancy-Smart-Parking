namespace ApiGateway.Services;

using ApiGateway.DTOs;

public class NotificationService : INotificationService
{
    private readonly List<NotificationDto> notifications = new();

    public List<NotificationDto> GetNotificationsByEmail(string email)
    {
        return notifications
            .Where(n => n.Email.Equals(email, StringComparison.OrdinalIgnoreCase))
            .ToList();
    }

    // Use Case 5 (Trigger A): Potwierdzenie utworzenia rezerwacji
    public NotificationDto SendReservationConfirmation(string email, string parkingSpot)
    {
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

    // Use Case 6: Powiadomienie o anulowaniu rezerwacji
    public NotificationDto SendCancellationNotification(string email, string parkingSpot)
    {
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

    // Use Case 5 (Trigger B): Przypomnienie o konczacym sie czasie parkowania
    // TODO: Add automatic background timer when Driver Backend is connected
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
}
