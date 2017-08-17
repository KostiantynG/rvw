package com.gk.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MyTranslation {
    private String original;
    private String translated;
    private String fromLanguage;
    private String toLanguage;
}