package API;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class WeatherAPI {
        /**
     * Sends a GET request to the specified API URL.
     * 
     * @param apiURL the URL to send the GET request to
     * @return the response body as a String
     * @throws Exception if the request fails
     */
    private static String get(String apiURL) throws Exception {
        URI uri = new URI(apiURL);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set HTTP method and headers
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        // Check for successful response
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("GET failed. HTTP error code: " + conn.getResponseCode());
        }

        // Read response
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();
        return sb.toString();
    }

    //some variables thats getting reused
    private static final String API_KEY = EnvLoader.loadEnv("data/.env").get("OPENWEATHER_TOKEN");
    private static final Gson GSON = new Gson();

    //weather model to get the actual geolocation
    public static class GeoLocation {
        public double lat;
        public double lon;
        public String name;
        public String country;
        public String state;

        @Override
        public String toString() {
            return name + (state != null ? ", " + state : "") + ", " + country;
        }
    }

    //parsing the json response into the important fields
    public static class WeatherResponse {
        public String name;
        public Main main;
        public Weather[] weather;
        public Sys sys;

        public static class Main {
            public double temp;
            public double feels_like;
            public int humidity;
        }

        public static class Weather {
            public String main;
            public String description;
            public String icon;
        }

        public static class Sys {
            public String country;
        }
    }

    //searches for location based on user input. returns all possible matches
    public List<GeoLocation> searchLocations(String userLocation) {
        if (userLocation.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }

        try {
            String encodedLocation = URLEncoder.encode(userLocation, StandardCharsets.UTF_8);

            String url = "https://api.openweathermap.org/geo/1.0/direct?q="+encodedLocation+"&limit=10&appid="+API_KEY;
            String json = get(url);

            GeoLocation[] results = GSON.fromJson(json, GeoLocation[].class);

            if (results == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(results);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search locations", e);
        }
    }

    public WeatherResponse getWeather(double lat, double lon) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&appid="+API_KEY;
            String json = get(url);

            return GSON.fromJson(json, WeatherResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather data", e);
        }
    }

    //caching data to avoid unnecessary calls
    private WeatherResponse cachedWeather = null;
    private long lastFetchTime = 0;
    private static final long CACHE_DURATION = 10*60*1000; //10 minutes cached

    public WeatherResponse getWeatherCached(double lat, double lon) {
        long now = System.currentTimeMillis();
        
        if (cachedWeather == null || now - lastFetchTime > CACHE_DURATION) {
            try {
                cachedWeather = getWeather(lat, lon);
                lastFetchTime = now;
            } catch (Exception e) {
                System.err.println("Failed to fetch new weather data, using cached value if available");
                if (cachedWeather == null) throw e;
            }
        }
        return cachedWeather;
    }
}
