package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ShopItemDto;
import com.gametest.springprojekt.model.BaseItemEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.ShopOfferEntity;
import com.gametest.springprojekt.repository.BaseItemRepository;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.ItemRepository;
import com.gametest.springprojekt.repository.ShopOfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ItemShopService {
    private final ItemRepository itemRepository;
    private final SecureRandom random = new SecureRandom();
    private final ShopOfferRepository shopOfferRepository;
    private final BaseItemRepository baseItemRepository;
    private final CharacterRepository characterRepository;
    private final int NUMBER_OF_SHOP_ITEMS = 3; //narazie 3 zeby w dataloaderze nie dodawac za duzo

    public ItemShopService(ItemRepository itemRepository, ShopOfferRepository shopOfferRepository, BaseItemRepository baseItemRepository, CharacterRepository characterRepository) {
        this.itemRepository = itemRepository;
        this.shopOfferRepository = shopOfferRepository;
        this.baseItemRepository = baseItemRepository;
        this.characterRepository = characterRepository;
    }

    //sciaga dzisiejsza oferte z bazy danych
    public List<ShopItemDto> getTodayShopItemDto() {
        LocalDate today = LocalDate.now();

        List<ShopOfferEntity> offers = shopOfferRepository.findByOfferDate(today);

        List<ShopItemDto> shopItemDtos = new ArrayList<>();

        for (ShopOfferEntity offerEntity : offers) {
            shopItemDtos.add(generateShopItemDto(offerEntity));
        }

        return shopItemDtos;
    }

    //generuje dto na podstawie oferty sciagnietej z bazy
    private ShopItemDto generateShopItemDto(ShopOfferEntity shopOfferEntity) {
        ItemEntity item = shopOfferEntity.getItem();
        return new ShopItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getSlotType(),
                item.getBonusRizz(),
                item.getBonusStrength(),
                item.getBonusAgility(),
                item.getBonusEndurance(),
                item.getBonusLuck(),
                item.getPrice(),
                item.getImagePath()
        );
    }

    //o podanej godzinie (6 rano) odswieza oferte dla kazdego charactera w bazie
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void refreshShopOffers() {
        List<CharacterEntity> characters = characterRepository.findAll();

        for (CharacterEntity character : characters) {
            //nad tym trzeba bedzie sie zastanowic
            shopOfferRepository.deleteByCharacter(character);

            List<ShopOfferEntity> offers = new ArrayList<>();

            for (int i = 0; i < NUMBER_OF_SHOP_ITEMS; i++) {
                offers.add(generateRandomShopOffer(character));
            }

            shopOfferRepository.saveAll(offers);
        }
    }

    //tworzy jeden item do oferty
    private ShopOfferEntity generateRandomShopOffer(CharacterEntity character) {
        List<ShopOfferEntity> presentOffers = shopOfferRepository.findByCharacterAndOfferDate(character, LocalDate.now());

        List<BaseItemEntity> baseItems = baseItemRepository.findAll();

        BaseItemEntity basicItem = baseItems.get(random.nextInt(baseItems.size()));

        return new ShopOfferEntity(null, LocalDate.now(), generateItemEntity(basicItem, character), character);
    }

    //tworzy item na podstawie basicItema z dynamicznymi statystykami, na podstawie aury itp, tutaj da sie algorytmy do liczenia tego
    private ItemEntity generateItemEntity(BaseItemEntity baseItemEntity, CharacterEntity character) {
        return new ItemEntity(
                null,
                baseItemEntity.getName(),
                baseItemEntity.getDescription(),
                baseItemEntity.getSlotType(),
                baseItemEntity.getBaseRizz() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseStrength() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseAgility() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseEndurance() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseLuck() + random.nextInt(character.getAura()),
                baseItemEntity.getBasePrice() + random.nextInt(character.getAura()),
                baseItemEntity.getImagePath()
        );
    }

}
