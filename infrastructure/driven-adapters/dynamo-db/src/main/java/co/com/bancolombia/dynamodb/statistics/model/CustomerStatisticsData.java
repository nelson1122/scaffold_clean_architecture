package co.com.bancolombia.dynamodb.statistics.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
public class CustomerStatisticsData {
    @Getter(AccessLevel.NONE)
    private String id;
    private int totalCustomerContacts;
    private int claimReason;
    private int warrantyReason;
    private int doubtReason;
    private int purchaseReason;
    private int congratulationsReason;
    private int changeReason;
    private String hash;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
