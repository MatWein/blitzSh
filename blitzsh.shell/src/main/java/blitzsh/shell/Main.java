package blitzsh.shell;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliJLineCompleter;

import java.io.IOException;

public class Main {
    public static final String PROMPT = ": ";

    public static void main(String[] args) throws IOException {
        CliCommands commands = new CliCommands();
        CommandLine cmd = new CommandLine(commands);

        Terminal terminal = TerminalBuilder.builder()
                .build();

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new PicocliJLineCompleter(cmd.getCommandSpec()))
                .parser(new DefaultParser())
                .build();
        commands.setReader(reader);

//        int columns = ...;
//        int rows = ...;
//        Attributes attributes = new Attributes();
//        // set attributes...
//        Terminal terminal = TerminalBuilder.builder()
//                .system(false)
//                .streams(input, output)
//                .size(new Size(columns, rows))
//                .attributes(attributes)
//                .build();

        while (true) {
            try {
                String line = reader.readLine(PROMPT);
                ParsedLine pl = reader.getParser().parse(line, 0);
                String[] arguments = pl.words().toArray(new String[] {});
                cmd.execute(arguments);
            } catch (UserInterruptException ignored) {
            } catch (EndOfFileException e) {
                return;
            }
        }
    }
}
