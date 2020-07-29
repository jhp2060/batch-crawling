package com.ddingdongsogang.batchcrawling.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CrawlingRequestJobConfiguration {

    @Scheduled(cron = "0 0 * * * *")
    public void run() throws FileNotFoundException {
        log.info("crawling started.");
        Yaml y = new Yaml();
        String basePath = System.getProperty("user.dir");
        String secretsPath = basePath + "/src/main/secrets.yml";
        Reader secretsYaml = new FileReader(secretsPath);
        Map<String, String> yamlMaps = y.load(secretsYaml);
        String boardsUrl = yamlMaps.get("boardsUrl");

        ArrayList<Integer> boardIdList = getBoardIdList(boardsUrl);

    }

    public static ArrayList<Integer> getBoardIdList(String boardsUrl) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        HttpComponentsClientHttpRequestFactory factory
                = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setConnectionRequestTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(boardsUrl).build();

        ResponseEntity<List> responseEntity =
                restTemplate.getForEntity(uri.toString(), List.class);

        List<LinkedHashMap<String, String>> boards = responseEntity.getBody();

        for (LinkedHashMap<?, ?> board : boards) {
            ret.add((Integer) board.get("id"));
        }

        return ret;
    }
}
