package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.PremiumOfferDto;
import com.gametest.springprojekt.exception.PaymentUnsuccessfulException;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.TransactionEntity;
import com.gametest.springprojekt.model.enums.CristalsPackage;
import com.gametest.springprojekt.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PremiumShopService {
    private final TransactionRepository transactionRepository;
    private final Random random = new Random();

    public PremiumShopService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<PremiumOfferDto> getPremiumOffers() {
        List<PremiumOfferDto> premiumOffers = new ArrayList<>();

        for (CristalsPackage cristalsPackage : CristalsPackage.values()) {
            premiumOffers.add(generatePremiumOfferDto(cristalsPackage));
        }

        return premiumOffers;
    }

    private PremiumOfferDto generatePremiumOfferDto(CristalsPackage cristalsPackage) {
        return new PremiumOfferDto(cristalsPackage.name(), cristalsPackage.getPriceInGrosze(), cristalsPackage.getCristals());
    }

    @Transactional
    public PremiumOfferDto buyPackage(String packageCode, CharacterEntity character) {
        CristalsPackage cristalsPackage = CristalsPackage.valueOf(packageCode);

        boolean wasPaymentSuccessful;

        //tutaj jakas tam platnosc
        wasPaymentSuccessful = random.nextBoolean();

        if (!wasPaymentSuccessful) {
            throw new PaymentUnsuccessfulException("Błąd podczas wykonywania płatności");
        }

        character.setCristals(character.getCristals() + cristalsPackage.getCristals());

        saveTransaction(character, cristalsPackage);

        return generatePremiumOfferDto(cristalsPackage);
    }

    private void saveTransaction(CharacterEntity character, CristalsPackage cristalsPackage) {
        TransactionEntity transactionEntity = new TransactionEntity(
                null,
                character,
                cristalsPackage.name(),
                cristalsPackage.getPriceInGrosze(),
                cristalsPackage.getCristals(),
                LocalDateTime.now()
                );
        transactionRepository.save(transactionEntity);
    }
}


