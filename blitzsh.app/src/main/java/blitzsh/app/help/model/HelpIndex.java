package blitzsh.app.help.model;

import blitzsh.app.common.model.enums.TerminalType;

import java.util.concurrent.ConcurrentHashMap;

public class HelpIndex extends ConcurrentHashMap<String, CommandHelp> {
    private TerminalType terminalType;

    public TerminalType getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(TerminalType terminalType) {
        this.terminalType = terminalType;
    }
}
