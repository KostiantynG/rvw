package com.gk.web;

import com.gk.service.TranslateServiceImpl;
import jdk.nashorn.internal.runtime.regexp.joni.constants.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TranslateController {

    @Autowired
    private TranslateServiceImpl translateService;

    /**
     * curl -H "Content-Type: application/json" -X GET http://localhost:8090/translateText/
     */
    @GetMapping(value = "api.google.com/translate/")
    public ResponseEntity<String> translateText2() {
        String answer;
        try {
            answer = translateService.getAnswer();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    /**
     * curl -H "Content-Type: application/json" -X POST -d '{"values":[2,3,4]}' http://localhost:8090/calculate?engine=nashorn
     */

    @PostMapping(value = "api.google.com/translate",
            consumes = "application/json")
    public ResponseEntity<String> translateText(
                                          @RequestParam(value = "translate", required = false, defaultValue = "false") String translate) {
        String result  = "ok";

        try {
            //result = calculatorFactory.getCalculator(engine).process(arguments).toString();
        } catch (ArithmeticException | NumberFormatException e) {
            result = "Wrong arguments " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}