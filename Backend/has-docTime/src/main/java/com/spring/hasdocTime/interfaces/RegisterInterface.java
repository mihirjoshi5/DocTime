/**
 * Interface defining operations for user registration.
 */
package com.spring.hasdocTime.interfaces;

import com.spring.hasdocTime.entity.Admin;
import com.spring.hasdocTime.entity.AuthenticationResponse;
import com.spring.hasdocTime.entity.Doctor;
import com.spring.hasdocTime.entity.User;
import com.spring.hasdocTime.exceptionHandling.exception.DoesNotExistException;
import com.spring.hasdocTime.exceptionHandling.exception.MissingParameterException;

public interface RegisterInterface {

    /**
     * Registers an admin.
     *
     * @param admin the Admin object representing the admin to be registered
     * @return the AuthenticationResponse object containing the registration information
     * @throws DoesNotExistException    if related entities do not exist
     * @throws MissingParameterException if required parameters are missing
     */
    AuthenticationResponse registerAdmin(Admin admin) throws DoesNotExistException, MissingParameterException;

    /**
     * Registers a user.
     *
     * @param user the User object representing the user to be registered
     * @return the AuthenticationResponse object containing the registration information
     * @throws MissingParameterException if required parameters are missing
     * @throws DoesNotExistException    if related entities do not exist
     */
    AuthenticationResponse registerUser(User user) throws MissingParameterException, DoesNotExistException;

    /**
     * Registers a doctor.
     *
     * @param doctor the Doctor object representing the doctor to be registered
     * @return the AuthenticationResponse object containing the registration information
     * @throws MissingParameterException if required parameters are missing
     * @throws DoesNotExistException    if related entities do not exist
     */
    AuthenticationResponse registerDoctor(Doctor doctor) throws MissingParameterException, DoesNotExistException;
}
