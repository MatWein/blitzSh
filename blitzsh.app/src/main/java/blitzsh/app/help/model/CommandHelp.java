package blitzsh.app.help.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandHelp implements Serializable {
    private String description;
    private String syntax;
    private String manual;

    private List<CommandOption> options = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public List<CommandOption> getOptions() {
        return options;
    }

    public void setOptions(List<CommandOption> options) {
        this.options = options;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }
}
