package com.luizaprestes.challenge.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.luizaprestes.challenge.exception.JsonException;

public final class JacksonAdapter {

  private static final JacksonAdapter INSTANCE;

  static {
    INSTANCE = new JacksonAdapter();
  }

  private final ObjectMapper mapper;

  private JacksonAdapter() {
    this.mapper = new ObjectMapper();
  }

  public String serialize(final Object value) throws JsonException {
    try {
      return mapper.writeValueAsString(value);
    } catch (Exception exception) {
      throw new JsonException(exception);
    }
  }

  public <T> T deserialize(final String json, final Class<T> clazz)
      throws JsonException {
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception exception) {
      throw new JsonException(exception);
    }
  }

  public JsonNode get(final ObjectNode node, final String key) {
    final JsonNode jsonNode = node.get(key);
    if (jsonNode == null) {
      throw new NullPointerException("The key specified cannot be null");
    }

    return jsonNode;
  }

  public static JacksonAdapter getInstance() {
    return INSTANCE;
  }

}
