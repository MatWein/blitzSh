package blitzsh.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Resources {
    private static final Logger LOGGER = LoggerFactory.getLogger(Resources.class);

    public static final ImageIcon BLITZSH_16 = new ImageIcon(Resources.class.getResource("/icons/blitzsh16.png"));
    public static final ImageIcon BLITZSH_24 = new ImageIcon(Resources.class.getResource("/icons/blitzsh24.png"));
    public static final ImageIcon BLITZSH_32 = new ImageIcon(Resources.class.getResource("/icons/blitzsh32.png"));
    public static final ImageIcon BLITZSH_64 = new ImageIcon(Resources.class.getResource("/icons/blitzsh64.png"));
    public static Image BLITZSH_16_IMAGE;
    public static Image BLITZSH_512_IMAGE;

    public static final List<Image> ICONS = Arrays.asList(
            BLITZSH_16.getImage(),
            BLITZSH_24.getImage(),
            BLITZSH_32.getImage(),
            BLITZSH_64.getImage()
    );

    public static final ImageIcon TERMINAL_PLUS = new ImageIcon(Resources.class.getResource("/icons/common/plus-terminal.png"));
    public static final ImageIcon SSH_PLUS = new ImageIcon(Resources.class.getResource("/icons/common/plus-ssh.png"));
    public static final ImageIcon FOLDER_PLUS = new ImageIcon(Resources.class.getResource("/icons/common/folder-plus.png"));
    public static final ImageIcon MINUS = new ImageIcon(Resources.class.getResource("/icons/common/minus.png"));
    public static final ImageIcon COPY = new ImageIcon(Resources.class.getResource("/icons/common/copy.png"));
    public static final ImageIcon SSH = new ImageIcon(Resources.class.getResource("/icons/common/ssh.png"));
    public static Image SSH_IMAGE;

    public static final ImageIcon FOLDER = new ImageIcon(Resources.class.getResource("/icons/common/folder.png"));
    public static Image FOLDER_IMAGE;

    static {
        try {
            FOLDER_IMAGE = ImageIO.read(Resources.class.getResourceAsStream("/icons/common/folder.png"));
            BLITZSH_16_IMAGE = ImageIO.read(Resources.class.getResourceAsStream("/icons/blitzsh16.png"));
            BLITZSH_512_IMAGE = ImageIO.read(Resources.class.getResourceAsStream("/icons/blitzsh512.png"));
            SSH_IMAGE = ImageIO.read(Resources.class.getResourceAsStream("/icons/common/ssh.png"));
        } catch (IOException e) {
            LOGGER.error("Error on loading images.", e);
        }
    }
}
