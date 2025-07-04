package co.com.bancolombia.api.model.response;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatisticsResponseData {
    private String code;
    private String message;
}

