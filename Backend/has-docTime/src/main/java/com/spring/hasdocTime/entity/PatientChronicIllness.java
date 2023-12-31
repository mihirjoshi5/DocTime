package com.spring.hasdocTime.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Objects;


/**
 * The PatientChronicIllness class represents the chronic illness of a patient.
 * It contains information about the composite key, associated user, associated
 * chronic illness, and years of illness.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="patient_chronic_illness")
public class PatientChronicIllness {

    @EmbeddedId
    private CompositeKeyPatientChronicIllness id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patientId")
    @JoinColumn(name="patient_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value={"patientChronicIllness", "doctor", "admin", "appointmentData", "appointments", "symptoms"}, allowSetters = true)
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @MapsId("chronicIllnessId")
    @JoinColumn(name="chronic_illness_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "patientChronicIllnesses", allowSetters = true)
    private ChronicIllness chronicIllness;

    @Column(name="years_of_illness")
    @DecimalMin(value = "0.0", inclusive = true, message = "Years of Illness should at least be 0")
    @DecimalMax(value = "150.0", inclusive = true, message = "Years of Illness should not exceed 150")
    private float yearsOfIllness;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientChronicIllness that = (PatientChronicIllness) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
