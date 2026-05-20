package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.BoxerResultDto;
import com.gametest.springprojekt.exception.InsufficientMoneyException;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

@Service
public class BoxerService {
    private SecureRandom random = new SecureRandom();
    private final int BOXER_PRICE = 10;

    private static final TreeMap<Integer, Integer> PAYOUT_TABLE = new TreeMap<>();
    static {
        PAYOUT_TABLE.put(0, -10);
        PAYOUT_TABLE.put(100, -7);
        PAYOUT_TABLE.put(200, -5);
        PAYOUT_TABLE.put(300, -3);
        PAYOUT_TABLE.put(400, -2);
        PAYOUT_TABLE.put(500, -1);
        PAYOUT_TABLE.put(600, 0);
        PAYOUT_TABLE.put(700, 1);
        PAYOUT_TABLE.put(750, 2);
        PAYOUT_TABLE.put(800, 3);
        PAYOUT_TABLE.put(850, 5);
        PAYOUT_TABLE.put(900, 7);
        PAYOUT_TABLE.put(950, 10);
        PAYOUT_TABLE.put(970, 15);
        PAYOUT_TABLE.put(990, 25);
        PAYOUT_TABLE.put(999, 50);
    }



    @Transactional
    public BoxerResultDto playBoxer(CharacterEntity character) {
        if (character.getMoney() < BOXER_PRICE) {
            throw new InsufficientMoneyException("Gracz ma za malo pieniedzy!");
        }

        character.setMoney(character.getMoney() - BOXER_PRICE);

        BoxerResultDto result = calculateResult(character.getLuck());

        result.setNewMoney(character.getMoney());

        if (result.getRizzChange() != 0) {
            int newRizz = character.getRizz() + result.getRizzChange();
            result.setNewRizz(newRizz);
            character.setRizz(Math.max(0, newRizz));
        } else {
            result.setNewRizz(character.getRizz());
        }

        return result;
    }


    private BoxerResultDto calculateResult(int luck) {
        int result;
        boolean isLucky = isLuckyStrike(luck);

        if (isLucky) {
            result = luckyStrike();
        }
        else {
            result = normalStrike();
        }

        int rizzChange = calculateRizzChange(result);


        return new BoxerResultDto(result, rizzChange,0, 0, isLucky);
    }

    private int normalStrike() {
        double roll = random.nextDouble();

        if (roll < 0.30) {
            return random.nextInt(200);
        } else if (roll < 0.55) {
            return 200 + random.nextInt(200);
        } else if (roll < 0.75) {
            return 400 + random.nextInt(200);
        } else if (roll < 0.88) {
            return 600 + random.nextInt(150);
        } else if (roll < 0.95) {
            return 750 + random.nextInt(150);
        } else if (roll < 0.99) {
            return 900 + random.nextInt(70);
        } else {
            return 970 + random.nextInt(30);
        }
    }

    private int luckyStrike() {
        return 850 + random.nextInt(150);
    }

    private int calculateRizzChange(int result) {
        Map.Entry<Integer, Integer> entry = PAYOUT_TABLE.floorEntry(result);
        return entry != null ? entry.getValue() : 0;
    }

    private boolean isLuckyStrike(int luck) {
        if (luck <= 0) {
            return false;
        }
        double chance = Math.min(0.05, Math.log10(luck) / 20.0);
        return random.nextDouble() < chance;
    }
}
