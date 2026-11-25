namespace ApiGateway.DTOs;

public class AdminSpotDto
{
    public string SpotNumber { get; set; } = "";
    public string SpotType { get; set; } = "REGULAR";
    public Guid FacilityId { get; set; }
}

public class AdminFacilityDto
{
    public string Name { get; set; } = "";
    public string Address { get; set; } = "";
    public double Latitude { get; set; }
    public double Longitude { get; set; }
    public double HourlyRate { get; set; }
}
