package com.ddingdongsogang.batchcrawling.job;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class CrawlingRequestJobTest {

    @Test
    public void getBoardListSuccess() throws FileNotFoundException {
        Yaml y = new Yaml();
        String basePath = System.getProperty("user.dir");
        String secretsPath = basePath + "/src/main/secrets.yml";
        Reader secretsYaml = new FileReader(secretsPath);
        Map<String, String> yamlMaps = y.load(secretsYaml);
        String boardsUrl = yamlMaps.get("boardsUrl");

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

        assertThat(ret.size()).isGreaterThan(0);
    }
}
