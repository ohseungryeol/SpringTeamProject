package com.example.backend.web.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class MetaImageController {

    @GetMapping("/metaimage")
    public String getMetaImage(@RequestParam String url) {

        try {
            int index = url.indexOf('?');
            Document doc;

            if (index > 0) {
                String prefix = url.substring(0, index + 1);
                String queryString = url.substring(index + 1);
                queryString = URLEncoder.encode(queryString, StandardCharsets.UTF_8);
                doc = Jsoup.connect(prefix + queryString).get();
            } else {
                doc = Jsoup.connect(url).get();
            }

            return doc.select("meta[property=og:image]").get(0).attr("content");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
