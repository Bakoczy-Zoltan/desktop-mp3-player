package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImagePanel extends JPanel {

    private String[] arrayImg;
    private int numberOfImages;
    private String actualImageName;
    private boolean isMusicStopped;

    public ImagePanel() {
        this.arrayImg = makeListOfImages();
        this.numberOfImages = arrayImg.length;
        this.actualImageName = "images/mp3starPictures.png";
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        String relative = this.actualImageName;
        Image img = new ImageIcon(relative).getImage();
        g.drawImage(img, 45, 30, this);
    }

    private boolean isStandingOnLastImageInArray(int position) {
        return (position == this.numberOfImages - 1);
    }

    private String[] makeListOfImages() {
        File folder = new File("images");
        return folder.list();
    }

    public void setMusicStopped(boolean musicStopped) {
        isMusicStopped = musicStopped;
    }


}
