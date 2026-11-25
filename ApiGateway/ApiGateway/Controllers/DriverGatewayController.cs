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
    public async Task<ActionResult<DriverRegistrationDto>> RegisterDriver([FromBody] DriverRegistrationDto request)
    {
        try
        {
            try
            {
                string displayName = !string.IsNullOrWhiteSpace(request.FullName) ? request.FullName : request.UserName;
                var names = displayName.Split(' ', 2);
                string firstName = names.Length > 0 ? names[0] : "Driver";
                string lastName = names.Length > 1 ? names[1] : "User";

                var javaPayload = new
                {
                    email = request.Email,
                    password = request.Password,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = "+4500000000",
                    licensePlate = !string.IsNullOrWhiteSpace(request.LicensePlate) ? request.LicensePlate : "AB12345",
                    brand = "Standard",
                    model = "Car"
                };

                await httpClient.PostAsJsonAsync($"{DriverServiceUrl}/api/drivers/register", javaPayload);
            }
            catch (Exception ex)
            {
                // Non-blocking: Java driver-service may be offline during standalone testing
                Console.WriteLine($"[DriverGateway] Note: Java driver-service register forward: {ex.Message}");
            }

            return Ok(request);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    // Use Case 3: Login - verify credentials against Java driver-service database
    [HttpPost("login")]
    public async Task<ActionResult> LoginDriver([FromBody] LoginDto request)
    {
        try
        {
            var javaPayload = new { email = request.Email, password = request.Password };

            HttpResponseMessage response = await httpClient.PostAsJsonAsync(
                $"{DriverServiceUrl}/api/drivers/login", javaPayload);

            if (response.IsSuccessStatusCode)
            {
                string body = await response.Content.ReadAsStringAsync();
                return Ok(body);
            }

            return Unauthorized("Wrong email or password.");
        }
        catch (Exception e)
        {
            Console.WriteLine($"[DriverGateway] Login forward error: {e.Message}");
            return StatusCode(503, "Could not connect to driver database. Please try again.");
        }
    }

    [HttpPost("reserve")]
    public ActionResult<ReservationDto> ReserveParking([FromBody] ReservationDto request)
    {
        try
        {
            // TODO: Forward reservation to Java Driver Backend via gRPC/REST when ready
            notificationService.SendReservationConfirmation(request.Email, request.ParkingSpot, request.Hours);

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
