package ru.kryz.max;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) {
        Scanner scanner =  new Scanner(System.in);

        System.out.println("Input sensor name:");
        String sensorName =  scanner.nextLine();
        //register new sensor
        registerSensor(sensorName);

        //send measurement
        sendMeasurement(20.22, false, sensorName);

        scanner.close();
    }

    private static void registerSensor(String sensorName){
        final String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData =  new HashMap<>();
        jsonData.put("name", sensorName);

        doPostRequestWithJSON(url, jsonData);
    }

    private static void sendMeasurement(double value, boolean raining, String sensorName){
        final String url = "http://localhost:8080/meas/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));

        doPostRequestWithJSON(url, jsonData);
    }

    private static void doPostRequestWithJSON(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate =  new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        //determine type
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Wrap
        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        //send
        try {
            restTemplate.postForObject(url, request, String.class);
            System.out.println("Request was successfully sent to the server");
        } catch (HttpClientErrorException e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }
}
