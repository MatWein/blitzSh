package blitzsh.app.help.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandOption implements Serializable {
    private List<String> aliases = new ArrayList<>();
    private String description;

    public CommandOption() {
    }

    public CommandOption(String description, String... aliases) {
        this.description = description;
        this.aliases = Arrays.asList(aliases);
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
