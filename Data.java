package flashcard;

import java.util.ArrayList;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class Data {
    private ArrayList<WordData> data;
    private Word[] wordList;
    
    public Data(Word[] wordList) {
        data = new ArrayList<WordData>();
        this.wordList = wordList;
    }
    
    public void storeWordData(WordData wordData) {
        data.add(wordData);
    }
    
    public int getFlipCount(int index) {
        int flipCount = 0;
        for(WordData wordData : data) {
            if(wordData.getWordIndex() == index && wordData.getFlipCount() > flipCount ) {
                flipCount = wordData.getFlipCount();
            }
        }
        return flipCount;
    }
    
    public int getWordTime(int index) {
        int totalTime = 0;
        for(WordData wordData : data) {
            if(wordData.getWordIndex() == index && !wordData.isSynonym()) {
                totalTime += wordData.getTime();
            }
        }
        return totalTime;
    }
    
    public int getSynonymTime(int index) {
        int totalTime = 0;
        for(WordData wordData : data) {
            if(wordData.getWordIndex() == index && wordData.isSynonym()) {
                totalTime += wordData.getTime();
            }
        }
        return totalTime;
    }
    
    public int getSynonymWordTime(int index) {
        int totalTime = 0;
        for(WordData wordData : data) {
            if(wordData.getWordIndex() == index) {
                totalTime += wordData.getTime();
            }
        }
        return totalTime;
    }
    
    @Override
    public String toString() {
        String rawData = "\tWord\tisPaused\ttime\tflipCount\n";
        for(WordData wordData : data) {
            rawData += wordData.toString() + "\n";
        }
        return rawData;
    }
    
    public String toSummary() {
        String summary = "\t# Flips\tisPaused\tTime on Word\tTime on Synonym\tTotal Time\n";
        int index = 0;
        int wordTime;
        int synonymTime;
        int flipCount;
        boolean isPaused;
        
        for(int i = 0; i < data.size(); ) {
            wordTime = 0;
            synonymTime = 0;
            flipCount = 0;
            isPaused = false;
            while(i < data.size() && data.get(i).getWordIndex() == index) {
                WordData wordData = data.get(i);
                if(!wordData.isSynonym()) {
                    wordTime += wordData.getTime();
                }
                if(wordData.isSynonym()) {
                    synonymTime += data.get(i).getTime();
                }
                if(wordData.getFlipCount() > flipCount) {
                    flipCount = wordData.getFlipCount();
                }
                if(wordData.isPaused()) {
                    isPaused = true;
                }
                i++;
            }
            summary += wordList[index].getWord() + "\t" + flipCount + "\t" + isPaused + "\t" + wordTime + "\t" + synonymTime + "\t" + (wordTime + synonymTime) + "\n";
            index++;
        }
        return summary;
    }
}
