package baykov.daniel.springbootlibraryapp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class LibraryHTTPException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public LibraryHTTPException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
