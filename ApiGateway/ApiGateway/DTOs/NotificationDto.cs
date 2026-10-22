namespace ApiGateway.DTOs;

public class NotificationDto
{
    public int Id { get; set; }
    public string Email { get; set; } = "";
    public string Message { get; set; } = "";
    public string CreatedAt { get; set; } = "";
}
