package co.com.bancolombia.model.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorCS {
    private String title;
    private String code;
    private String message;
}
