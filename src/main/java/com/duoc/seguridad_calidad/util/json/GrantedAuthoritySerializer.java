package com.duoc.seguridad_calidad.util.json; // or .config.json if you chose that

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;

public class GrantedAuthoritySerializer extends JsonSerializer<GrantedAuthority> {
    @Override
    public void serialize(GrantedAuthority value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("authority", value.getAuthority());
        gen.writeEndObject();
    }
}