package com.streamshop.product_service.utils;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UuidUtils {

  private static final Pattern UUID_PATTERN = Pattern.compile(
      "^([a-fA-F0-9]{8})([a-fA-F0-9]{4})([a-fA-F0-9]{4})([a-fA-F0-9]{4})([a-fA-F0-9]+)$");

  public static UUID parseUuid(String input) {
    if (input == null || input.isBlank()) {
      return null;
    }

    System.out.println(input);
    try {

      // Try normal parsing first
      return UUID.fromString(input);

    } catch (IllegalArgumentException e) {
      // If it fails, try fixing format
      Matcher matcher = UUID_PATTERN.matcher(input.replaceAll("-", ""));
      if (matcher.matches()) {
        String formatted = String.format("%s-%s-%s-%s-%s",
            matcher.group(1),
            matcher.group(2),
            matcher.group(3),
            matcher.group(4),
            matcher.group(5));
        return UUID.fromString(formatted);
      }
      throw new IllegalArgumentException("Invalid UUID format: " + input);
    }
  }

  public static List<UUID> parseUuids(List<String> ids) {
    return ids.stream()
        .map(UuidUtils::parseUuid)
        .toList();
  }
}
