namespace ApiGateway.DTOs;

public class ReservationDto
{
    public int Id { get; set; }
    public string Email { get; set; } = "";
    public string LicensePlate { get; set; } = "";
    public string ParkingSpot { get; set; } = "";
    public int Hours { get; set; } = 1;
    public double Payment { get; set; }
    public DateTime EndTime { get; set; }
    public bool ReminderSent { get; set; }
}
