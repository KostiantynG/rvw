package com.gk.web;

import com.gk.service.TranslateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TranslateController {

    @Autowired
    private TranslateServiceImpl translateService;

    /**
     * curl -H "Content-Type: application/json" -X GET http://localhost:8090/translateText/
     */
    @GetMapping(value = "translateText/")
    public ResponseEntity<String> translateText() {
        String answer;
        try {
            answer = translateService.getAnswer();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}