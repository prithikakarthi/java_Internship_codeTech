import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {

    // ✅ Your API key from OpenWeatherMap
    static String API_KEY = "a86eae2ee91363fa92eba956d45555eb";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                     "&appid=" + API_KEY + "&units=metric";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            if (json.get("cod").getAsInt() == 200) {
                JsonObject main = json.getAsJsonObject("main");
                double temp = main.get("temp").getAsDouble();
                int humidity = main.get("humidity").getAsInt();
                String weather = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

                System.out.println("\n--- Weather Info ---");
                System.out.println("City: " + city);
                System.out.println("Temperature: " + temp + " °C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Condition: " + weather);
            } else {
                System.out.println("API error: " + json.get("message").getAsString());
            }

        } catch (Exception e) {
            System.out.println("Failed to fetch weather data: " + e.getMessage());
        }

        scanner.close();
    }
}