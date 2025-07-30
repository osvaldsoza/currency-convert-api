package com.github.osvaldsoza.externalclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExchangeRateClient {

    private final ObjectMapper mapper = new ObjectMapper();

    private static String apiKey;

    static {
        try {
            Dotenv dotenv = Dotenv.load();
            apiKey = dotenv.get("EXCHANGE_API_KEY");
        } catch (DotenvException e) {
            apiKey = System.getenv("EXCHANGE_API_KEY");
        }
    }

    public double convert(String from, String to, double amount) throws Exception {
        String url = String.format("https://api.exchangeratesapi.io/v1/convert?access_key=%s&from=%s&to=%s&amount=%f", apiKey, from, to, amount);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .timeout(Duration.ofSeconds(10))
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        checkTheHTTPStatusCodeFirst(response);

        JsonNode jsonNode = mapper.readTree(response.body());

        checkIfTheAPIReturnedErrorMessage(jsonNode);

        if (jsonNode.has("result") && jsonNode.get("result").isDouble()) {
            return jsonNode.get("result").asDouble();
        } else {
            // Handle cases where "result" is missing or not a double
            System.err.println("API response did not contain a valid 'result' field: " + jsonNode.toPrettyString());
            throw new RuntimeException("Failed to get conversion result from API response.");
        }
    }

    private static void checkIfTheAPIReturnedErrorMessage(JsonNode jsonNode) {
        if (jsonNode.has("error")) {
            String errorCode = jsonNode.get("error").get("code").asText();
            String errorMessage = jsonNode.get("error").get("info").asText();
            System.err.println("API Error: Code=" + errorCode + ", Message=" + errorMessage);
            throw new RuntimeException("Exchange rate API returned an error: " + errorMessage + " (Code: " + errorCode + ")");
        }
    }

    private static void checkTheHTTPStatusCodeFirst(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            String errorBody = response.body();
            // Log the error body for debugging
            System.err.println("API call failed with status code: " + response.statusCode());
            System.err.println("Response Body: " + errorBody);
            // Throw a more specific exception or handle the error
            throw new RuntimeException("Exchange rate API call failed: " + response.statusCode() + " - " + errorBody);
        }
    }
}
