package com.example.pdfgeneratortest.model;

import lombok.*;

import java.time.LocalDate;

/**
 * Class {@linkplain Certificate} contains information about certificates after competing courses. Information for
 * generating PDF's is getting from this class in {@linkplain com.example.pdfgeneratortest.service.CertificateService}
 * @author Shalera
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificate {
    /**
     *Id of certificate
     */
    private Long id;
    /**
     * Attached {@linkplain Course}
     */
    private Course course;
    /**
     * The student, completed course
     */
    private User student;
    /**
     * The mentor of student
     */
    private User mentor;
    /**
     * Date of generating record to DataBase and date of competing course
     */
    private LocalDate issuedDate;
    /**
     * Verification code, needed for checking authenticity of given certificate
     */
    private String verificationCode;
    /**
     * File name of certificate, when creating record to DataBase file name is {@code Null}, after generating PDF
     * certificate his name is setting to DataBase record
     */
    private String fileName;
}