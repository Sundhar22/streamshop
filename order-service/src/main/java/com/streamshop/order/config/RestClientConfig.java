package com.streamshop.order.config;

import io.micrometer.observation.ObservationRegistry;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.client.RestClient;
    import org.springframework.web.client.support.RestClientAdapter;
    import org.springframework.web.service.invoker.HttpServiceProxyFactory;
    import org.springframework.http.client.SimpleClientHttpRequestFactory;

    import com.streamshop.order.Client.InventoryClient;

    @Configuration
    public class RestClientConfig {

      @Value("${inventory.url}")
      private String inventoryServiceUrl;
//
      @Autowired
      private ObservationRegistry observationRegistry;

      @Bean
      public InventoryClient inventoryClient() {
        RestClient restClient = RestClient.builder()
            .baseUrl(inventoryServiceUrl)
            .requestFactory(getClientRequestFactory())
            .observationRegistry(observationRegistry)
            .build();

        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(InventoryClient.class);
      }

      private SimpleClientHttpRequestFactory getClientRequestFactory() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return factory;
      }
    }