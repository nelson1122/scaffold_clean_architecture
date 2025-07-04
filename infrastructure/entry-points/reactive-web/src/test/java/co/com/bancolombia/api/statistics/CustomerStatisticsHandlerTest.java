package co.com.bancolombia.api.statistics;

import co.com.bancolombia.api.RouterRest;
import co.com.bancolombia.api.model.request.CustomerStatisticsRequest;
import co.com.bancolombia.api.model.request.CustomerStatisticsRequestData;
import co.com.bancolombia.model.exception.CustomerStatisticsException;
import co.com.bancolombia.usecase.customerstatistics.CustomerStatisticsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerStatisticsHandlerTest {
    @Mock
    Validator validator;
    @Mock
    CustomerStatisticsUseCase customerStatisticsUseCase;
    @InjectMocks
    CustomerStatisticsHandler customerStatisticsHandler;
    @Autowired
    WebTestClient webTestClient;

    CustomerStatisticsRequest request;

    @BeforeEach
    void setUp() {
        var data = CustomerStatisticsRequestData.builder()
                .totalCustomerContacts(10).claimReason(2).warrantyReason(1).doubtReason(3)
                .purchaseReason(4).congratulationsReason(0).changeReason(1).hash("abc123hash")
                .build();
        request = CustomerStatisticsRequest.builder().data(data).build();
        webTestClient = WebTestClient.bindToRouterFunction(
                new RouterRest().routerFunction(customerStatisticsHandler)).build();
    }

    @Test
    void processBodyTest() {
        when(customerStatisticsUseCase.validateMD5Hash(any())).thenReturn(Mono.just(Boolean.TRUE));
        webTestClient.post()
                .uri("/api/v1/stats")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerStatisticsRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerStatisticsRequest.class);
    }

    @Test
    void processBodyErrorTest() {
        when(customerStatisticsUseCase.validateMD5Hash(any()))
                .thenReturn(Mono.error(() -> new CustomerStatisticsException("TCST001", "Invalid hash value")));
        webTestClient.post()
                .uri("/api/v1/stats")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerStatisticsRequest.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomerStatisticsRequest.class);
    }

    @Test
    void processBodyRequiredFieldsErrorTest() {
        request.setData(CustomerStatisticsRequestData.builder().build());
        webTestClient.post()
                .uri("/api/v1/stats")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerStatisticsRequest.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomerStatisticsRequest.class);
    }
}