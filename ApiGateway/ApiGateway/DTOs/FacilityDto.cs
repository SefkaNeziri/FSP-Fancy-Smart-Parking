namespace ApiGateway.DTOs;

public class FacilityDto
{
    public Guid Id { get; set; }
    public string Name { get; set; } = "";
    public string Address { get; set; } = "";
    public double Latitude { get; set; }
    public double Longitude { get; set; }
    public double DistanceKm { get; set; }
    public decimal HourlyRate { get; set; }
    public long TotalSpots { get; set; }
    public long AvailableSpots { get; set; }
}
