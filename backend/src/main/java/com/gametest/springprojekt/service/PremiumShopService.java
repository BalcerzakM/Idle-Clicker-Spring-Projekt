package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.PremiumOfferDto;
import com.gametest.springprojekt.model.enums.CristalsPackage;
import com.gametest.springprojekt.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PremiumShopService {
    TransactionRepository transactionRepository;

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
        return new PremiumOfferDto(cristalsPackage.name(), cristalsPackage.getPrice(), cristalsPackage.getCristals());
    }
}


