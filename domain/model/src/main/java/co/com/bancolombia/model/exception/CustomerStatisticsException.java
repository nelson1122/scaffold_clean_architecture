package co.com.bancolombia.model.exception;

import lombok.Getter;

@Getter
public class CustomerStatisticsException extends Exception {
    private String code;

    public CustomerStatisticsException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
