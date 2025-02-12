package pe.edu.vallegrande.enrollment.application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.enrollment.domain.dto.*;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
public class ExternalService {

    private final WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(ExternalService.class);

    public ExternalService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // Método para obtener perfil por ID
    public Mono<ProfileDto> getProfileById(String profileId) {
        return fetchData("http://real-drucill-shirleyas-ccbaedfa.koyeb.app/management/api/v1/profile/list/", profileId, ProfileDto.class);
    }

    // Método para obtener período académico por ID
    public Mono<AcademicPeriodDto> getAcademicPeriodById(String academicPeriodId) {
        return fetchData("http://detailed-rosalinda-brillith-c11c160b.koyeb.app/management/api/v1/academic_period/id/", academicPeriodId, AcademicPeriodDto.class);
    }

    // Método para buscar estudiante por número de documento
    public Mono<StudentDto> getStudentByDocumentNumber(String documentNumber) {
        return fetchData("https://vg-ms-student-production.up.railway.app/management/api/v1/student/list/document/", documentNumber, StudentDto.class);
    }

    // Método para obtener personal institucional por ID
    public Mono<InstitucionalStaffDto> getInstitutionalStaffById(String staffId) {
        return fetchData("https://institucional-staff-production.up.railway.app/developer/api/v1/institucional-staff/get/", staffId, InstitucionalStaffDto.class);
    }

    // Método para obtener programa de estudios por ID
    public Mono<StudyProgramDto> getStudyProgramById(String programId) {
        return fetchData("https://vg-ms-study-programme-production.up.railway.app/common/api/v1/study-program/", programId, StudyProgramDto.class);
    }

    // Método genérico para realizar llamadas externas con manejo de errores y timeout
    private <T> Mono<T> fetchData(String baseUrl, String id, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> {
                            // Manejar errores 4xx y 5xx
                            log.error("Error fetching data from " + baseUrl + id + ": " + clientResponse.statusCode());
                            return clientResponse.createException().flatMap(Mono::error);
                        })
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))  // Timeout de 5 segundos
                .doOnNext(response -> log.info("Response from " + baseUrl + ": " + response))
                .onErrorResume(TimeoutException.class, e -> {
                    log.error("Timeout fetching data from " + baseUrl + id, e);
                    return Mono.empty();  // Retorna Mono vacío si hay timeout
                })
                .onErrorResume(error -> {
                    log.error("Error fetching data from " + baseUrl + id, error);
                    return Mono.empty();  // Retorna Mono vacío en caso de otro error
                });
    }
}
