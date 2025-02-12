package pe.edu.vallegrande.enrollment.domain.dto;

import lombok.Data;

@Data
public class InstitucionalStaffDto {
    private String id;
    private String name;
    private String documentNumber;
    private String email;
    private String phone;
    private String occupation;
    private String status;
}
