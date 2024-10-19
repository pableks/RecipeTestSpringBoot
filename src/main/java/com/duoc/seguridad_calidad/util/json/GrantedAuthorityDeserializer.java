package com.duoc.seguridad_calidad.util.json; // or .config.json if you chose that

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;

public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {
    @Override
    public GrantedAuthority deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String authority = node.get("authority").asText();
        return new SimpleGrantedAuthority(authority);
    }
}