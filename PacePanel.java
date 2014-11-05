package flashcard;

import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.*;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class PacePanel extends JPanel {
    private ArrayList<HashMap<String, JComponent>> paceOptions;
    
    public PacePanel(String[] paceArray) {
        setLayout(new CardLayout(0,0));
        JPanel[] cards = new JPanel[paceArray.length];
        JPanel[] timeOptionsPanel = new JPanel[paceArray.length];
        JPanel[] flipOptionsPanel = new JPanel[paceArray.length];
        JPanel[] wordOptionsPanel = new JPanel[paceArray.length];
        
        paceOptions = new ArrayList<HashMap<String, JComponent>>(paceArray.length);
        
        //Set default display time
        //Set default number of flips per word
        //Set default number of words to display before auto pause
        for(int i = 0; i < paceArray.length; i++) {
            HashMap<String, JComponent> hashMap = new HashMap<String, JComponent>();
            hashMap.put("time", new JTextField("5000", 5));
            hashMap.put("flipCount", new JTextField("5", 5));
            hashMap.put("wordCount", new JTextField("5", 5));
            paceOptions.add(i, hashMap);
        }
        
        for(int i = 0; i < paceArray.length; i++) {
            switch(paceArray[i]) {
                case "Automatic Pace":
                    cards[i] = new JPanel();
                    cards[i].setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));
                    cards[i].setBackground(Color.WHITE);
                    
                    timeOptionsPanel[i] = new JPanel();
                    timeOptionsPanel[i].setLayout(new FlowLayout(FlowLayout.LEADING, 10, 1));
                    timeOptionsPanel[i].setBackground(new Color(235, 235, 235));
                    timeOptionsPanel[i].add(new JLabel("Time: "));
                    timeOptionsPanel[i].add(paceOptions.get(i).get("time"));
                    timeOptionsPanel[i].add(new JLabel("ms"));
                    
                    flipOptionsPanel[i] = new JPanel();
                    flipOptionsPanel[i].setLayout(new FlowLayout(FlowLayout.LEADING, 10, 1));
                    flipOptionsPanel[i].setBackground(new Color(235, 235, 235));
                    flipOptionsPanel[i].add(new JLabel("Flips: "));
                    flipOptionsPanel[i].add(paceOptions.get(i).get("flipCount"));
                    
                    cards[i].add(timeOptionsPanel[i]);
                    cards[i].add(flipOptionsPanel[i]);
                    break;
                case "Automatic Pace/Spacebar to Pause":
                    cards[i] = new JPanel();
                    cards[i].setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));
                    cards[i].setBackground(Color.WHITE);
                    
                    timeOptionsPanel[i] = new JPanel();
                    timeOptionsPanel[i].setLayout(new FlowLayout(FlowLayout.LEADING, 10, 1));
                    timeOptionsPanel[i].setBackground(new Color(235, 235, 235));
                    timeOptionsPanel[i].add(new JLabel("Time: "));
                    timeOptionsPanel[i].add(paceOptions.get(i).get("time"));
                    timeOptionsPanel[i].add(new JLabel("ms"));
                    
                    flipOptionsPanel[i] = new JPanel();
                    flipOptionsPanel[i].setLayout(new FlowLayout(FlowLayout.LEADING, 10, 1));
                    flipOptionsPanel[i].setBackground(new Color(235, 235, 235));
                    flipOptionsPanel[i].add(new JLabel("Flips: "));
                    flipOptionsPanel[i].add(paceOptions.get(i).get("flipCount"));
                    
                    cards[i].add(timeOptionsPanel[i]);
                    cards[i].add(flipOptionsPanel[i]);
                    break;
                case "Automatic Pace/Automatic Pause":
                    cards[i] = new JPanel();
                    cards[i].setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));
                    cards[i].setBackground(Color.WHITE);
                    
                    timeOptionsPanel[i] = new JPanel();
                    timeOptionsPanel[i].setLayout(new FlowLayout(FlowLayout.LEADING, 10, 1));
                    timeOptionsPanel[i].setBackground(new Color(235, 235, 235));
                    timeOptionsPanel[i].add(new JLabel("Time: "));
                    timeOptionsPanel[i].add(paceOptions.get(i).get("time"));
                    timeOptionsPanel[i].add(new JLabel("ms"));
                    
                    flipOptionsPanel[i] = new JPanel();
                    flipOptionsPanel[i].setLayout(new FlowLayout(FlowLayout.LEADING, 10, 1));
                    flipOptionsPanel[i].setBackground(new Color(235, 235, 235));
                    flipOptionsPanel[i].add(new JLabel("Flips: "));
                    flipOptionsPanel[i].add(paceOptions.get(i).get("flipCount"));

                    wordOptionsPanel[i] = new JPanel();
                    wordOptionsPanel[i].setLayout(new FlowLayout(FlowLayout.LEADING, 10, 1));
                    wordOptionsPanel[i].setBackground(new Color(235, 235, 235));
                    wordOptionsPanel[i].add(new JLabel("Pause After: "));
                    wordOptionsPanel[i].add(paceOptions.get(i).get("wordCount"));
                    wordOptionsPanel[i].add(new JLabel("words"));
                    
                    cards[i].add(timeOptionsPanel[i]);
                    cards[i].add(flipOptionsPanel[i]);
                    cards[i].add(wordOptionsPanel[i]);
                    break;
                case "Self Pace":
                    cards[i] = new JPanel();
                    cards[i].setBackground(Color.LIGHT_GRAY);
            }
        }
        
        for(int i = 0; i < paceArray.length; i++) {
            add(cards[i], paceArray[i]);
        }
    }
    
    public HashMap<String, String> getPaceOptions(int index) {
        HashMap<String, String> stringOptions = new HashMap<String, String>();
        stringOptions.put("time", getFieldString(index, "time"));
        stringOptions.put("flipCount", getFieldString(index, "flipCount"));
        stringOptions.put("wordCount", getFieldString(index, "wordCount"));
        return stringOptions;
    }
    
    public String getFieldString(int index, String option) {
        if(paceOptions.get(index).get("time") instanceof JTextField) {
            JTextField field = (JTextField)paceOptions.get(index).get(option);
            return field.getText();
        }
        else if(paceOptions.get(index).get(option) instanceof JComboBox) {
            JComboBox field = (JComboBox)paceOptions.get(index).get(option);
            return (String)field.getSelectedItem();
        }
        return null;
    }
}
