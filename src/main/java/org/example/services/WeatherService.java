package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.WeatherInfoDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    public static WeatherInfoDTO fetchWeatherDataByLocationName(String locationName) {
        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "https://vejr.eu/api.php?location=" + locationName + "&degree=C";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), WeatherInfoDTO.class);
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}