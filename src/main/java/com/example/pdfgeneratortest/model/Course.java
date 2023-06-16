package com.example.pdfgeneratortest.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    private Long id;
    private String name;
    private User mentor;
}
