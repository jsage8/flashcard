package flashcard;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class Word {
    private final String word;
    private final String pronunciation;
    private final String definition;
    
    public Word(String word, String pronunciation, String definition) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.definition = definition;
    }
    
    public String getWord() {
        return word;
    }
    
    public String getPronunciation() {
        return pronunciation;
    }
    
    public String getDefinition() {
        return definition;
    }
    
    @Override
    public String toString() {
        return word + "\t" + pronunciation + "\t" + definition + "\n";
    }
}


