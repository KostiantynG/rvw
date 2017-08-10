package com.gk.web;

import com.gk.model.User;
import com.gk.service.ReviewServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReviewController {

    @Value("${limit}")
    private Long limit;

    @Autowired
    private Gson gson;

    @Autowired
    private ReviewServiceImpl reviewService;

    /**
     * curl -H "Content-Type: application/json" -X GET http://localhost:8090/mostActiveUsers/
     * curl -H "Content-Type: application/json" -X GET http://localhost:8090/mostCommentedFoodIds/
     * curl -H "Content-Type: application/json" -X GET http://localhost:8090/mostUsedWordsInReviews/
     * <p>
     * Map<User, Integer> mostActiveUsers();
     * <p>
     * Map<String, Long> mostCommentedFoodIds();
     * <p>
     * Map<String, Long> mostUsedWordsInReviews();
     */

    @GetMapping(value = "mostActiveUsers/")
    public ResponseEntity<String> mostActiveUsers() {
        List<User> result;
        try {
            result = reviewService.mostActiveUsers().keySet().stream()
                    .collect(Collectors.toList());
            result.stream().sorted().limit(limit);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(gson.toJson(result), HttpStatus.OK);
    }

    @GetMapping(value = "mostCommentedFoodIds/")
    public ResponseEntity<String> mostCommentedFoodIds() {
        List<String> result;
        try {
            result = reviewService.mostCommentedFoodIds().keySet().stream()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(gson.toJson(result), HttpStatus.OK);
    }

    @GetMapping(value = "mostUsedWordsInReviews/")
    public ResponseEntity<String> mostUsedWordsInReviews() {
        List<String> result;
        try {
            result = reviewService.mostUsedWordsInReviews().keySet().stream()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(gson.toJson(result), HttpStatus.OK);
    }
}