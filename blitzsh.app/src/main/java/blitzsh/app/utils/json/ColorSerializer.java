package blitzsh.app.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.awt.*;
import java.io.IOException;

public class ColorSerializer extends StdSerializer<Color> {
    protected ColorSerializer() {
        this(Color.class);
    }

    protected ColorSerializer(Class<Color> type) {
        super(type);
    }

    @Override
    public void serialize(Color value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray(3);
        gen.writeNumber(value.getRed());
        gen.writeNumber(value.getGreen());
        gen.writeNumber(value.getBlue());
        gen.writeEndArray();
    }
}
