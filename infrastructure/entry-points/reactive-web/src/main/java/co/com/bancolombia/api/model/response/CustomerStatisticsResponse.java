package co.com.bancolombia.api.model.response;

import co.com.bancolombia.model.error.ErrorCS;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatisticsResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private CustomerStatisticsResponseData data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private ErrorCS error;
}
