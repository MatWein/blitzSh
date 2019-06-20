package blitzsh.app.settings.model;

import blitzsh.app.utils.interfaces.IName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TerminalConfigurationFolder implements Serializable, IName {
    private String name;

    private List<TerminalConfigurationFolder> folders = new ArrayList<>();
    private List<TerminalConfiguration> configurations = new ArrayList<>();
    private List<SshConfiguration> sshConfigurations = new ArrayList<>();

    public TerminalConfigurationFolder() {
    }

    public TerminalConfigurationFolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TerminalConfigurationFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<TerminalConfigurationFolder> folders) {
        this.folders = folders;
    }

    public List<TerminalConfiguration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<TerminalConfiguration> configurations) {
        this.configurations = configurations;
    }

    public List<SshConfiguration> getSshConfigurations() {
        return sshConfigurations;
    }

    public void setSshConfigurations(List<SshConfiguration> sshConfigurations) {
        this.sshConfigurations = sshConfigurations;
    }
}
