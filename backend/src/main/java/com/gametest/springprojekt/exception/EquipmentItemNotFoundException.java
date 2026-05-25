package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EquipmentItemNotFoundException extends RuntimeException {
  public EquipmentItemNotFoundException(String message) {
    super(message);
  }
}
