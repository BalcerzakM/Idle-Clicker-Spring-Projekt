package com.gametest.springprojekt.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gametest.springprojekt.dto.GangCombatDto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false) //właczamy tylko kiedu faktycznie potrzebne
public class GangCombatDtoJsonConverter implements AttributeConverter<GangCombatDto, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(GangCombatDto attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Błąd serializacji GangCombatDto do JSON", e);
        }
    }

    @Override
    public GangCombatDto convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return objectMapper.readValue(dbData, GangCombatDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Błąd deserializacji JSON do GangCombatDto", e);
        }
    }
}