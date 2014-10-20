package flashcard;

import java.awt.event.*;

public interface PaceListener extends ActionListener {
    public void display();
    public void displayWord();
    public void displayDefinition();
    public void nextWord();
    public void startShow();
    public void endShow();
    public void timeStamp();
    public void timeStamp(String word, boolean isSynonym);
}
