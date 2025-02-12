package pe.edu.vallegrande.enrollment.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.enrollment.domain.dto.EnrollmentDto;
import pe.edu.vallegrande.enrollment.domain.dto.StudentDto;
import pe.edu.vallegrande.enrollment.domain.model.Enrollment;
import pe.edu.vallegrande.enrollment.application.service.EnrollmentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(value = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/management/${api.version}/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Enrollment>> listById(@PathVariable String id) {
        return ResponseEntity.ok(enrollmentService.findById(id));
    }

    @GetMapping("/listAll")
    public ResponseEntity<Flux<Enrollment>> listAll() {
        return ResponseEntity.ok(enrollmentService.findAll());
    }

    @GetMapping("/list")
    public ResponseEntity<Flux<EnrollmentDto>> list() {
        return ResponseEntity.ok(enrollmentService.findByStatus("A"));
    }

    @GetMapping("/list/active")
    public ResponseEntity<Flux<EnrollmentDto>> listActive() {
        return ResponseEntity.ok(enrollmentService.findByStatus("A"));
    }

    @GetMapping("/list/inactive")
    public ResponseEntity<Flux<EnrollmentDto>> listInactive() {
        return ResponseEntity.ok(enrollmentService.findByStatus("I"));
    }

    @PostMapping("/create")
    public ResponseEntity<Mono<Enrollment>> create(@RequestBody Enrollment enrollment) {
        return ResponseEntity.ok(enrollmentService.save(enrollment));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Mono<Enrollment>> update(@PathVariable String id,
                                                   @RequestBody Enrollment enrollment) {
        return ResponseEntity.ok(enrollmentService.update(id, enrollment));
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Mono<Enrollment>> activate(@PathVariable String id) {
        return ResponseEntity.ok(enrollmentService.changeStatus(id, "A"));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Mono<Enrollment>> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(enrollmentService.changeStatus(id, "I"));
    }
    // Nueva ruta para autocompletar la inscripción usando el DocumentNumber del estudiante
    @GetMapping("/student/documentNumber/{documentNumber}")
    public ResponseEntity<Mono<StudentDto>> getStudentByDocumentNumber(@PathVariable String documentNumber) {
        return ResponseEntity.ok(enrollmentService.findStudentByDocumentNumber(documentNumber));
    }
}