package co.com.bancolombia.api.model.request;

import jakarta.validation.constraints.NotBlank;
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
public class CustomerStatisticsRequestData {
    @NotNull
    private Integer totalCustomerContacts;
    @NotNull
    private Integer claimReason;
    @NotNull
    private Integer warrantyReason;
    @NotNull
    private Integer doubtReason;
    @NotNull
    private Integer purchaseReason;
    @NotNull
    private Integer congratulationsReason;
    @NotNull
    private Integer changeReason;
    @NotNull
    @NotBlank
    private String hash;
}
