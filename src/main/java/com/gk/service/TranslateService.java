package com.gk.service;

import java.util.List;

public interface TranslateService {

    String getAnswer();

    String translate(List<String> text, String fromLanguage, String toLanguage);

}