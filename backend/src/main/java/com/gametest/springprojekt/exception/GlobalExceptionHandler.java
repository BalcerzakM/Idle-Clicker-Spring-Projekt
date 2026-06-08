package com.gametest.springprojekt.exception;

import com.gametest.springprojekt.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BackpackIsAlreadyFullException.class)
    public ResponseEntity<ErrorDto> handleBackpackIsAlreadyFull(BackpackIsAlreadyFullException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "BACKPACK_ALREADY_FULL",
                        "Twój plecak jest już pełny!"
                ));
    }

    @ExceptionHandler(BackpackItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleBackpackItemNotFound(BackpackItemNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "BACKPACK_ITEM_NOT_FOUND",
                        "Błąd wewnętrzny serwera!"
                ));
    }

    @ExceptionHandler(CharacterNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCharacterNotFound(CharacterNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "CHARACTER_NOT_FOUND",
                        "Błąd wewnętrzny serwera!"
                ));
    }

    @ExceptionHandler(EquipmentItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEquipmentItemNotFound(EquipmentItemNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "EQUIPMENT_ITEM_NOT_FOUND",
                        "Błąd wewnętrzny serwera"
                ));
    }

    @ExceptionHandler(InsufficientMoneyException.class)
    public ResponseEntity<ErrorDto> handleInsufficientMoney(InsufficientMoneyException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "INSUFFICIENT_MONEY",
                        "Masz za mało pieniędzy!"
                ));
    }

    @ExceptionHandler(InvalidInputValueException.class)
    public ResponseEntity<ErrorDto> handleInvalidInputValue(InvalidInputValueException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "INVALID_INPUT_VALUE",
                        "Nieprawidłowa wartość: " + e.getMessage()
                ));
    }

    @ExceptionHandler(InvalidItemTypeException.class)
    public ResponseEntity<ErrorDto> handleInvalidItemType(InvalidItemTypeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "INVALID_ITEM_TYPE",
                        "Przedmiot ma nieprawidłowy typ!"
                ));
    }

    @ExceptionHandler(InvalidSlotException.class)
    public ResponseEntity<ErrorDto> handleInvalidSlotException(InvalidSlotException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "INVALID_SLOT",
                        "Nieprawidłowe miejsce na przedmiot!"
                ));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleItemNotFound(ItemNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "ITEM_NOT_FOUND",
                        "Błąd wewnętrzny serwera!"
                ));
    }

    @ExceptionHandler(NoActiveQuestException.class)
    public ResponseEntity<ErrorDto> handleNoActiveQuest(NoActiveQuestException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "NO_ACTIVE_QUEST",
                        "Nie masz aktywnego questa!"
                ));
    }

    @ExceptionHandler(NotEnoughAvailableBaseItemsException.class)
    public ResponseEntity<ErrorDto> handleNotEnoughAvailableBaseItems(NotEnoughAvailableBaseItemsException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "NOT_ENOUGH_AVAILABLE_BASE_ITEMS",
                        "Błąd wewnętrzny serwera!"
                ));
    }

    @ExceptionHandler(QuestStillActiveException.class)
    public ResponseEntity<ErrorDto> handleQuestStillActive(QuestStillActiveException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "QUEST_STILL_ACTIVE",
                        "Twój quest jest wciąż aktywny!"
                ));
    }

    @ExceptionHandler(SlotAlreadyOccupiedException.class)
    public ResponseEntity<ErrorDto> handleSlotAlreadyOccupied(SlotAlreadyOccupiedException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "SLOT_ALREADY_OCCUPIED",
                        "To miejsce jest już zajęte!"
                ));
    }

    @ExceptionHandler(QuestNotFoundException.class)
    public ResponseEntity<ErrorDto> handleQuestNotFound(QuestNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "QUEST_NOT_FOUND",
                        "Błąd wewnętrzny serwera!"
                ));
    }

    @ExceptionHandler(VehicleIsAlreadyRentedException.class)
    public ResponseEntity<ErrorDto> handleVehicleIsAlreadyRented(VehicleIsAlreadyRentedException e) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorDto(
                        "VEHICLE_IS_ALREADY_RENTED",
                        "Masz już aktywny pojazd! Najpierw anuluj posiadany pojazd w panelu postaci."
                ));
    }
}
