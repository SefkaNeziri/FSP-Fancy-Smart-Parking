namespace ApiGateway.Controllers;

using ApiGateway.DTOs;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/admin")]
public class AdminGatewayController : ControllerBase
{
    private static readonly List<AdminFacilityDto> ManagedFacilities = new();
    private static readonly List<AdminSpotDto> ManagedSpots = new();

    // Use Case: Admin creates a new parking facility (Blazor Admin Website)
    [HttpPost("facilities")]
    public ActionResult<AdminFacilityDto> CreateFacility([FromBody] AdminFacilityDto dto)
    {
        ManagedFacilities.Add(dto);
        Console.WriteLine($"[AdminGateway] New parking facility created: {dto.Name} ({dto.HourlyRate} EUR/h)");
        return Created($"/api/admin/facilities/{dto.Name}", dto);
    }

    // Use Case: Admin adds a new parking spot to a facility
    [HttpPost("spots")]
    public ActionResult<AdminSpotDto> AddSpot([FromBody] AdminSpotDto dto)
    {
        ManagedSpots.Add(dto);
        Console.WriteLine($"[AdminGateway] New parking spot added: {dto.SpotNumber} to facility {dto.FacilityId}");
        return Created($"/api/admin/spots/{dto.SpotNumber}", dto);
    }

    // Use Case: Admin views managed spots
    [HttpGet("spots")]
    public ActionResult<IEnumerable<AdminSpotDto>> GetSpots()
    {
        return Ok(ManagedSpots);
    }
}
