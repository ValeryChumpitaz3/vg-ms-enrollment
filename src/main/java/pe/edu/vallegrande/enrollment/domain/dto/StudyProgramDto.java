package pe.edu.vallegrande.enrollment.domain.dto;

import lombok.Data;

@Data
public class StudyProgramDto {
    private String programId;
    private String name;
    private String module;
    private String trainingLevel;
    private String studyPlanType;
    private String credits;
    private String hours;
    private String status;

}