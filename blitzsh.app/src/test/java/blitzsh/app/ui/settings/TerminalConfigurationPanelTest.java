package blitzsh.app.ui.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TerminalConfigurationPanelTest {
    @Test
    void testRegex() {
        Assertions.assertArrayEquals(new String[] { "command" }, TerminalConfigurationPanel.splitCommand("command"));
        Assertions.assertArrayEquals(new String[] { "java", "-jar" }, TerminalConfigurationPanel.splitCommand("java -jar"));
        Assertions.assertArrayEquals(new String[] { "java", "-jar", "C:\\someFile.jar" }, TerminalConfigurationPanel.splitCommand("java -jar C:\\someFile.jar"));
        Assertions.assertArrayEquals(new String[] { "java", "-jar", "C:\\someFile with spaces.jar" }, TerminalConfigurationPanel.splitCommand("java -jar \"C:\\someFile with spaces.jar\""));
    }
}