package mycrm.rest.clients;

import mycrm.exceptions.RestTemplateResponseErrorHandler;
import mycrm.models.Plugin;
import mycrm.models.TpsCheck;
import mycrm.services.PluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Primary
public class TpsCheckRestClientImpl implements TpsCheckRestClient {
    private static final Logger logger = LogManager.getLogger();
    private final PluginService pluginService;
    @Value("${tps.provider.name}")
    private String tpsApiService;
    private final RestTemplate restTemplate;

    @Autowired
    public TpsCheckRestClientImpl(PluginService pluginService, RestTemplateBuilder restTemplateBuilder) {
        this.pluginService = pluginService;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Override
    public TpsCheck callTpsApiService(String number) {
        Plugin plugin = pluginService.getPluginByApi(tpsApiService);
        String url = plugin.getUrl();
        String token = plugin.getToken();
        String outputFormat = plugin.getOutputFormat();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("token", token)
                .queryParam("output", outputFormat)
                .queryParam("number", number);

        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);

        return new TpsCheck(number, response.getBody(), response.getStatusCodeValue());
    }
}
