namespace ApiGateway.Controllers;

using ApiGateway.DTOs;
using ApiGateway.Services;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/driver")]
public class DriverGatewayController : ControllerBase
{
    private readonly HttpClient httpClient;
    private readonly INotificationService notificationService;

    public DriverGatewayController(HttpClient httpClient, INotificationService notificationService)
    {
        this.httpClient = httpClient;
        this.notificationService = notificationService;
    }

    [HttpPost("register")]
    public ActionResult<DriverRegistrationDto> RegisterDriver([FromBody] DriverRegistrationDto request)
    {
        try
        {
            // TODO: Forward to Java Driver Backend when ready
            return Ok(request);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    [HttpPost("reserve")]
    public ActionResult<ReservationDto> ReserveParking([FromBody] ReservationDto request)
    {
        try
        {
            // TODO: Forward reservation to Java Driver Backend via gRPC/REST when ready
            notificationService.SendReservationConfirmation(request.Email, request.ParkingSpot);

            return Ok(request);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    [HttpDelete("cancel")]
    public ActionResult CancelReservation([FromQuery] string email, [FromQuery] string parkingSpot)
    {
        try
        {
            // TODO: Forward cancel request to Java Driver Backend when ready
            notificationService.SendCancellationNotification(email, parkingSpot);
            return NoContent();
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }
}
