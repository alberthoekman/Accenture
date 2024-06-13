package Accenture.Assessment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/** Configuration class for external API settings. */
@Configuration
public class ApiConfig {

  @Value("${external.api.base-url}")
  private String baseUrl;

  @Value("${external.api.endpoint.all}")
  private String allCountriesEndpoint;

  public String getBaseUrl() {
    return baseUrl;
  }

  public String getAllCountriesEndpoint() {
    return allCountriesEndpoint;
  }

  /**
   * Configures the RestTemplate that will handle the requests to the external API.
   *
   * @return RestTemplate
   */
  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    clientHttpRequestFactory.setConnectTimeout(120000);
    clientHttpRequestFactory.setReadTimeout(120000);
    return new RestTemplate(clientHttpRequestFactory);
  }
}
