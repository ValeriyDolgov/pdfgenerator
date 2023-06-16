package com.example.pdfgeneratortest.service;

import com.example.pdfgeneratortest.constants.CertStrings;
import com.example.pdfgeneratortest.model.Certificate;
import com.example.pdfgeneratortest.model.Course;
import com.example.pdfgeneratortest.model.User;
import com.example.pdfgeneratortest.service.utils.TransliterateUtil;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final TransliterateUtil transliterateUtil;

    public String parseThymeleafTemplate(String verificationCode, Long certificateId) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        User student = User.builder()
                .surname("Shalera").name("Valera").build();
        User mentor = User.builder()
                .surname("Light of Java").name("Kuanish").position("Senior Java Developer").build();
        Course course = Course.builder()
                .mentor(mentor)
                .name("Java Developer for 3 weeks").build();
//        while(certificateRepository.findByVerificationCode(verificationCode) != null) {
//            verificationCode = RandomStringUtils.random(25, true, true);
//        }

        Certificate certificate = Certificate.builder()
                .id(54L)
                .course(course)
                .student(student)
                .verificationCode(verificationCode)
                .issuedDate(LocalDate.now())
                .build();
        Context context = new Context();
        context.setVariable("company_name", "qazdev"); // "to" - variable in Thymeleaf, "Qazdevelop" - inserted value
        context.setVariable("student_name", certificate.getStudent().getSurname() + " " + certificate.getStudent().getName());
        context.setVariable("course_name", certificate.getCourse().getName());
        context.setVariable("mentor_name", certificate.getCourse().getMentor().getSurname() + " " + certificate.getCourse().getMentor().getName());
        context.setVariable("issued_date", certificate.getIssuedDate());
        context.setVariable("verification_code", certificate.getVerificationCode());
        String generatedFileName = this.generatePdfFromHtml(templateEngine.process("pdf_cert_temp", context),
                "Java", certificate);
        certificate.setFileName(generatedFileName);
        return generatedFileName;
    }

    //for prod
//    public String parseThymeleafTemplate(Long id) {
//        Certificate certificate = certRepository.findById(id);
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode(TemplateMode.HTML);
//
//        TemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver);
//
//        Context context = new Context();
//        context.setVariable("to", "Qazdevelop"); // "to" - variable in Thymeleaf, "Qazdevelop" - inserted value
//        context.setVariable("student_name", certificate.getStudent().getSurname() + " " + certificate.getStudent().getName());
//        context.setVariable("course_name", certificate.getCourse().getName());
//        context.setVariable("mentor_name", certificate.getCourse().getMentor().getSurname() + " " + certificate.getCourse().getMentor().getName());
//        context.setVariable("issued_date", certificate.getIssuedDate());
//        context.setVariable("verification_code", certificate.getVerificationCode());
//        return templateEngine.process("pdf_cert_temp", context);//
//    }

    public void createNewCertificate() {

    }

    public String generatePdfFromHtml(String html, String courseTag, Certificate certificate) {
        String fileName = courseTag + "_" +
                transliterateUtil.transliterateTextFromRussianToLatin(certificate.getStudent().getName()) + "_" +
                certificate.getIssuedDate().toString() + "_" + certificate.getId() +
                CertStrings.CERT_EXTENSION;
        String outputFolder = CertStrings.PATH_TO_PDF_CERTS + File.separator + fileName;
        try (OutputStream outputStream = new FileOutputStream(outputFolder)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(CertStrings.ERROR_WHILE_OUTPUT_STREAM + e);
        } catch (DocumentException e) {
            throw new RuntimeException(CertStrings.ERROR_CREATING_PDF + e);
        }
        return fileName;
    }

    public void selectNameOfGeneratingPdf() {

    }
}
