package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SinglePlayEngine extends Thread {

    private  File songFile;
    private AdvancedPlayer advancedPlayer;
    private boolean isPlaying;

    SinglePlayEngine(File songFile) throws FileNotFoundException, JavaLayerException {
        this.songFile = songFile;
        advancedPlayer = new AdvancedPlayer(new FileInputStream(songFile));
    }

    @Override
    public void run() {
        try {
            isPlaying = true;
            advancedPlayer.play();

        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    void stopMusic() {
        isPlaying = false;
        this.advancedPlayer.close();
    }
    AdvancedPlayer getAdvancedPlayer() {
        return advancedPlayer;
    }
}
