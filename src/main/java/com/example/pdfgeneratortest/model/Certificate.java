package com.example.pdfgeneratortest.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificate {
    private Long id;
    private Course course;
    private User student;
    private User mentor;
    private LocalDate issuedDate;
    private String verificationCode;
    private String fileName;
}
