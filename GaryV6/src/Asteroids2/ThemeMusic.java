package Asteroids2;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class ThemeMusic {
    private final Media mainTheme;
    private final MediaPlayer mainPlayer;
    private final Media menuTheme;
    private final MediaPlayer menuPlayer;

    public ThemeMusic() {
        mainTheme = new Media(new File("src/SFX/theme.mp3").toURI().toString());
        mainPlayer = new MediaPlayer(mainTheme);
        menuTheme = new Media(new File("src/SFX/mainmenu.mp3").toURI().toString());
        menuPlayer = new MediaPlayer(menuTheme);
    }

    public void playMainTheme() {
        Thread thread = new Thread(() -> {
            mainPlayer.play();
        });
        thread.start();
    }

    public void stopMainTheme() {
        mainPlayer.stop();
    }

    public void playMenuTheme() {
        Thread thread = new Thread(() -> {
            menuPlayer.play();
        });
        thread.start();
    }

    public void stopMenuTheme() {
        menuPlayer.stop();
    }
}
