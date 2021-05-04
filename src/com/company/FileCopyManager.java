package com.company;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileCopyManager extends Thread {
    private JFrame frame;
    private File file;
    private boolean isCopyCompleted;
    private boolean isFileAnImageToCopy;

    public FileCopyManager() {
    }

    public FileCopyManager(File file, JFrame frame, boolean isFileAnImageToCopy) {
        this.file = file;
        this.frame = frame;
        this.isFileAnImageToCopy = isFileAnImageToCopy;
    }

    @Override
    public void run() {
        isCopyCompleted = false;
        String targetPath = "";
        if(checkFileIsImg(file)){
            targetPath = "images" + "\\" + file.getName();
        }else targetPath = "music" + "\\" + file.getName();

        System.out.println("origin path: " + file.getPath());
        System.out.println("to : " + targetPath);

        makeCopyOfFile(file, targetPath);

    }

    private void makeCopyOfFile(File file, String targetPath) {
        try (FileInputStream input = new FileInputStream(file.getPath());
             FileOutputStream output = new FileOutputStream(targetPath)) {
            int byteSource;
            while ((byteSource = input.read()) != -1) {
                output.write(byteSource);
            }
            isCopyCompleted = true;
            System.out.println("copy of " + file.getName() + ": done");
        } catch (Exception e) {
            System.out.println("Invalid path or file or target");
        }
    }

    public boolean checkFileIsMp3(File file) {
        if(file == null) return false;
        String fileName = file.getName();
        int indexOfPoint = fileName.indexOf('.');
        return fileName.substring(indexOfPoint).equals(".mp3");
    }
    public boolean checkFileIsImg(File file) {
        if(file == null) return false;
        String fileName = file.getName();
        int indexOfPoint = fileName.indexOf('.');
        return (fileName.substring(indexOfPoint).equals(".png")
                || fileName.substring(indexOfPoint).equals(".jpg")
                || fileName.substring(indexOfPoint).equals(".jpeg"));
    }

    public boolean isCopyCompleted() {
        return isCopyCompleted;
    }
}
