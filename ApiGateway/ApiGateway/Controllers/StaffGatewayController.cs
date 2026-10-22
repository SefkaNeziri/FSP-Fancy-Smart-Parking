namespace ApiGateway.Controllers;

using ApiGateway.DTOs;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/staff")]
public class StaffGatewayController : ControllerBase
{
    // TODO: Connect with Christian's Staff Service when ready

    [HttpPost("scan")]
    public ActionResult ScanParkedCar([FromBody] ScanParkedCarDto request)
    {
        // Staff scan parkedCarLicensePlate
        return Ok("Scan endpoint sketch - waiting for Staff Backend implementation.");
    }
}
