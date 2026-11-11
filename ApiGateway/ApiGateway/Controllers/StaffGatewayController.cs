namespace ApiGateway.Controllers;

using System.Net.Http.Json;
using ApiGateway.DTOs;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/staff")]
public class StaffGatewayController : ControllerBase
{
    private const string StaffServiceUrl = "http://localhost:9090";

    private readonly HttpClient httpClient;

    public StaffGatewayController(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }

    // Use Case 8 & 9: Get expected license plate for a parking spot from Java staff backend
    [HttpGet("parking-spots/{parkingSpot}")]
    public async Task<ActionResult<string>> GetLicensePlate([FromRoute] string parkingSpot)
    {
        try
        {
            string url = $"{StaffServiceUrl}/api/parking-spots/{parkingSpot}";
            string licensePlate = await httpClient.GetStringAsync(url);
            return Ok(licensePlate);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    // Report illegally parked car to Java staff backend
    [HttpPost("report")]
    public async Task<ActionResult> ReportIllegalParking([FromBody] IllegalParkingReportDto report)
    {
        try
        {
            string url = $"{StaffServiceUrl}/api/parking-spots/report";
            HttpResponseMessage response = await httpClient.PostAsJsonAsync(url, report);
            response.EnsureSuccessStatusCode();
            return Ok(report);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }

    [HttpPost("scan")]
    public ActionResult<ScanParkedCarDto> ScanParkedCar([FromBody] ScanParkedCarDto request)
    {
        try
        {
            return Ok(request);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, e.Message);
        }
    }
}
