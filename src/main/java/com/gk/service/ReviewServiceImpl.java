package com.gk.service;

import com.gk.model.Review;
import com.gk.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.counting;
// todo Use Map Reduce pattern to allow scalability
@Service
public class ReviewServiceImpl implements ReviewService{

    @Value("${csv.file.path}")
    private String filePath;

    public Map<User, Integer> mostActiveUsers() {
        return usersReviewsCount(readFile(filePath));
    }

    protected Map<User, Integer> usersReviewsCount(Stream<String> lines) {
        Map<User, Integer> userReviewsCount = new HashMap<>();
        lines.parallel()
                .filter(line -> !line.startsWith("Id"))
                .map(mapToItem)
                .forEach(r -> countReviews(userReviewsCount, r));
        return userReviewsCount;
    }

    private void countReviews(Map<User, Integer> userReviewsCount, Review r) {
        User user = new User(r.getUserId(), r.getProfileName());
        Integer count = userReviewsCount.get(user);
        userReviewsCount.put(user, count == null ? 1 : ++count);
    }

    public Map<String, Long> mostCommentedFoodIds() {
        return foodCommentsCount(readFile(filePath));
    }

    protected Map<String, Long> foodCommentsCount(Stream<String> lines) {
        Map<String, Long> commentsCount =
                lines.parallel()
                        .filter(line -> !line.startsWith("Id"))
                        .map(mapToItem)
                        .collect(Collectors.groupingByConcurrent(Review::getProductId, counting()));
        return commentsCount;
    }

    public Map<String, Long> mostUsedWordsInReviews() {
        return mostUsedWordsInReviews(readFile(filePath));
    }

    protected Map<String, Long> mostUsedWordsInReviews(Stream<String> lines) {
        Map<String, Long> wordsCount =
                lines.parallel()
                        .filter(line -> !line.startsWith("Id"))
                        .map(mapToItem)
                        .flatMap(review -> Arrays.stream(splitByWords(review.getText())))
                        .collect(Collectors.groupingByConcurrent(s -> s, counting()));
        return wordsCount;

    }

    protected String[] splitByWords(String text) {
        requireNonNull(text);
        return text.split("\\W+");
    }

    /**
     * Columns<br />
     * Id,ProductId,UserId,ProfileName,HelpfulnessNumerator,HelpfulnessDenominator,Score,Time,Summary,Text <br />
     * 0     1         2       3              4                         5             6    7     8     9
     */
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
    //todo close resources
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
}