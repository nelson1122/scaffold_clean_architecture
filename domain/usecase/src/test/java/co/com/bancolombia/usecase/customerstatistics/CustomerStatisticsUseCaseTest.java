package co.com.bancolombia.usecase.customerstatistics;

import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import co.com.bancolombia.model.customerstatistics.gateways.CustomerStatisticsSaverRepository;
import co.com.bancolombia.model.customerstatistics.gateways.CustomerStatisticsSenderRepository;
import co.com.bancolombia.model.exception.CustomerStatisticsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerStatisticsUseCaseTest {

    @Mock
    CustomerStatisticsSenderRepository senderRepository;
    @Mock
    CustomerStatisticsSaverRepository saverRepository;
    @InjectMocks
    CustomerStatisticsUseCase useCase;

    @Test
    void validateMD5Hash_shouldReturnTrue_whenHashMatches() {
        var customerStatistics = mock(CustomerStatistics.class);
        var data = "testData";
        var hash = "3a760fae784d30a1b50e304e97a17355";

        when(customerStatistics.toString()).thenReturn(data);
        when(customerStatistics.getHash()).thenReturn(hash);
        when(senderRepository.send(any())).thenReturn(Mono.just(customerStatistics));
        when(saverRepository.save(any())).thenReturn(Mono.just(customerStatistics));

        Mono<Boolean> result = useCase.validateMD5Hash(customerStatistics);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateMD5Hash_shouldReturnError_whenHashDoesNotMatch() {
        var customerStatistics = mock(CustomerStatistics.class);
        var data = "testData";
        var hash = "invalidHash";

        when(customerStatistics.toString()).thenReturn(data);
        when(customerStatistics.getHash()).thenReturn(hash);

        Mono<Boolean> result = useCase.validateMD5Hash(customerStatistics);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof CustomerStatisticsException
                        && ((CustomerStatisticsException) throwable).getCode().equals("BCS-001"))
                .verify();
    }
}