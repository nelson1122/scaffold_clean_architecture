package co.com.bancolombia.model.customerstatistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerStatistics {
    private Integer totalCustomerContacts;
    private Integer claimReason;
    private Integer warrantyReason;
    private Integer doubtReason;
    private Integer purchaseReason;
    private Integer congratulationsReason;
    private Integer changeReason;
    private String hash;

    @Override
    public String toString() {
        return this.totalCustomerContacts + "," +
                this.claimReason + "," +
                this.warrantyReason + "," +
                this.doubtReason + "," +
                this.purchaseReason + "," +
                this.congratulationsReason + "," +
                this.changeReason;
    }
}
