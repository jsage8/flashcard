package flashcard;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class AutoPaceAutoPauseListener implements PaceListener {
    private final ExperimentSettings experimentSettings;
    
    private final int wordCount;
    private final int flipCount;
    private final Word[] wordList;
    private int wordCounter;
    private int wordIndex;
    private int flipCounter;
    private final JLabel textOne;
    private final JLabel textTwo;
    private boolean onWord;
    private final Timer timer;
    private final JButton beginButton;
    private final JButton flipButton;
    private final JButton nextWordButton;
    
    //Store all user interaction
    private final Data data;
    private WordData wordData;
    
    private boolean isPaused;
    
    private final File file;
    
    public AutoPaceAutoPauseListener(ExperimentSettings experimentSettings, Timer timer, JLabel textOne, JLabel textTwo, JButton beginButton, JButton flipButton, JButton nextWordButton) {
        this.experimentSettings = experimentSettings;
        
        this.wordCount = experimentSettings.getWordCount();
        this.flipCount = experimentSettings.getFlipCount();
        this.wordList = experimentSettings.getWordList();
        this.wordCounter = 0;
        this.wordIndex = 0;
        this.flipCounter = 0;
        this.textOne = textOne;
        this.textTwo = textTwo;
        onWord = true;
        this.timer = timer;
        this.beginButton = beginButton;
        this.flipButton = flipButton;
        this.nextWordButton = nextWordButton;
        //Disable buttons and set their text to null they are only visual placeholders
        flipButton.setEnabled(false);
        nextWordButton.setEnabled(false);
        flipButton.setText(null);
        nextWordButton.setText(null);
        
        data = new Data(wordList);
        
        this.file = experimentSettings.getOutputFile();
        
        isPaused = false;
    }
    
    @Override
    public void display() {
        if(onWord) {
            displayWord();
            onWord = false;
            flipCounter++;
        }
        else {
            displayDefinition();
            onWord = true;
            flipCounter++;
        }
    }
    
    @Override
    public void displayWord() {
        Word currentWord = wordList[wordIndex];
        
        timeStamp(currentWord.getWord(), false);
        
        textOne.setForeground(new Color(125, 0, 5));
        textOne.setFont(new Font("Arial", Font.PLAIN, 58));
        textOne.setText(currentWord.getWord());
        
        textTwo.setForeground(new Color(125, 0, 5));
        textTwo.setFont(new Font("Arial", Font.PLAIN, 20));
        textTwo.setText(currentWord.getPronunciation());
    }

    @Override
    public void displayDefinition() {
        Word currentWord = wordList[wordIndex];
        
        timeStamp(currentWord.getDefinition(), true);
        
        textOne.setForeground(new Color(232, 179, 35));
        textOne.setFont(new Font("Arial", Font.PLAIN, 20));
        textOne.setText("Synonym:");
        
        textTwo.setForeground(new Color(232, 179, 35));
        textTwo.setFont(new Font("Arial", Font.PLAIN, 58));
        textTwo.setText(currentWord.getDefinition());
    }
    
    @Override
    public void nextWord() {
        flipCounter = 0;
        wordCounter++;
        wordIndex++;
    }
    
    @Override
    public void startShow() {
        timer.start();
        beginButton.setVisible(false);
        flipButton.setVisible(true);
        nextWordButton.setVisible(true);
        textOne.setVisible(true);
        textTwo.setVisible(true);
        display();
    }
    
    @Override
    public void endShow() {
        timeStamp();
        timer.stop();
        textOne.setForeground(new Color(0, 0, 0));
        textOne.setFont(new Font("Arial", Font.PLAIN, 58));
        textOne.setText("End");
        textTwo.setVisible(false);
        nextWordButton.setEnabled(false);
        nextWordButton.setText(null);
        
        PrintWriter outputStream = writeFile(file);
        outputStream.println(data.toString());
        outputStream.println(data.toSummary());
        outputStream.close();
        
        experimentSettings.setDone(true);
    }
    
    //Close existing WordData object if one exists and open a new one
    @Override
    public void timeStamp(String word, boolean isSynonym) {
        long currentTime = System.currentTimeMillis();
        if(wordData != null) {
            wordData.setEndTime(currentTime);
            wordData.setIsPaused(isPaused);
            data.storeWordData(wordData);
        }
        if(wordIndex < wordList.length) {
            wordData = new WordData(wordIndex, word, isSynonym, currentTime, flipCounter);
        }
    }
    
    //Close existing WordData object if one exists
    @Override
    public void timeStamp() {
        long currentTime = System.currentTimeMillis();
        if(wordData != null) {
            wordData.setEndTime(currentTime);
            wordData.setIsPaused(isPaused);
            data.storeWordData(wordData);
        }
    }
    
    private PrintWriter writeFile(File fileObject) {
        PrintWriter outputStream;
        try {
            outputStream = new PrintWriter(new FileOutputStream(fileObject, true));
            return outputStream;
        }
        catch(FileNotFoundException e) {
            System.out.println("Cannot find file " + fileObject.getName() + ".txt or it could not be opened.");
            System.exit(0);
            return null;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Timer":
                    if(flipCounter <= flipCount) {
                        display();
                    }
                    else {
                        nextWord();
                        if(wordCounter < wordCount) {
                            if(wordIndex < wordList.length) { display(); }
                            else { endShow(); }
                        }
                        else {
                            if(wordIndex <= wordList.length) { 
                                timer.stop();
                                isPaused = true;
                                nextWordButton.setEnabled(true);
                                nextWordButton.setText("Resume Slides");
                                wordCounter = 0;
                            }
                            else { endShow(); }
                        }
                    }
                    break;
            case "Begin":
                startShow();
                break;
            case "Flip Card":
                //Disabled
                break;
            case "Next Card":
                if(wordIndex < wordList.length) {
                    onWord = true;
                    display();
                    //Disable buttons and set their text to null they are only visual placeholders
                    nextWordButton.setEnabled(false);
                    nextWordButton.setText(null);
                    isPaused = false;
                    timer.start();
                }
                else {
                    endShow();
                }
                break;
        }
    }
}