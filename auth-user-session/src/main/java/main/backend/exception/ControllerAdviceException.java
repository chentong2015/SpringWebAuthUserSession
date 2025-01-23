package main.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// TODO. 统一管理Controller中抛出的Exception
//  在异常情况下抛出对应的ResponseEntity并带有状态信息
@ControllerAdvice
public class ControllerAdviceException {

  @ExceptionHandler(ResourceConflictException.class)
  public ResponseEntity<ExceptionResponse> resourceConflict(ResourceConflictException ex) {
    ExceptionResponse response = new ExceptionResponse();
    response.setErrorId(ex.getResourceId());
    response.setErrorCode("Error Conflict");
    response.setErrorMessage(ex.getMessage());

    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }
}
