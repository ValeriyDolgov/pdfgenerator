package com.example.pdfgeneratortest.service.utils;

import com.ibm.icu.text.Transliterator;
import org.springframework.stereotype.Component;

@Component
public class TransliterateUtil {
    public String transliterateTextFromRussianToLatin(String text) {
        Transliterator transliterator = Transliterator.getInstance("Russian-Latin/BGN");
        return transliterator.transliterate(text);
    }
}
