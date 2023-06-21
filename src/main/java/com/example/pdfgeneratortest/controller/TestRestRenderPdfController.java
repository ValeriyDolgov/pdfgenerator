package com.example.pdfgeneratortest.controller;

import com.example.pdfgeneratortest.constants.CertStrings;
import com.example.pdfgeneratortest.controller.dto.AppError;
import com.example.pdfgeneratortest.service.PdfCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certificate")
public class TestRestRenderPdfController {
    private final PdfCertificateService certificateService;

    @GetMapping("/generate")
    // скорее всего из URI получать имя курса, далее делать поиск по нему и по getAuthentication
    public ResponseEntity<?> generatePdf(@RequestParam String courseTag) {
        try {
            String fileName = certificateService.generatePdfCertificate("dolgov.v@qazdevelop.com", courseTag);
            if (fileName == null) {
                return new ResponseEntity<>(CertStrings.COURSE_NOT_FOUND + ": " + courseTag, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Certificate created successfully! Certificate file name: " + fileName,
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Something goes wrong through creating certificate!"),
                    HttpStatus.BAD_REQUEST);
        }
    }
//      to prod
//    @PostMapping("/check/{serialCode}") // М.б. доступно всем
//    public ResponseEntity<?> checkCertificateByValidationCode(@PathVariable String validationCode) {
//        try {
//            Certificate certificate = certificateService.getByValidationCode();
//            return new ResponseEntity<>(certificate, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
//                    "Certificate with validation code " + validationCode + " not found!"), HttpStatus.NOT_FOUND);
//        }
//    }
}
