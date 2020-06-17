package com.company;

import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class DisplayManager {

    private JFrame frame;
    private SinglePlayEngine engine;
    private FileCopyManager fileCopyManager;
    private MultiPlayEngine multiPlayEngine;
    private Label labelActual;
    private JScrollPane songListPanel;
    private JList selectionList;
    private JButton buttonPlay;
    private JButton buttonStop;
    private JButton buttonAddImagine;
    private JButton buttonSelectMusic;
    private JButton buttonAddNewMusic;
    private JPanel panelOfButtons;
    private JPanel actualSongPanel;
    private ImageDisplayManager imageManager;

    DisplayManager() {
        frame = new JFrame();
        multiPlayEngine = new MultiPlayEngine();
        fileCopyManager = new FileCopyManager();
    }

    void runPlayer() {
        ImagePanel insidePanel = new ImagePanel();

       // imageManager = new ImageDisplayManager(insidePanel);

        buildButtonsWithFunctions();
        makeSelectionListAndActualSongPanel();
        buildPanelOfButtons();

        createAndMakeVisibleFrame(insidePanel, panelOfButtons, actualSongPanel);
    }

    private void buildButtonsWithFunctions() {
        buttonPlay = new JButton("    Play  |>      ");
        buttonPlay.addActionListener(new PlayMusicListener());

        buttonStop = new JButton("    Stop  []      ");
        buttonStop.addActionListener(new StopMusic());

        buttonAddImagine = new JButton(" Add  Image ");
        buttonAddImagine.addActionListener(new AddNewImagineListener());


        buttonSelectMusic = new JButton("Select music");
        buttonSelectMusic.addActionListener(new PlaySelectedFileListener());

        buttonAddNewMusic = new JButton("Add new music");
        buttonAddNewMusic.addActionListener(new AddNewToListListener());
    }

    private void buildPanelOfButtons() {
        panelOfButtons = new JPanel();
        panelOfButtons.setBackground(Color.LIGHT_GRAY);
        panelOfButtons.setLayout(new BoxLayout(panelOfButtons, BoxLayout.Y_AXIS));

        panelOfButtons.add(buttonPlay);
        panelOfButtons.add(buttonStop);
        panelOfButtons.add(buttonAddImagine);
        panelOfButtons.add(buttonSelectMusic);
        panelOfButtons.add(songListPanel);
    }

    private void makeSelectionListAndActualSongPanel() {
        String[] playlist = multiPlayEngine.makeListOfNamesOfFile();
        selectionList = new JList(playlist);
        songListPanel = new JScrollPane(selectionList);
        songListPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        songListPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        songListPanel.setPreferredSize(new Dimension(70, 0));
        selectionList.setForeground(Color.BLUE);
        selectionList.setBackground(Color.cyan);

        selectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionList.addListSelectionListener(new SelectionFromListListener());

        actualSongPanel = new JPanel();
        actualSongPanel.setLayout(new BoxLayout(actualSongPanel, BoxLayout.X_AXIS));
        actualSongPanel.add(buttonAddNewMusic);
        labelActual = new Label("none");

        actualSongPanel.add(new Label("Actual music:"));
        actualSongPanel.add(labelActual);
    }

    private void createAndMakeVisibleFrame(ImagePanel insidePanel, JPanel panelButtons, JPanel actualSongPanel) {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.EAST, panelButtons);
        frame.getContentPane().add(BorderLayout.SOUTH, actualSongPanel);
        frame.getContentPane().add(BorderLayout.CENTER, insidePanel);
        frame.setSize(550, 430);
        frame.setVisible(true);
    }

    /**
     * Button and Action Listeners:
     */

    public class PlayMusicListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            multiPlayEngine = new MultiPlayEngine();
            multiPlayEngine.start();
            //imageManager.start();
        }
    }

    public class SelectionFromListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            try {
                if (e.getValueIsAdjusting()) {
                    String fileName = (String) selectionList.getSelectedValue();
                    File file = new File("music/" + fileName);
                    addFileToPlay(fileName, file);
                }
            } catch (FileNotFoundException | JavaLayerException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addFileToPlay(String fileName, File file) throws FileNotFoundException, JavaLayerException {
        if (engine != null) engine.stopMusic();
        engine = new SinglePlayEngine(file);
        labelActual.setText(fileName);
        labelActual.setForeground(Color.BLUE);
        engine.start();
    }

    public class PlaySelectedFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                File selectedFile = getFileFromBrowsing("Browse and select music to play");
                if (selectedFile != null) {
                    addFileToPlay(selectedFile.getName(), selectedFile);
                }
            } catch (FileNotFoundException | JavaLayerException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File getFileFromBrowsing(String s) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(s);
        chooser.showDialog(frame, null);
        return chooser.getSelectedFile();
    }

    public class AddNewToListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File selectedFile = getFileFromBrowsing("Browse to copy a file");
            if (!fileCopyManager.checkFileIsMp3(selectedFile)) {
                JOptionPane.showMessageDialog(frame, "Invalid type of file. Choose mp3 file!", "Warnings", JOptionPane.WARNING_MESSAGE);
            } else {
                copyMusicFileToPlyalist(selectedFile, false);
            }
        }
    }

    private class AddNewImagineListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File selectedFile = getFileFromBrowsing("Browse to copy and add an Image");
            if (!fileCopyManager.checkFileIsImg(selectedFile)) {
                JOptionPane.showMessageDialog(frame, "Invalid type. Choose img or png file!", "Warnings", JOptionPane.WARNING_MESSAGE);
            } else {
                copyMusicFileToPlyalist(selectedFile, true);
            }
        }
    }

    private void copyMusicFileToPlyalist(File selectedFile, boolean isFileAnImage) {
        String copyMessage = selectedFile.getName() + " will be copied to playlist";
        JOptionPane.showMessageDialog(frame, copyMessage, "Copy File", JOptionPane.INFORMATION_MESSAGE);

        fileCopyManager = new FileCopyManager(selectedFile, frame, isFileAnImage);
        fileCopyManager.start();
        labelActual.setText("Copy file...");
        while (fileCopyManager.isAlive()) {
            ;
        }
        if (fileCopyManager.isCopyCompleted()) {
            String[] fileList = multiPlayEngine.makeListOfNamesOfFile();
            labelActual.setText("Copy is done..");
            selectionList.setListData(fileList);
        }
    }

    public class StopMusic implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            labelActual.setText("None");
            labelActual.setForeground(Color.BLACK);
            if (engine != null) {
                engine.stopMusic();
            }
            multiPlayEngine.stopRunning();
           // imageManager.setStopImageAnimation();
        }
    }
}
