package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoneyAndAvatarDto {
    private int money;
    private int cristals;
    private String avatarPicture;
}
