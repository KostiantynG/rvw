package com.gk.service;

import com.gk.model.User;

import java.util.Map;

public interface ReviewService {

    Map<User, Integer> mostActiveUsers();

    Map<String, Long> mostCommentedFoodIds();

    Map<String, Long> mostUsedWordsInReviews();

}