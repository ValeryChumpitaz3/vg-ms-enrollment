package pe.edu.vallegrande.enrollment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private String id;                     // ID del estudiante
    private String documentType;           // Tipo de documento (DNI, Pasaporte, etc.)
    private String documentNumber;         // Número de documento del estudiante
    private String lastNamePaternal;      // Apellido paterno
    private String lastNameMaternal;       // Apellido materno
    private String names;                  // Nombres del estudiante
    private String sex;                    // Sexo (M, F)
    private String birthDate;              // Fecha de nacimiento (formato YYYY-MM-DD)
    private String birthCountry;            // País de nacimiento
    private Ubigeo ubigeoBirth;        // Ubigeo de nacimiento (debe ser un objeto)
    private Ubigeo ubigeoResidence;    // Ubigeo de residencia (debe ser un objeto)
    private String email;                  // Correo electrónico
    private String phoneNumber;            // Número de teléfono
    private String maritalStatus;          // Estado civil (S, C, etc.)
    private String educationLevel;         // Nivel educativo
    private boolean disability;            // Indica si tiene alguna discapacidad (booleano)
    private String disabilityType;         // Tipo de discapacidad (si aplica)
    private boolean internetAccess;        // Indica acceso a internet (booleano)
    private boolean employed;              // Indica si está empleado (booleano)
    private String occupation;             // Ocupación
    private String nativeLanguage;         // Idioma nativo
    private String address;                // Dirección del estudiante
    private String status;                 // Estado del estudiante (A, I)
}
