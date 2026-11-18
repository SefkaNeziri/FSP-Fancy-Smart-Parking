package driverservice.service;

import driverservice.dto.DriverProfileResponse;
import driverservice.dto.LoginRequest;
import driverservice.dto.RegisterRequest;
import driverservice.model.Reservation;
import driverservice.model.User;
import driverservice.model.Vehicle;
import driverservice.repository.ReservationRepository;
import driverservice.repository.UserRepository;
import driverservice.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Use case 3: Register account + Log in.
 */
@Service
public class DriverService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordHasher passwordHasher;

    // Dependency Injection through the constructor
    public DriverService(UserRepository userRepository,
                         VehicleRepository vehicleRepository,
                         ReservationRepository reservationRepository,
                         PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.reservationRepository = reservationRepository;
        this.passwordHasher = passwordHasher;
    }

    @Transactional
    public DriverProfileResponse register(RegisterRequest request) {
        // 1. validate data
        if (request.email() == null || !request.email().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        if (request.password() == null || request.password().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must have at least 6 characters");
        }
        if (request.licensePlate() == null || request.licensePlate().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "License plate is required");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already used");
        }

        // 2. create user with hashed password
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordHasher.hash(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user = userRepository.save(user);

        // 3. create his first vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setDriverId(user.getId());
        vehicle.setLicensePlate(request.licensePlate());
        vehicle.setBrand(request.brand());
        vehicle.setModel(request.model());
        vehicle.setIsDefault(true);
        vehicleRepository.save(vehicle);

        // 4. driver is logged in automatically -> return his profile
        return buildProfile(user, "Account created successfully.");
    }

    public DriverProfileResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password"));

        if (!passwordHasher.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        }
        return buildProfile(user, "Logged in successfully.");
    }

    public DriverProfileResponse getProfile(UUID driverId) {
        return buildProfile(getDriver(driverId), "OK");
    }

    public User getDriver(UUID driverId) {
        return userRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
    }

    public Vehicle addVehicle(UUID driverId, Vehicle vehicle) {
        getDriver(driverId); // check driver exists
        vehicle.setDriverId(driverId);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehicles(UUID driverId) {
        return vehicleRepository.findByDriverId(driverId);
    }

    private DriverProfileResponse buildProfile(User user, String message) {
        List<Vehicle> vehicles = vehicleRepository.findByDriverId(user.getId());
        List<Reservation> active = reservationRepository.findByDriverIdAndStatusIn(
                user.getId(), List.of("PENDING_PAYMENT", "CONFIRMED", "ACTIVE"));
        return new DriverProfileResponse(message, user, vehicles, active);
    }
}
