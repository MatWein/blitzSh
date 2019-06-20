package blitzsh.app.utils.json;

import blitzsh.app.settings.model.TerminalConfiguration;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {
    @Test
    void toJsonAndBack() {
        String json = JsonUtils.toJson(new TerminalConfiguration());
        System.out.println(json);
        JsonUtils.fromJson(TerminalConfiguration.class, json);
    }
}