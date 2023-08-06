package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "CustomBadRequestException Bad Request!")
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") // reason => messages.properties 사용해서, Error Message 처리 가능하다.
public class CustomBadRequestException extends RuntimeException{
}
