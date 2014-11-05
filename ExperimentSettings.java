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
    private boolean isDone;
    private String beginMessage;
    private String endMessage;
    
    public ExperimentSettings(File outputFile, Word[] experimentWordList, String pace, String beginMessage, String endMessage) {
        this.outputFile = outputFile;
        this.wordList = experimentWordList;
        this.pace = pace;
        this.isDone = false;
        this.beginMessage = beginMessage;
        this.endMessage = endMessage;
    }
    
    public ExperimentSettings(File outputFile, Word[] experimentWordList, String pace, int time, int flipCount, String beginMessage, String endMessage) {
        this.outputFile = outputFile;
        this.wordList = experimentWordList;
        this.pace = pace;
        this.time = time;
        this.flipCount = flipCount;
        this.isDone = false;
        this.beginMessage = beginMessage;
        this.endMessage = endMessage;
    }
    
    public ExperimentSettings(File outputFile, Word[] experimentWordList, String pace, int time, int flipCount, int wordCount, String beginMessage, String endMessage) {
        this.outputFile = outputFile;
        this.wordList = experimentWordList;
        this.pace = pace;
        this.time = time;
        this.flipCount = flipCount;
        this.wordCount = wordCount;
        this.isDone = false;
        this.beginMessage = beginMessage;
        this.endMessage = endMessage;
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
    
    public boolean isDone() {
        return isDone;
    }
    
    public void setDone(boolean status) {
        isDone = status;
    }
    
    public String getBeginMessage() {
        return beginMessage;
    }
    
    public String getEndMessage() {
        return endMessage;
    }
    
    @Override
    public String toString() {
        String experimentString = outputFile.getName() + "\n";
        experimentString += "Experiment Word List\n";
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
