package ru.kryz.max;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) {
        Scanner scanner =  new Scanner(System.in);

        //user input sensor name
        String sensorName = inputSensorName(scanner);

        //register new sensor request
        registerSensor(sensorName);

        //user input value
        double value =  500;
        while((value > 200) | (value < -200)){
            System.out.println("Input temperature value between -200 to 200");
            try {
                String valueStr = scanner.nextLine();
                value = Double.parseDouble(valueStr);
            }
            catch (NumberFormatException e){
                System.out.println("Error!\nInput number, characters are not available");
            }
        }

        //user input raining
       boolean isRaining = inputRainy(scanner);

        //send measurement request
        sendMeasurement(value, isRaining, sensorName);
        System.out.println("value = " +  value + "\n" + "isRaining = " + isRaining + "\n" +
                "sensorName =  " + sensorName);

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

    private static String inputSensorName(Scanner scanner){
        System.out.println("Input sensor name:");
        return scanner.nextLine();
    }

    private static boolean inputRainy(Scanner scanner){
        boolean isRaining =  false;

        System.out.println("Is it raining?Y/N");
        String rainy = scanner.nextLine();
        //instead "if" "switch case" also possible way
        if(rainy.equals("Y") | rainy.equals("Yes") | rainy.equals("yes")) {
            isRaining =  true;
        }

        return isRaining;
    }
}
