package blitzsh.app.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.awt.*;
import java.io.IOException;

public class ColorDeserializer extends StdDeserializer<Color> {
    protected ColorDeserializer() {
        this(Color.class);
    }

    protected ColorDeserializer(Class<Color> type) {
        super(type);
    }

    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        int red = node.get(0).numberValue().intValue();
        int green = node.get(1).numberValue().intValue();
        int blue = node.get(2).numberValue().intValue();

        return new Color(red, green, blue);
    }
}
