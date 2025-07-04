package co.com.bancolombia.usecase.customerstatistics;

import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import co.com.bancolombia.model.customerstatistics.gateways.CustomerStatisticsSaverRepository;
import co.com.bancolombia.model.customerstatistics.gateways.CustomerStatisticsSenderRepository;
import co.com.bancolombia.model.exception.CustomerStatisticsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;

@RequiredArgsConstructor
public class CustomerStatisticsUseCase {
    private final CustomerStatisticsSenderRepository senderRepository;
    private final CustomerStatisticsSaverRepository saverRepository;

    public Mono<Boolean> validateMD5Hash(CustomerStatistics customerStatistics) {
        return generateMD5Hash(customerStatistics.toString())
                .flatMap(hash -> {
                    if (!hash.equals(customerStatistics.getHash())) {
                        return Mono.error(() ->
                                new CustomerStatisticsException("BCS-001", "Hash does not match with the customer data"));
                    }
                    return Mono.zip(senderRepository.send(customerStatistics),
                                    saverRepository.save(customerStatistics))
                            .map(statistics -> Boolean.TRUE);
                });
    }

    private Mono<String> generateMD5Hash(String text) {
        return Mono.fromCallable(() -> {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] digest = md.digest(text.getBytes());
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : digest) {
                        hexString.append(String.format("%02x", b));
                    }
                    return hexString.toString();
                })
                .onErrorResume(Exception.class, e -> Mono.error(() ->
                        new CustomerStatisticsException("TCS-001", "Error generating MD5 hash -> " + e.getMessage())
                ));
    }
}
