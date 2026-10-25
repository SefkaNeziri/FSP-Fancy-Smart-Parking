namespace ApiGateway.Grpc;

using ApiGateway.DTOs;
using ApiGateway.Services;
using global::Grpc.Core;

public class ParkingGrpcService : ParkingGrpc.ParkingGrpcBase
{
    private readonly INotificationService notificationService;

    public ParkingGrpcService(INotificationService notificationService)
    {
        this.notificationService = notificationService;
    }

    // Handles reservation via gRPC and sends confirmation notification
    public override Task<ReservationReply> ReserveParking(ReservationRequest request, ServerCallContext context)
    {
        NotificationDto notification = notificationService.SendReservationConfirmation(
            request.Email,
            request.ParkingSpot);

        ReservationReply reply = new()
        {
            Success = true,
            Message = notification.Message
        };

        return Task.FromResult(reply);
    }

    // Sends expiration reminder via gRPC
    public override Task<NotificationReply> SendReminder(ReminderRequest request, ServerCallContext context)
    {
        NotificationDto notification = notificationService.SendExpirationReminder(
            request.Email,
            request.ParkingSpot,
            request.MinutesLeft);

        NotificationReply reply = new()
        {
            Id = notification.Id,
            Email = notification.Email,
            Message = notification.Message
        };

        return Task.FromResult(reply);
    }
}
