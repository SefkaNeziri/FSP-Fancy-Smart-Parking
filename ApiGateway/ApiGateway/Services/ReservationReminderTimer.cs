namespace ApiGateway.Services;

using Microsoft.Extensions.Hosting;

public class ReservationReminderTimer : BackgroundService
{
    private readonly INotificationService notificationService;

    public ReservationReminderTimer(INotificationService notificationService)
    {
        this.notificationService = notificationService;
    }

    // Runs in the background every 30 seconds to check expiring reservations (Use Case 5 Trigger B)
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            notificationService.CheckExpiringReservations();
            await Task.Delay(TimeSpan.FromSeconds(30), stoppingToken);
        }
    }
}
