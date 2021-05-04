package com.company;

import javazoom.jl.decoder.JavaLayerException;

import java.io.File;
import java.io.FileNotFoundException;

public class MultiPlayEngine extends Thread {
    private SinglePlayEngine engine;
    private boolean isStopped;
    private File file;

    @Override
    public void run() {
        String[] fileNames = this.makeListOfNamesOfFile();
        isStopped = false;
        for (int fileIndex = 0; fileIndex < fileNames.length; ) {
            if (isStopped) {
                break;
            }
            try {
                file = new File("music/" + fileNames[fileIndex]);
                engine = new SinglePlayEngine(file);
                engine.start();
                while (engine.isAlive()) {
                    ;
                }
                engine.getAdvancedPlayer().close();
                fileIndex++;
            } catch (FileNotFoundException | JavaLayerException ex) {
                ex.printStackTrace();
            }
        }
    }

    String[] makeListOfNamesOfFile() {
        File folder = new File("music");
        return folder.list();
    }

    void stopRunning() {
        if (engine != null) {
            engine.stopMusic();
            engine.getAdvancedPlayer().close();
            isStopped = true;
        }
    }
}
