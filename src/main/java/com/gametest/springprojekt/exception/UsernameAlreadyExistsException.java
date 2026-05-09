package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends Throwable {
    public UsernameAlreadyExistsException(String s) {
        super("Użytkownik o nazwie: "+s+ "już istnieje");
    }
}
