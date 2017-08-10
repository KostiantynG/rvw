package com.gk.service;

import com.gk.model.Translation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

// todo store in normal cache or in database.
// todo Google SDK should be used instead
// todo
@Service
public class TranslateServiceImpl implements TranslateService {

    @Value("${google.api.key}")
    private String googleApiKey;

    private Map<Integer, Translation> hashToTranslation = new HashMap<>(); // todo Map<Integer, List<Translation>> to have multi lang caching:)
    private RestTemplate restTemplate = new RestTemplate();

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

    public String translate(String text, String toLanguage) {
        Translation cached = hashToTranslation.get(text.hashCode());
        if (cached == null || (cached.getOriginal().equals(text) && cached.getToLanguage().equals(toLanguage))) {
            String url = "https://www.googleapis.com/language/translate/v2" +
                    "?key=AIzaSyC2dmfyMpeOyxtR50emi5xQALlUgzDLc9o&source=en&target=" + toLanguage + "&q=" + text;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String answer = restTemplate.getForObject(url, String.class);
            // TODO get translatedText JSON field from answer!
            cached = new Translation(text, answer, toLanguage);
            hashToTranslation.put(text.hashCode(), cached);
        }
        return cached.getTranslated();
    }
}