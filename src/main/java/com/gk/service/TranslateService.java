package com.gk.service;

public interface TranslateService {

    String getAnswer();

    String translate(String text, String toLanguage);

}