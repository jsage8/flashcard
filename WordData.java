package flashcard;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class WordData {
    private final int wordIndex;
    private final boolean isSynonym;
    private final String word;
    private boolean isPaused;
    private long startTime;
    private long endTime;
    private final int flipCount;
    
    public WordData(int wordIndex, String word, boolean isSynonym, long startTime, int flipCount) {
        this.wordIndex = wordIndex;
        this.word = word;
        this.isSynonym = isSynonym;
        this.startTime = startTime;
        this.flipCount = flipCount;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
    
    public int getWordIndex() {
        return wordIndex;
    }
    
    public String getWordName() {
        return word;
    }
    
    public boolean isSynonym() {
        return isSynonym;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public int getTime(){
        return (int)(endTime - startTime);
    }
    
    public int getFlipCount() {
        return flipCount;
    }

    @Override
    public String toString() {
        String string;
        if(isSynonym) { string = "synonym "; }
        else { string = "word "; }
        string += (wordIndex + 1) + "\t" + word + "\t" + isPaused + "\t" + getTime() + "\t" + flipCount;
        return string;
    }
}
