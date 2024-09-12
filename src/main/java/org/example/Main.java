package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.WeatherInfoDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {

        // Create an HttpClient instance that follows redirects
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)  // Follow redirects
                .build();

        try {
            // Construct the weather API request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://vejr.eu/api.php?location=Roskilde&degree=C"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the status code and parse the JSON response if successful
            if (response.statusCode() == 200) {
                // Create an ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse the JSON response into a JsonNode
                JsonNode jsonNode = objectMapper.readTree(response.body());

                // Extract relevant fields and map them to WeatherInfoDTO
                WeatherInfoDTO weatherInfo = new WeatherInfoDTO();
                weatherInfo.setLocationName(jsonNode.get("LocationName").asText());

                // Extract weather data from "CurrentData"
                JsonNode currentData = jsonNode.get("CurrentData");
                weatherInfo.setTemperature(currentData.get("temperature").asInt());
                weatherInfo.setTypeOfWeather(currentData.get("skyText").asText());
                weatherInfo.setHumidity(currentData.get("humidity").asInt());

                // Extract wind speed as a double and convert to int if necessary
                String windText = currentData.get("windText").asText();
                double windSpeed = Double.parseDouble(windText.split(" ")[0]);
                weatherInfo.setWindSpeed((int) windSpeed);  // Convert to int if that's the desired type

                // Print the weather details
                System.out.println(weatherInfo);
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
