package pe.edu.vallegrande.enrollment.domain.dto;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Ubigeo {

    @Id
    private String idUbigeo;
    private String ubigeoReniec;
    private String department;
    private String province;
    private String district;
    private String region;
    private String status;

}