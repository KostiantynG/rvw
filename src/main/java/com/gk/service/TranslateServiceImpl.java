package com.gk.service;

import com.gk.model.MyTranslation;
import com.gk.model.Review;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// todo store in normal cache or in database.
// todo Google SDK should be used instead
// todo
@Service
public class TranslateServiceImpl implements TranslateService {

    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${limit}")
    private Long limit;

    @Autowired
    private Gson gson;

    @Autowired
    private ReviewServiceImpl reviewService;

    @Value("${csv.file.path}")
    private String filePath;

    private Map<Integer, MyTranslation> hashToTranslation = new HashMap<>(); // todo Map<Integer, List<MyTranslation>> to have multi lang caching:)
    private RestTemplate restTemplate = new RestTemplate();

    public String translate(List<String> text, String fromLanguage, String toLanguage) {
        Translate translate = TranslateOptions.builder().apiKey("AIzaSyC2dmfyMpeOyxtR50emi5xQALlUgzDLc9o").build().service();




        Stream<String> lines = readFile(filePath);
        List<String> textList = lines.parallel()
                .filter(line -> !line.startsWith("Id"))
                .limit(10)
                .collect(Collectors.toList());
        List<Translation> translation = translate.translate(
                textList,
                Translate.TranslateOption.sourceLanguage("en"),
                Translate.TranslateOption.targetLanguage("ru")
        );
        translation.forEach(System.out::println);


//        MyTranslation cached = hashToTranslation.get(text.hashCode());
//        if (cached == null || (cached.getOriginal().equals(text) && cached.getFromLanguage().equals(fromLanguage) && cached.getToLanguage().equals(toLanguage))) {
//
//
//        }
//        cached = new MyTranslation(text, answer, fromLanguage, toLanguage);
//        hashToTranslation.put(text.hashCode(), cached);
//
//        return cached.getTranslated();
        return "OK";
    }


    public String getAnswer() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://www.googleapis.com/language/translate/v2?key=AIzaSyC2dmfyMpeOyxtR50emi5xQALlUgzDLc9o&source=en&target=de&q=World";
        String requestJson = "{input_lang: ‘en’, output_lang: ‘fr’, text: “Hello John, how are you?”}\n";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        HttpEntity<String> entity = new HttpEntity<>(requestJson,headers);
        String answer = restTemplate.getForObject(url, String.class);
        return answer;
    }

    private Stream<String> readFile(String inputFilePath) {
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

    private Function<String, Review> mapToItem = (line) -> {
        String[] p = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");// a CSV has comma separated lines
        Review item = new Review();
        item.setId(Long.valueOf(p[0]));//<-- this is the first column in the csv file
        item.setProductId(p[1]);
        item.setUserId(p[2]);
        item.setProfileName(p[3]);
        item.setHelpfulnessNumerator(Integer.valueOf(p[4]));
        item.setHelpfulnessDenominator(Integer.valueOf(p[5]));
        item.setScore(Integer.valueOf(p[6]));
        item.setTime(Long.valueOf(p[7]));
        item.setSummary(p[8]);
        item.setText(p[9]);
        return item;
    };
}