package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.BoxerResultDto;
import com.gametest.springprojekt.exception.InsufficientMoneyException;
import com.gametest.springprojekt.exception.InvalidInputValueException;
import com.gametest.springprojekt.model.CharacterEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

@Service
public class BoxerService {
    private final SecureRandom random = new SecureRandom();
    //private final int BOXER_PRICE = 10;

    private static final TreeMap<Integer, Double> PAYOUT_TABLE = new TreeMap<>();
    static {
        PAYOUT_TABLE.put(0, 0.0);
        PAYOUT_TABLE.put(100, 0.1);
        PAYOUT_TABLE.put(200, 0.2);
        PAYOUT_TABLE.put(300, 0.4);
        PAYOUT_TABLE.put(400, 0.6);
        PAYOUT_TABLE.put(500, 0.8);
        PAYOUT_TABLE.put(600, 1.0);
        PAYOUT_TABLE.put(700, 1.2);
        PAYOUT_TABLE.put(750, 1.4);
        PAYOUT_TABLE.put(800, 1.6);
        PAYOUT_TABLE.put(850, 1.8);
        PAYOUT_TABLE.put(900, 2.0);
        PAYOUT_TABLE.put(925, 2.5);
        PAYOUT_TABLE.put(950, 3.0);
        PAYOUT_TABLE.put(970, 4.0);
        PAYOUT_TABLE.put(990, 5.0);
        PAYOUT_TABLE.put(999, 10.0);
    }



    @Transactional
    public BoxerResultDto playBoxer(CharacterEntity character, int bet) {
        if (bet <= 0) {
            throw new InvalidInputValueException("Zakład musi być większy od 0!");
        }

        if (character.getMoney() < bet) {
            throw new InsufficientMoneyException("Gracz ma za mało pieniędzy!");
        }

        character.setMoney(character.getMoney() - bet);

        BoxerResultDto result = calculateResult(character.getLuck(), bet, character.getStrength());

        int winAmount = result.getWinAmount();
        character.setMoney(character.getMoney() + winAmount);

        return result;
    }


    private BoxerResultDto calculateResult(int luck, int bet, int strength) {
        int score;
        boolean isLucky = isLuckyStrike(luck);
        int strengthBonus = (int) Math.sqrt(strength/2.0);

        if (isLucky) {
            score = luckyStrike(strengthBonus);
        }
        else {
            score = normalStrike(strengthBonus);
        }

        score = Math.min(score, 999);

        double multiplier = getPayoutMultiplier(score);

        int winAmount = (int) (bet * multiplier);

        return new BoxerResultDto(score, winAmount, bet, isLucky);
    }

    private int normalStrike(int strengthBonus) {
        double roll = random.nextDouble();

        if (roll < 0.25) {
            return random.nextInt(200) + strengthBonus;
        } else if (roll < 0.50) {
            return 200 + random.nextInt(200) + strengthBonus;
        } else if (roll < 0.75) {
            return 400 + random.nextInt(200) + strengthBonus;
        } else if (roll < 0.90) {
            return 600 + random.nextInt(150) + strengthBonus;
        } else if (roll < 0.97) {
            return 750 + random.nextInt(150) + strengthBonus;
        } else if (roll < 0.99) {
            return 900 + random.nextInt(70) + strengthBonus;
        } else {
            return 970 + random.nextInt(30) + strengthBonus;
        }
    }

    private int luckyStrike(int strengthBonus) {
        return 750 + random.nextInt(250) +  strengthBonus;
    }

    private double getPayoutMultiplier(int score) {
        Map.Entry<Integer, Double> entry = PAYOUT_TABLE.floorEntry(score);
        return entry != null ? entry.getValue() : 0.0;
    }

    private boolean isLuckyStrike(int luck) {
        if (luck <= 0) {
            return false;
        }
        double chance = Math.min(0.05, Math.log10(luck) / 20.0);
        return random.nextDouble() < chance;
    }
}
