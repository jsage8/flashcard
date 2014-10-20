package flashcard;

import java.io.*;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class ExperimentSettings {
    private final File outputFile;
    private final Word[] wordList;
    private final String pace;
    private int time;
    private int flipCount;
    private int wordCount;
    
    public ExperimentSettings(File outputFile, Word[] wordList, String pace) {
        this.outputFile = outputFile;
        this.wordList = wordList;
        this.pace = pace;
    }
    
    public ExperimentSettings(File outputFile, Word[] wordList, String pace, int time, int flipCount) {
        this.outputFile = outputFile;
        this.wordList = wordList;
        this.pace = pace;
        this.time = time;
        this.flipCount = flipCount;
    }
    
    public ExperimentSettings(File outputFile, Word[] wordList, String pace, int time, int flipCount, int wordCount) {
        this.outputFile = outputFile;
        this.wordList = wordList;
        this.pace = pace;
        this.time = time;
        this.flipCount = flipCount;
        this.wordCount = wordCount;
    }
    
    public File getOutputFile() {
        return outputFile;
    }
    
    public Word[] getWordList() {
        return wordList;
    }
    
    public String getPace() {
        return pace;
    }
    
    public int getTime() {
        return time;
    }
    
    public int getFlipCount() {
        return flipCount;
    }
    
    public int getWordCount() {
        return wordCount;
    }
    
    @Override
    public String toString() {
        String experimentString = outputFile.getName() + "\n";
        for(Word word : wordList) {
            experimentString += word.toString();
        }
        experimentString += pace + "\n";
        switch(pace) {
            case "Automatic Pace":
                experimentString += time + "\n";
                experimentString += flipCount + "\n";
                break;
            case "Automatic Pace/Click to Pause":
                experimentString += time + "\n";
                experimentString += flipCount + "\n";
                break;
            case "Automatic Pace/Automatic Pause":
                experimentString += time + "\n";
                experimentString += flipCount + "\n";
                experimentString += wordCount + "\n";
                break;
            case "Self Pace":
                // No Options Applicable
                break;
        }
        return experimentString;
    }
}
