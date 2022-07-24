package mycrm.rest.clients;

import mycrm.exceptions.RestTemplateResponseErrorHandler;
import mycrm.models.DiallerAPIResponse;
import mycrm.models.Plugin;
import mycrm.search.DiallerResponseService;
import mycrm.services.PluginService;
import mycrm.upload.DiallerDatasetResponse;
import mycrm.upload.DiallerDatasetType;
import mycrm.upload.DiallerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Primary
public class DiallerRestClientImpl implements DiallerRestClient {
    private static Logger logger = LogManager.getLogger();

    private final DiallerResponseService diallerResponseService;
    private final PluginService pluginService;
    @Value("${dialler.provider.name}")
    private String diallerProviderName;

    private RestTemplate restTemplate;

    @Autowired
    public DiallerRestClientImpl(DiallerResponseService diallerResponseService, PluginService pluginService,
                                 RestTemplateBuilder restTemplateBuilder) {
        this.diallerResponseService = diallerResponseService;
        this.pluginService = pluginService;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Override
    public DiallerAPIResponse getDiallerToken() {
        return authenticateWithDiallerService();
    }

    private DiallerAPIResponse authenticateWithDiallerService() {
        logger.info("Attempting to make a dialler token call");

        String url = getBaseUrl() + "/token.php";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("action", "get")
                .queryParam("username", diallerPlugin().getUsername())
                .queryParam("password", diallerPlugin().getPassword());

        restTemplate.getMessageConverters().add(getMappingJackson2HttpMessageConverter());

        DiallerAPIResponse response = restTemplate.getForObject(builder.toUriString(), DiallerAPIResponse.class);

        recordDiallerResponse(200, response.toString());

        return response;
    }

    private String getBaseUrl() {
        return diallerPlugin().getUrl();
    }

    @Override
    public DiallerAPIResponse sendDataToDiallerServer(String filePath, DiallerDatasetType type) {
        DiallerAPIResponse token = getDiallerToken();

        if (!token.isSuccess()) {
            logger.info("Authentication with dialler failed");
            return null;
        }

        logger.info("Authentication with dialler successful");

        logger.info("Posting {} file to server {}", type.getMethod(), filePath);
        return postFileToDiallerServer(token.getToken(), type.getMethod(), filePath);
    }

    @Override
    public void updateDataset(String dataset, String status) {
        String url = getBaseUrl() + "/ecnow.php";
        String method = "ecnow_datasets";
        String action = "update";

        JSONArray requestBody = new JSONArray();
        JSONObject request = new JSONObject();
        request.put("qid", "1644053");
        request.put("dataset", dataset);
        request.put("d_status", status);
        request.put("d_priority", 90);
        request.put("notes", "Updated by CUC CRM");
        request.put("reccount", "");
        requestBody.add(request);

        UriComponentsBuilder builder = buildURLParameters(getDiallerToken().getToken(), action, url, method);
        builder.queryParam("raw", "1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.toUriString(), requestEntity, String.class);

        recordDiallerResponse(response.getStatusCodeValue(), response.toString());

        logger.info("Response from dataset update: {}", response.getBody());
    }

    @Override
    public DiallerDatasetResponse getDatasets() {
        String url = getBaseUrl() + "/ecnow.php";
        String method = "ecnow_datasets";
        String action = "read";

        UriComponentsBuilder builder = buildURLParameters(getDiallerToken().getToken(), action, url, method);

        ResponseEntity<DiallerDatasetResponse> response = restTemplate.getForEntity(builder.toUriString(), DiallerDatasetResponse.class);

        if (response.getBody().getSuccess().equals("true")) {
            return response.getBody();
        }

        return null;
    }

    private DiallerAPIResponse postFileToDiallerServer(String token, String action, String filePath) {

        String postMethod = "RAW"; // FILE_BASED

        String url = getBaseUrl() + "/ecnow.php";
        String method = "ecnow_records";

        String fileResourcePath = filePath;

        logger.info("Dialler File path: {}", fileResourcePath);
        Resource file = getFile(fileResourcePath);

        if (!file.exists()) {
            logger.info("Dialler file does not exist");
            return new DiallerAPIResponse(false, "Dialler file does not exist");
        }

        UriComponentsBuilder builder = buildURLParameters(token, action, url, method);

        restTemplate.getMessageConverters().add(getMappingJackson2HttpMessageConverter());

        if ("RAW".equals(postMethod)) {
            return sendJSONData(filePath, builder);
        }

        return sendJSONDataFile(file, builder);
    }

    private DiallerAPIResponse sendJSONData(String filePath, UriComponentsBuilder builder) {
        builder.queryParam("raw", "1");

        try {
            JSONParser parser = new JSONParser();
            JSONArray fileData = (JSONArray) parser.parse(new FileReader(filePath));

            logger.info("Sending raw JSON data to dialler");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(fileData.toString(), headers);

            logger.info("Dialler request made");

            DiallerAPIResponse response = restTemplate.postForObject(builder.toUriString(), requestEntity, DiallerAPIResponse.class);

            recordDiallerResponse(200, response.toString());

            return response;
        } catch (Exception e) {
            logger.error("Oops, there was an error when trying to post raw data", e.getMessage());

            DiallerAPIResponse response = new DiallerAPIResponse();
            response.setError("Oops, there was an error when trying to post raw data");

            recordDiallerResponse(200, response.toString());

            return response;
        }
    }

    private DiallerAPIResponse sendJSONDataFile(Resource file, UriComponentsBuilder builder) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("easycall", file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        logger.info("Dialler request made: {}", requestEntity);

        DiallerAPIResponse response = restTemplate.postForObject(builder.toUriString(), requestEntity, DiallerAPIResponse.class);

        recordDiallerResponse(200, response.toString());

        return response;
    }

    private UriComponentsBuilder buildURLParameters(String token, String action, String url, String method) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("format", diallerPlugin().getOutputFormat())
                .queryParam("action", action)
                .queryParam("method", method)
                .queryParam("token", token);
    }

    private Resource getFile(String path) {
        Path file = Paths.get(path);
        File returnedFile = file.toFile();

        return new FileSystemResource(returnedFile);
    }

    private MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.MULTIPART_FORM_DATA));
        return mappingJackson2HttpMessageConverter;
    }

    private Plugin diallerPlugin() {
        return this.pluginService.getPluginByApi(diallerProviderName);
    }

    private void recordDiallerResponse(int statusCode, String response) {
        diallerResponseService.saveDiallerResponse(new DiallerResponse(statusCode, response, LocalDateTime.now()));
    }
}
