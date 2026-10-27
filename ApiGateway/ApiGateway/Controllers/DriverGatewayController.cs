namespace ApiGateway.Controllers;

using System.Globalization;
using System.Net.Http.Json;
using ApiGateway.DTOs;
using ApiGateway.Services;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/driver")]
public class DriverGatewayController : ControllerBase
{
    private const string DriverServiceUrl = "http://localhost:8080";

    private readonly HttpClient httpClient;
    private readonly INotificationService notificationService;

    public DriverGatewayController(HttpClient httpClient, INotificationService notificationService)
    {
        this.httpClient = httpClient;
        this.notificationService = notificationService;
    }

    // Use Case 1: Search nearby parking facilities from Java driver-service
    [HttpGet("facilities")]
    public async Task<ActionResult<IEnumerable<FacilityDto>>> FindNearbyFacilities(
        [FromQuery] double lat,
        [FromQuery] double lng)
    {
        try
        {
            string latStr = lat.ToString(CultureInfo.InvariantCulture);
            string lngStr = lng.ToString(CultureInfo.InvariantCulture);
            string url = $"{DriverServiceUrl}/api/facilities?lat={latStr}&lng={lngStr}";

            List<FacilityDto>? facilities = await httpClient.GetFromJsonAsync<List<FacilityDto>>(url);
            return Ok(facilities);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    // Use Case 1: Get single parking facility details from Java driver-service
    [HttpGet("facilities/{id:guid}")]
    public async Task<ActionResult<FacilityDto>> GetFacility([FromRoute] Guid id)
    {
        try
        {
            string url = $"{DriverServiceUrl}/api/facilities/{id}";
            FacilityDto? facility = await httpClient.GetFromJsonAsync<FacilityDto>(url);
            return Ok(facility);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    // Use Case 1: Get spots for a facility from Java driver-service
    [HttpGet("facilities/{id:guid}/spots")]
    public async Task<ActionResult<IEnumerable<ParkingSpotDto>>> GetFacilitySpots(
        [FromRoute] Guid id,
        [FromQuery] string? status = null)
    {
        try
        {
            string url = $"{DriverServiceUrl}/api/facilities/{id}/spots";
            if (!string.IsNullOrWhiteSpace(status))
            {
                url += $"?status={status}";
            }

            List<ParkingSpotDto>? spots = await httpClient.GetFromJsonAsync<List<ParkingSpotDto>>(url);
            return Ok(spots);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
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
