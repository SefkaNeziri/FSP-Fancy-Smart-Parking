namespace ApiGateway.Controllers;

using ApiGateway.DTOs;
using ApiGateway.Services;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/[controller]")]
public class NotificationsController : ControllerBase
{
    private readonly INotificationService notificationService;

    public NotificationsController(INotificationService notificationService)
    {
        this.notificationService = notificationService;
    }

    [HttpGet]
    public ActionResult<IEnumerable<NotificationDto>> GetNotifications([FromQuery] string email)
    {
        try
        {
            List<NotificationDto> result = notificationService.GetNotificationsByEmail(email);
            return Ok(result);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    [HttpPost("confirmation")]
    public ActionResult<NotificationDto> SendConfirmation([FromQuery] string email, [FromQuery] string parkingSpot)
    {
        try
        {
            NotificationDto created = notificationService.SendReservationConfirmation(email, parkingSpot);
            return Ok(created);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    [HttpPost("reminder")]
    public ActionResult<NotificationDto> SendReminder(
        [FromQuery] string email,
        [FromQuery] string parkingSpot,
        [FromQuery] int minutesLeft)
    {
        try
        {
            NotificationDto created = notificationService.SendExpirationReminder(email, parkingSpot, minutesLeft);
            return Ok(created);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }
}
