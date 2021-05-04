package com.company;

import java.io.File;

public class ImageDisplayManager extends Thread {
    private ImagePanel imagePanel;
    private boolean isUnderPlaying;

    public ImageDisplayManager(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    @Override
    public void run() {
        isUnderPlaying = true;
        for(int imgIndex = 0; imgIndex < makeListOfImages().length; imgIndex++ ){
            imagePanel.setMusicStopped(false);
            if(!isUnderPlaying){
                imagePanel.setMusicStopped(true);
                return;
            }
            imagePanel.paint(imagePanel.getGraphics());
        }
    }
    private String[] makeListOfImages(){
        File folder = new File("images");
        return folder.list();
    }

    public void setStopImageAnimation(){
        isUnderPlaying = false;
    }
}
