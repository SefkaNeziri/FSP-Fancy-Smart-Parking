namespace ApiGateway.Services;

using ApiGateway.DTOs;

public interface INotificationService
{
    List<NotificationDto> GetNotificationsByEmail(string email);
    NotificationDto SendReservationConfirmation(string email, string parkingSpot, int hours = 1);
    NotificationDto SendCancellationNotification(string email, string parkingSpot);
    NotificationDto SendExpirationReminder(string email, string parkingSpot, int minutesLeft);
    void CheckExpiringReservations();
}
