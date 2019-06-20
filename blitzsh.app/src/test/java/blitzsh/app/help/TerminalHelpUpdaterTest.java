package blitzsh.app.help;

import blitzsh.app.common.model.enums.TerminalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TerminalHelpUpdaterTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void updateHelp() {
        TerminalHelpUpdater.updateHelp(TerminalType.SHELL_MAN, "C:\\Windows\\System32\\bash.exe");
    }
}