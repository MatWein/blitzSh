package blitzsh.app.settings.model;

import java.util.HashMap;
import java.util.Map;

public class TerminalConfiguration extends BaseConfiguration {
    private String[] command;
    private String workingDir;
    private String charset;
    private Map<String, String> environment = new HashMap<>();

    private boolean console;
    private boolean cygwin;

    public TerminalConfiguration() {
    }

    public TerminalConfiguration(String name) {
        super(name);
    }

    public String[] getCommand() {
        return command;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public boolean isCygwin() {
        return cygwin;
    }

    public void setCygwin(boolean cygwin) {
        this.cygwin = cygwin;
    }
}
