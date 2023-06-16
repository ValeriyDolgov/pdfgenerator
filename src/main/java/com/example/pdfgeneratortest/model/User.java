package com.example.pdfgeneratortest.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String surname;
    private String name;
    private String position; // for mentor
}
