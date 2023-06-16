package com.example.pdfgeneratortest.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class AppError {
    private int statusCode;
    private String message;
}
