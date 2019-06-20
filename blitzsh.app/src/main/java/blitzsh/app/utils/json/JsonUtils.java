package blitzsh.app.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());
        module.addDeserializer(Color.class, new ColorDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }

    public static void toJsonFile(Object object, File targetFile) {
        try {
            OBJECT_MAPPER.writeValue(targetFile, object);
        } catch (Throwable e) {
            String message = String.format("Could not serialize %s to JSON. Target file: %s", object, targetFile);
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Throwable e) {
            String message = String.format("Could not serialize %s to JSON.", object);
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static <T> T fromJsonFile(Class<T> type, File targetFile) {
        try {
            return OBJECT_MAPPER.readValue(targetFile, type);
        } catch (Throwable e) {
            String message = String.format("Could not deserialize JSON from file %s to %s.", targetFile, type.getName());
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static <T> T fromJson(Class<T> type, String json) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Throwable e) {
            String message = String.format("Could not deserialize JSON from JSON '%s' to %s.", json, type.getName());
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
