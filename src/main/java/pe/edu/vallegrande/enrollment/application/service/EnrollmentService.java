package pe.edu.vallegrande.enrollment.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.enrollment.domain.dto.EnrollmentDto;
import pe.edu.vallegrande.enrollment.domain.dto.StudentDto;
import pe.edu.vallegrande.enrollment.domain.dto.StudyProgramDto;
import pe.edu.vallegrande.enrollment.domain.model.Enrollment;
import pe.edu.vallegrande.enrollment.domain.repository.EnrollmentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ExternalService externalService;
    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    public EnrollmentService(EnrollmentRepository enrollmentRepository, ExternalService externalService) {
        this.enrollmentRepository = enrollmentRepository;
        this.externalService = externalService;
    }

    // Obtener todas las inscripciones
    public Flux<Enrollment> findAll() {
        return enrollmentRepository.findAll()
                .doOnNext(enrollment -> log.info("Enrollment encontrado: {}", enrollment))
                .doOnError(error -> log.error("Error al obtener inscripciones", error));
    }

    // Obtener inscripciones por estado
    public Flux<EnrollmentDto> findByStatus(String status) {
        return enrollmentRepository.findByStatus(status)
                .doOnNext(enrollment -> log.info("Enrollment encontrado con status {}: {}", status, enrollment))
                .flatMap(enrollment -> Mono.zip(
                                        externalService.getProfileById(enrollment.getProfileId())
                                                .doOnSuccess(profile -> log.info("Perfil encontrado: {}", profile)),
                                        externalService.getAcademicPeriodById(enrollment.getAcademicPeriodId())
                                                .doOnSuccess(period -> log.info("Periodo académico encontrado: {}", period)),
                                        externalService.getInstitutionalStaffById(enrollment.getInstitutionalStaffId())
                                                .doOnSuccess(staff -> log.info("Staff institucional encontrado: {}", staff)),
                                        externalService.getStudentByDocumentNumber(enrollment.getDocumentNumber())
                                                .doOnSuccess(student -> log.info("Estudiante encontrado: {}", student)),
                                        externalService.getStudyProgramById(enrollment.getProgramId())
                                                .doOnSuccess(program -> log.info("Programa de estudios encontrado: {}", program))
                                ).map(tuple -> {
                                    EnrollmentDto enrollmentDto = new EnrollmentDto();
                                    enrollmentDto.setId(enrollment.getId());
                                    enrollmentDto.setProfileId(tuple.getT1());
                                    enrollmentDto.setAcademicPeriodId(tuple.getT2());
                                    enrollmentDto.setInstitutionalStaffId(tuple.getT3());
                                    enrollmentDto.setStudent(tuple.getT4());
                                    enrollmentDto.setProgramId(tuple.getT5());
                                    enrollmentDto.setStatus(enrollment.getStatus());
                                    return enrollmentDto;
                                })
                                .doOnError(error -> log.error("Error al procesar inscripción con ID: " + enrollment.getId(), error))
                );
    }


    // Buscar inscripción por ID
    public Mono<Enrollment> findById(String id) {
        return enrollmentRepository.findById(id)
                .doOnSuccess(enrollment -> log.info("Inscripción encontrada con ID: {}", id))
                .doOnError(error -> log.error("Error al buscar inscripción por ID: " + id, error));
    }

    // Guardar una nueva inscripción
    public Mono<Enrollment> save(Enrollment enrollment) {
        return externalService.getStudentByDocumentNumber(enrollment.getDocumentNumber())
                .flatMap(student -> {
                    // Asignar el `studentId` basado en la búsqueda del número de documento
                    enrollment.setStudentId(student.getId());
                    // Aquí puedes acceder a los detalles del ubigeo si lo necesitas
                    String ubigeoBirthId = student.getUbigeoBirth().getIdUbigeo();
                    String ubigeoResidenceId = student.getUbigeoResidence().getIdUbigeo();
                    // Establecer el estado de la inscripción a "A" (Activo) si está vacío o nulo
                    if (enrollment.getStatus() == null || enrollment.getStatus().isEmpty()) {
                        enrollment.setStatus("A");
                    }

                    // Guardar el enrollment con el `studentId` y `documentNumber`
                    return enrollmentRepository.save(enrollment)
                            .doOnSuccess(e -> log.info("Inscripción guardada exitosamente con ID: " + e.getId()))
                            .doOnError(error -> log.error("Error al guardar la inscripción", error));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Estudiante no encontrado con el número de documento: " + enrollment.getDocumentNumber())));
    }


    // Método para buscar estudiante por número de documento
    public Mono<StudentDto> findStudentByDocumentNumber(String documentNumber) {
        return externalService.getStudentByDocumentNumber(documentNumber)
                .doOnSuccess(student -> log.info("Estudiante encontrado con número de documento: {}", documentNumber))
                .doOnError(error -> log.error("Error al buscar estudiante con número de documento: " + documentNumber, error));
    }

    // Actualizar inscripción por ID
    public Mono<Enrollment> update(String id, Enrollment enrollment) {
        return enrollmentRepository.findById(id)
                .flatMap(existingEnrollment -> {
                    // Actualizar los datos solo si no son nulos
                    if (enrollment.getProfileId() != null) {
                        existingEnrollment.setProfileId(enrollment.getProfileId());
                    }
                    if (enrollment.getAcademicPeriodId() != null) {
                        existingEnrollment.setAcademicPeriodId(enrollment.getAcademicPeriodId());
                    }
                    if (enrollment.getInstitutionalStaffId() != null) {
                        existingEnrollment.setInstitutionalStaffId(enrollment.getInstitutionalStaffId());
                    }
                    if (enrollment.getDocumentNumber() != null) {
                        existingEnrollment.setDocumentNumber(enrollment.getDocumentNumber());
                    }
                    if (enrollment.getProgramId() != null) {
                        existingEnrollment.setProgramId(enrollment.getProgramId());
                    }
                    if (enrollment.getStatus() != null) {
                        existingEnrollment.setStatus(enrollment.getStatus());
                    }
                    // Guardar los cambios
                    return enrollmentRepository.save(existingEnrollment);
                })
                .doOnSuccess(e -> log.info("Inscripción actualizada exitosamente con ID: " + id))
                .doOnError(error -> log.error("Error al actualizar la inscripción con ID: " + id, error));
    }


    // Cambiar el estado de una inscripción
    public Mono<Enrollment> changeStatus(String id, String status) {
        return enrollmentRepository.findById(id)
                .flatMap(enrollment -> {
                    enrollment.setStatus(status);
                    return enrollmentRepository.save(enrollment);
                })
                .doOnSuccess(e -> log.info("Estado de inscripción cambiado exitosamente a " + status + " para el ID: " + id))
                .doOnError(error -> log.error("Error al cambiar el estado de la inscripción con ID: " + id, error));
    }
}
