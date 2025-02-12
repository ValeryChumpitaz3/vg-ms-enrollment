package pe.edu.vallegrande.enrollment.domain.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private String id;
    private String name;
    private String modularCode; // Código modular del CETPRO
    private String dreGre; // DRE/GRE
    private String managementType; // Tipo de gestión (privado, público, etc.)
    private String status; // A = Activo, I = Inactivo
}
