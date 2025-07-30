package com.github.osvaldsoza.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.osvaldsoza.externalclient.ExchangeRateClient;
import com.github.osvaldsoza.model.ConversionResponse;

public class CurrencyConverterHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ExchangeRateClient exchangeRateClient = new ExchangeRateClient();

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            var params = input.getQueryStringParameters();
            String from = params.get("from");
            String to = params.get("to");
            double amount = Double.parseDouble(params.get("amount"));

            double convertdValue = exchangeRateClient.convert(from, to, amount);

            ConversionResponse response = new ConversionResponse(from, to, amount, convertdValue);

            String json = objectMapper.writeValueAsString(response);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(java.util.Map.of("Content-Type", "application/json"))
                    .withBody(json);
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
