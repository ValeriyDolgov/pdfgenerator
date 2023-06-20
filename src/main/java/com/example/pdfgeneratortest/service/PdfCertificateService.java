package com.example.pdfgeneratortest.service;

import com.example.pdfgeneratortest.constants.CertStrings;
import com.example.pdfgeneratortest.model.Certificate;
import com.example.pdfgeneratortest.model.Course;
import com.example.pdfgeneratortest.model.User;
import com.example.pdfgeneratortest.service.utils.TransliterateUtil;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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
public class PdfCertificateService {
    private final TransliterateUtil transliterateUtil;

    public String generatePdfCertificate(String auth, String courseTag){
//        while(certificateRepository.findByVerificationCode(verificationCode) != null) {
//            String verificationCode = RandomStringUtils.random(25, true, true);
//        }
        String verificationCode = RandomStringUtils.random(25, true, true);
        return this.chooseTemplateForPdfByCourse(verificationCode, 54L, courseTag);
    }

    public String chooseTemplateForPdfByCourse(String verificationCode, Long certificateId, String courseTag) {
        Certificate certificate;
        User student;
        User mentor;
        Course course;
        switch (courseTag){
            case "Java":
                student = User.builder()
                        .surname("Shalera").name("Valera").build();
                mentor = User.builder()
                        .surname("Light of Java").name("Kuanish").position("Senior Java Developer").build();
                course = Course.builder()
                        .mentor(mentor)
                        .name("Java Developer for 3 weeks").build();
                certificate = Certificate.builder()
                        .id(certificateId)
                        .course(course)
                        .student(student)
                        .verificationCode(verificationCode)
                        .issuedDate(LocalDate.now())
                        .build();
                return parseThymeleafTemplate(verificationCode, certificateId, courseTag, certificate);
            case "JavaScript":
                student = User.builder()
                        .surname("ProtoMISHA").name("Илюха").build();
                mentor = User.builder()
                        .surname("\"Lisiy Cherep\"").name("Максим").position("Frontend Developer").build();
                course = Course.builder()
                        .mentor(mentor)
                        .name("Я тугой манкикодер").build();
                certificate = Certificate.builder()
                        .id(certificateId)
                        .course(course)
                        .student(student)
                        .verificationCode(verificationCode)
                        .issuedDate(LocalDate.now())
                        .build();
                return parseThymeleafTemplate(verificationCode, certificateId, courseTag, certificate);
        }
        return "No such course";
    }

    public String parseThymeleafTemplate(String verificationCode, Long certificateId, String courseTag, Certificate certificate) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);


//        Certificate certificate = certificateRepository.findById(certificateId);
        Context context = new Context();
        context.setVariable("company_name", "qazdev"); // "to" - variable in Thymeleaf, "qazdev" - inserted value
        context.setVariable("student_name", certificate.getStudent().getSurname() + " " + certificate.getStudent().getName());
        context.setVariable("course_name", certificate.getCourse().getName());
        context.setVariable("mentor_name", certificate.getCourse().getMentor().getSurname() + " " + certificate.getCourse().getMentor().getName());
        context.setVariable("issued_date", certificate.getIssuedDate());
        context.setVariable("verification_code", certificate.getVerificationCode());
        String generatedFileName = this.generatePdfFromHtml(templateEngine.process(courseTag.toLowerCase() + "_pdf_cert_temp", context),
                courseTag, certificate);
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

//    public Certificate createNewCertificate(User user, Course course) {
//        if ((100 * course.getNumberOfCompletedTests())/course.getNumberOfAllTests() >= 70) {
//            Certificate certificate = Certificate.builder()
//                    .course(course)
//                    .student(user)
//                    .mentor(course.getMentor())
//                    .issuedDate(LocalDate.now())
//                    .build();
//            return certificateRepository.save(certificate);
//        } else return null;
//    }

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


}
