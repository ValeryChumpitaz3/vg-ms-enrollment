package pe.edu.vallegrande.enrollment.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;
    private String profileId;
    private String academicPeriodId;
    private String institutionalStaffId;
    private String studentId;
    private String programId;
    private String documentNumber;
    private String status = "A"; // Inicializa con el valor "A"
}
