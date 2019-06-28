package blitzsh.shell;

import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.PrintWriter;

@Command(name = "", description = "Example interactive shell with completion",
        footer = {"", "Press Ctl-D to exit."},
        subcommands = {TestCommand.class})
public class CliCommands implements Runnable {
    LineReaderImpl reader;
    PrintWriter out;

    CliCommands() {}

    public void setReader(LineReader reader){
        this.reader = (LineReaderImpl)reader;
        out = reader.getTerminal().writer();
    }

    public void run() {
        out.println(new CommandLine(this).getUsageMessage());
    }
}
