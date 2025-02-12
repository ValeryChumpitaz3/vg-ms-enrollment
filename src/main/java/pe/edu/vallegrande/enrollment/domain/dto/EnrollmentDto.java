package pe.edu.vallegrande.enrollment.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentDto {
    private String id;
    private ProfileDto profileId;
    private AcademicPeriodDto academicPeriodId;
    private InstitucionalStaffDto institutionalStaffId;
    private StudentDto student; // Objeto completo del estudiante
    private StudyProgramDto programId;
    private String status = "A"; // Inicializa con el valor "A"
}
