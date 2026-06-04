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
                        e.getMessage()
                ));
    }

    @ExceptionHandler(BackpackItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleBackpackItemNotFound(BackpackItemNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "BACKPACK_ITEM_NOT_FOUND",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(EquipmentItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEquipmentItemNotFound(EquipmentItemNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "EQUIPMENT_ITEM_NOT_FOUND",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(InsufficientMoneyException.class)
    public ResponseEntity<ErrorDto> handleInsufficientMoney(InsufficientMoneyException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "INSUFFICIENT_MONEY",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(InvalidItemTypeException.class)
    public ResponseEntity<ErrorDto> handleInvalidItemType(InvalidItemTypeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "INVALID_ITEM_TYPE",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(InvalidSlotException.class)
    public ResponseEntity<ErrorDto> handleInvalidSlotException(InvalidSlotException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "INVALID_SLOT",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleItemNotFound(ItemNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "ITEM_NOT_FOUND",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(NoActiveQuestException.class)
    public ResponseEntity<ErrorDto> handleNoActiveQuest(NoActiveQuestException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "NO_ACTIVE_QUEST",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(NotEnoughAvailableBaseItemsException.class)
    public ResponseEntity<ErrorDto> handleNotEnoughAvailableBaseItems(NotEnoughAvailableBaseItemsException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(
                        "NOT_ENOUGH_AVAILABLE_BASE_ITEMS",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(QuestStillActiveException.class)
    public ResponseEntity<ErrorDto> handleQuestStillActive(QuestStillActiveException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "QUEST_STILL_ACTIVE",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(SlotAlreadyOccupiedException.class)
    public ResponseEntity<ErrorDto> handleSlotAlreadyOccupied(SlotAlreadyOccupiedException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(
                        "SLOT_ALREADY_OCCUPIED",
                        e.getMessage()
                ));
    }


}
