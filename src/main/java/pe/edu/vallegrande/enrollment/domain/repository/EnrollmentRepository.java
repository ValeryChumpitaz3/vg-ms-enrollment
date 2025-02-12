package pe.edu.vallegrande.enrollment.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.enrollment.domain.model.Enrollment;
import reactor.core.publisher.Flux;

@Repository
public interface EnrollmentRepository extends ReactiveMongoRepository<Enrollment, String> {

    Flux<Enrollment> findByStatus(String status);
}
