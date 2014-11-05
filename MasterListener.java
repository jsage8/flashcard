package flashcard;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.event.*;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class MasterListener {
    private final ExperimentSettings[] experimentSettings;
    private int experimentSettingsIndex;
    
    private final JPanel mainPanel;
    private final  JLabel textOne;
    private final JLabel textTwo;
    private final JButton beginButton; 
    private final JButton flipButton;
    private final JButton nextWordButton;
    
    public MasterListener(ExperimentSettings[] experimentSettings, JPanel mainPanel, JLabel textOne, JLabel textTwo, JButton beginButton, JButton flipButton, JButton nextWordButton) {
        this.experimentSettings = experimentSettings;
        experimentSettingsIndex = 0;
        
        this.mainPanel = mainPanel;
        this.textOne = textOne;
        this.textTwo = textTwo;
        this.beginButton = beginButton;
        this.flipButton = flipButton;
        this.nextWordButton = nextWordButton;
        
        // Enter Key Binding
        String enter = "ENTER";
        Action enterAction = new EnterAction();
        // Gets the mainPanel InputMap and pairs the key to the action
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(enter), "doEnterAction");
        // This line pairs the AbstractAction escapeAction to the action "doEscapeAction"
        mainPanel.getActionMap().put("doEnterAction", enterAction);
    }
    
    public void newListener() {
        if(experimentSettingsIndex < experimentSettings.length && experimentSettings[experimentSettingsIndex].isDone()) {
            // Remove current action listeners from all the begin, flip and next buttons
            for(ActionListener listener : beginButton.getActionListeners()) {
                beginButton.removeActionListener(listener);
            }
            for(ActionListener listener : flipButton.getActionListeners()) {
                flipButton.removeActionListener(listener);
            }
            for(ActionListener listener : nextWordButton.getActionListeners()) {
                nextWordButton.removeActionListener(listener);
            }
            experimentSettingsIndex++;
        }
        
        if(experimentSettingsIndex < experimentSettings.length) {
            flipButton.setVisible(false);
            nextWordButton.setVisible(false);
            textOne.setText(experimentSettings[experimentSettingsIndex].getBeginMessage());
            beginButton.setVisible(true);

            /***********************************************************************
             * PaceListener uses the Strategy Pattern. There are multiple 
             * implementations of paceListener, one for each different type.
             * The following switch statement sets the type of PaceListener used.
             **********************************************************************/
            PaceListener paceListener = null;

            /***********************************************************************
             * Swing Timer is used to fire the PaceListeners that all extend 
             * ActionListener. The ExperimentSettings time is used to set the 
             * interval. The Timer is originally instantiated with null for the
             * ActionListener so that I can first instantiate the ActionListener
             * and pass it the Timer instance. Then I add that ActionListener to the
             * Timer. This prevents a chicken and egg problem.
             **********************************************************************/
            Timer timer;

            switch(experimentSettings[experimentSettingsIndex].getPace()) {
                case "Automatic Pace":
                    timer = new Timer(experimentSettings[experimentSettingsIndex].getTime(), null);
                    timer.setActionCommand("Timer");
                    paceListener = new AutoPaceListener(experimentSettings[experimentSettingsIndex], timer, textOne, textTwo, beginButton, flipButton, nextWordButton);
                    timer.addActionListener(paceListener);
                    break;
                case "Automatic Pace/Spacebar to Pause":
                    timer = new Timer(experimentSettings[experimentSettingsIndex].getTime(), null);
                    timer.setActionCommand("Timer");
                    paceListener = new AutoPaceSpacebarPauseListener(experimentSettings[experimentSettingsIndex], timer, mainPanel, textOne, textTwo, beginButton, flipButton, nextWordButton);
                    timer.addActionListener(paceListener);
                    break;
                case "Automatic Pace/Automatic Pause":
                    timer = new Timer(experimentSettings[experimentSettingsIndex].getTime(), null);
                    timer.setActionCommand("Timer");
                    paceListener = new AutoPaceAutoPauseListener(experimentSettings[experimentSettingsIndex], timer, textOne, textTwo, beginButton, flipButton, nextWordButton);
                    timer.addActionListener(paceListener);
                    break;
                case "Self Pace":
                    paceListener = new SelfPaceListener(experimentSettings[experimentSettingsIndex], textOne, textTwo, beginButton, flipButton, nextWordButton);
                    break;
            }

            beginButton.addActionListener(paceListener);
            flipButton.addActionListener(paceListener);
            nextWordButton.addActionListener(paceListener);
        }
    }
    
    /***************************************************************************
     * When one experimentSettings is done, the experimenter can move to the 
     * next by pressing enter
     **************************************************************************/
    private class EnterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(experimentSettingsIndex < experimentSettings.length && experimentSettings[experimentSettingsIndex].isDone()) {
                newListener();
            }
        }
    }
}
