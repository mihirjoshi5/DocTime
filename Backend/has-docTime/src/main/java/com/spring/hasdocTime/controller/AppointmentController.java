package com.spring.hasdocTime.controller;

import com.spring.hasdocTime.entity.Appointment;
import com.spring.hasdocTime.exceptionHandling.exception.DoesNotExistException;
import com.spring.hasdocTime.exceptionHandling.exception.MissingParameterException;
import com.spring.hasdocTime.interfaces.AppointmentInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * The AppointmentController class handles HTTP requests related to the Appointment entity.
 */
@RestController
@RequestMapping("appointment")
@CrossOrigin(origins = "${port.address}")
public class AppointmentController {

    private AppointmentInterface appointmentService;

    /**
     * Constructs an instance of AppointmentController with the specified AppointmentService implementation.
     *
     * @param appointmentService The AppointmentService implementation to use.
     */
    @Autowired
    public AppointmentController(@Qualifier("appointmentServiceImpl") AppointmentInterface appointmentService) {
        this.appointmentService = appointmentService;
    }


    @GetMapping("")
    public ResponseEntity<Page<Appointment>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "user.name") String sortBy,                //sort by user.name, doctor.user.name, timeSlotForAppointment.startTime
            @RequestParam(required = false) String search
    ) {
        try {
            Page<Appointment> allAppointments = appointmentService.getAllAppointments(page, size, sortBy, search);
            return ResponseEntity.ok(allAppointments);
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build();
        }
//        return null;
    }

    /**
     * Retrieves all Appointments.
     *
     * @return ResponseEntity containing a list of all Appointments and HttpStatus.OK if successful,
     * or HttpStatus.NOT_FOUND if an error occurs.
     */
    @GetMapping("list")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        try {
            List<Appointment> allAppointments = appointmentService.getAllAppointmentList();
            return ResponseEntity.ok(allAppointments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Retrieves an Appointment by its ID.
     *
     * @param id The ID of the Appointment to retrieve.
     * @return ResponseEntity containing the retrieved Appointment and HttpStatus.OK if successful,
     * or HttpStatus.NOT_FOUND if the Appointment does not exist.
     * @throws DoesNotExistException If the Appointment with the given ID does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id) throws DoesNotExistException {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new Appointment.
     *
     * @param appointment The Appointment object to create.
     * @return The created Appointment.
     * @throws MissingParameterException If required parameters are missing in the creation request.
     * @throws DoesNotExistException     If the referenced entities (e.g., User, Doctor) do not exist.
     */
    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) throws MissingParameterException, DoesNotExistException {
        return appointmentService.createAppointment(appointment);
    }

    /**
     * Updates an Appointment.
     *
     * @param id         The ID of the Appointment to update.
     * @param appointment The updated Appointment object.
     * @return ResponseEntity containing the updated Appointment and HttpStatus.OK if successful,
     * or HttpStatus.NOT_FOUND if the Appointment does not exist.
     * @throws DoesNotExistException     If the Appointment with the given ID does not exist.
     * @throws MissingParameterException If required parameters are missing in the update request.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable int id, @RequestBody Appointment appointment) throws DoesNotExistException, MissingParameterException {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
            return ResponseEntity.ok(updatedAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an Appointment by its ID.
     *
     * @param id The ID of the Appointment to delete.
     * @return ResponseEntity containing the deleted Appointment and HttpStatus.OK if successful,
     * or HttpStatus.NOT_FOUND if the Appointment does not exist.
     * @throws DoesNotExistException If the Appointment with the given ID does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable int id) throws DoesNotExistException {
        Appointment appointment = appointmentService.deleteAppointment(id);
        if (appointment == null) {
            return new ResponseEntity<>(appointment, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    /**
     * Retrieves all Appointments associated with a User.
     *
     * @param id The ID of the User.
     * @return ResponseEntity containing a list of Appointments associated with the User and HttpStatus.OK if successful,
     * or HttpStatus.NO_CONTENT if no Appointments are found.
     * @throws DoesNotExistException If the User with the given ID does not exist.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<Appointment>> getAppointmentsByUser(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "doctor.user.name") String sortBy,     //sort by doctor.user.name, timeSlotForAppointment.startTime        &sortDirection=desc to sort ascending or descending
            @RequestParam(required = false) String search
    ) throws DoesNotExistException{
        Page<Appointment> appointments = appointmentService.getAppointmentsByUser(id, page, size, sortBy, search);
        if(appointments.isEmpty()){
            return new ResponseEntity<>(appointments, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/userList/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentListByUser(@PathVariable int id) throws DoesNotExistException{
        List<Appointment> appointments = appointmentService.getAppointmentListByUser(id);
        if(appointments==null){
            return new ResponseEntity<>(appointments, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Retrieves all Appointments associated with a Doctor.
     *
     * @param id The ID of the Doctor.
     * @return ResponseEntity containing a list of Appointments associated with the Doctor and HttpStatus.OK if successful,
     * or HttpStatus.NO_CONTENT if no Appointments are found.
     * @throws DoesNotExistException If the Doctor with the given ID does not exist.
     */
    @GetMapping("/doctor/{id}")
    public ResponseEntity<Page<Appointment>> getAppointmentsOfDoctor(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "timeSlotForAppointment.startTime") String sortBy,     //sort by user.name, timeSlotForAppointment.startTime
            @RequestParam(required = false) String search
    ) throws DoesNotExistException {
        Page<Appointment> appointments = appointmentService.getAppointmentsOfDoctor(id, page, size, sortBy, search);
        if(appointments.isEmpty()){
            return new ResponseEntity<>(appointments, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}
