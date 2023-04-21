package Asteroids2;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sfx {

    private final Media lazersound = new Media(new File("src/SFX/Lazer1.mp3").toURI().toString());
    private final Media hypersound = new Media(new File("src/SFX/hyperjump.mp3").toURI().toString());
    private final Media gameover = new Media(new File("src/SFX/explosion.mp3").toURI().toString());

    public void lazersound() {
        new Thread(() -> {
            MediaPlayer lazer = new MediaPlayer(lazersound);
            lazer.play();
            lazer.setOnEndOfMedia(() -> lazer.dispose());
        }).start();
    }

    public void hypersound() {
        new Thread(() -> {
            MediaPlayer hyper = new MediaPlayer(hypersound);
            hyper.play();
            hyper.setOnEndOfMedia(() -> hyper.dispose());
        }).start();
    }

    public void gameover() {
        new Thread(() -> {
            MediaPlayer gameOver = new MediaPlayer(gameover);
            gameOver.play();
            gameOver.setOnEndOfMedia(() -> gameOver.dispose());
        }).start();
    }
}
