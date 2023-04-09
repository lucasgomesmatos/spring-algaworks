package algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApiRequestException extends RuntimeException{

    public ApiRequestException(String message) {
        super(message);
    }

}
