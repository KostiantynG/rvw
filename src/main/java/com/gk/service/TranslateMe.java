package com.gk.service;

import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;

import java.util.Arrays;

public class TranslateMe {



    public static void main(String[] args) {

        try {
            // See comments on
            //   https://developers.google.com/resources/api-libraries/documentation/translate/v2/java/latest/
            // on options to set
            Translate translate = new Translate.Builder(
                    com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport()
                    , com.google.api.client.json.gson.GsonFactory.getDefaultInstance(), null)
                    //Need to update this to your App-Name
                    .setApplicationName("RVW")
                    .build();
            Translate.Translations.List list = translate.new Translations().list(
                    Arrays.asList(
                            //Pass in list of strings to be translated
                            "Hello World",
                            "How to use Google Translate from Java"),
                    //Target language
                    "ES");
            //Set your API-Key from https://console.developers.google.com/
            list.setKey("AIzaSyC2dmfyMpeOyxtR50emi5xQALlUgzDLc9o");
            TranslationsListResponse response = list.execute();
            for(TranslationsResource tr : response.getTranslations()) {
                System.out.println(tr.getTranslatedText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
