namespace ApiGateway.DTOs;

public class IllegalParkingReportDto
{
    public string LicensePlate { get; set; } = "";
    public string ParkingSpot { get; set; } = "";
    public DateTimeOffset ReportedAt { get; set; } = DateTimeOffset.Now;
}
