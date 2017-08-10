Run spring-boot project

curl -H "Content-Type: application/json" -X GET http://localhost:8090/mostActiveUsers/
curl -H "Content-Type: application/json" -X GET http://localhost:8090/mostCommentedFoodIds/
curl -H "Content-Type: application/json" -X GET http://localhost:8090/mostUsedWordsInReviews/

1) Finding 1000 most active users (ProfileName)
2) Finding 1000 most commented food items (ProductId)
3) Finding 1000 most used words in the reviews


curl -H "Content-Type: application/json" -X GET http://localhost:8090/translateText/

4) We want to translate all the reviews using Google Translate API. You can send up to 1000 characters per HTTP call. API has 200ms average response time. How to do it efficiently and cost effective (you pay for API calls and we have concurrency limits -  100 requests in parallel max) - please mock the calls to google translate API.
