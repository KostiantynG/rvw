package com.gk.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuickstartSample {
    public static void main(String... args) throws Exception {
        // Instantiates a client
        Translate translate = TranslateOptions.builder().apiKey("AIzaSyC2dmfyMpeOyxtR50emi5xQALlUgzDLc9o").build().service();




        Stream<String> lines = readFile("D:\\SqlLite\\Reviews.csv");
        List<String> textList = lines.parallel()
                .filter(line -> !line.startsWith("Id"))
                .limit(10)
                .collect(Collectors.toList());
        List<Translation> translation = translate.translate(
                textList,
                TranslateOption.sourceLanguage("en"),
                TranslateOption.targetLanguage("ru")
        );
        translation.forEach(System.out::println);

//        // The text to translate
//        String text = "Hello, world!";
//
//        // Translates some text into Russian
//        Translation translation = translate.translate(
//                text,
//                TranslateOption.sourceLanguage("en"),
//                TranslateOption.targetLanguage("ru")
//        );
//
//        System.out.printf("Text: %s%n", text);
//        System.out.printf("MyTranslation: %s%n", translation.translatedText());
//
//
//
//
//        List<String> textList = new ArrayList<>();
//        textList.add("Hello");
//        textList.add("My Friend");
//
//        // Translates some text into Russian
//        List<Translation> translation2 = translate.translate(
//                textList,
//                Translate.TranslateOption.sourceLanguage("en"),
//                Translate.TranslateOption.targetLanguage("ru")
//        );
//        translation2.forEach(System.out::println);



    }
    public static Stream<String> readFile(String inputFilePath) {
        Stream<String> lines = null;
        File file = new File(inputFilePath);
        try {
            InputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            lines = br.lines();
        } catch (IOException e) {
            // todo exception handling  
        }
        return lines;
    }
}
