package com.example.backend.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class GeoLocationUtil {

    @Value("${GOOGLE_GEOCODING_API_KEY}")
    private String API_KEY;
    private final static String PRE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    public Double[] parseLocationToLngLat(String location) {
        log.info("---------- 구글맵 util 시작 ------------");
        try {
            log.info("[구글맵 api key] : {}", API_KEY);
            URL url = new URL(PRE_URL + URLEncoder.encode(location, StandardCharsets.UTF_8) + "&key=" + API_KEY);
            InputStream is = url.openConnection().getInputStream();

            log.info("[구글맵 url] : {}", url.getContent());

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder responseStringBuilder = new StringBuilder();
            String inputString;

            while ((inputString = streamReader.readLine()) != null) {
                responseStringBuilder.append(inputString);
            }

            JsonObject jo = (JsonObject) JsonParser.parseString(responseStringBuilder.toString());
            JsonArray results = jo.getAsJsonArray("results");
            String region = null;
            String province = null;
            String zip = null;
            if (results.size() > 0) {
                JsonObject jsonObject = results.get(0).getAsJsonObject();
                Double lat = jsonObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsDouble();
                Double lng = jsonObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsDouble();

                return new Double[] {lng, lat};
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
