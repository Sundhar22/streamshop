package com.streamshop.order.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Converter
@RequiredArgsConstructor
public class JsonToMapConverter implements AttributeConverter<Map<String, Object>, String> {

  private final ObjectMapper objectMapper;

  @Override
  public String convertToDatabaseColumn(Map<String, Object> attribute) {
    try {
      System.out.println(attribute.toString());
      return objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
      System.out.println(e.toString());

      return "{}";
    }
  }

  @Override
  public Map<String, Object> convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {
      });
    } catch (Exception e) {
      return new HashMap<>();
    }
  }
}
