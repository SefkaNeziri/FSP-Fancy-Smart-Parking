namespace ApiGateway.DTOs;

public class ParkingSpotDto
{
    public Guid Id { get; set; }
    public Guid FacilityId { get; set; }
    public string SpotNumber { get; set; } = "";
    public string CurrentStatus { get; set; } = "AVAILABLE";
}
