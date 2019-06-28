package blitzsh.shell;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.TimeUnit;

@Command(name = "cmd", mixinStandardHelpOptions = true, version = "1.0",
        description = "Command with some options to demonstrate TAB-completion" +
                " (note that enum values also get completed)")
public class TestCommand implements Runnable {
    @Option(names = {"-v", "--verbose"})
    private boolean[] verbosity = {};

    @Option(names = {"-d", "--duration"})
    private int amount;

    @Option(names = {"-u", "--timeUnit"})
    private TimeUnit unit;

    @ParentCommand
    CliCommands parent;

    public void run() {
        if (verbosity.length > 0) {
            parent.out.printf("Hi there. You asked for %d %s.%n", amount, unit);
        } else {
            parent.out.println("hi!");
        }
    }
}