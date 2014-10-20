package flashcard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class CardShow extends JFrame {       
    public CardShow(ExperimentSettings experimentSettings) {
        // Add a set of image icons to the current JFrame
        URL flashcard16 = getClass().getClassLoader().getResource("resources/img/flashcard16.png");
        URL flashcard32 = getClass().getClassLoader().getResource("resources/img/flashcard32.png");
        URL flashcard64 = getClass().getClassLoader().getResource("resources/img/flashcard64.png");
        URL flashcard128 = getClass().getClassLoader().getResource("resources/img/flashcard128.png");
                
        java.util.List<Image> icons = new ArrayList<Image>();
        try {
            icons.add(ImageIO.read(flashcard16));
            icons.add(ImageIO.read(flashcard32));
            icons.add(ImageIO.read(flashcard64));
            icons.add(ImageIO.read(flashcard128));
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        this.setIconImages(icons);

        // Set the background for the content pane of the JFrame to dark gray
        getContentPane().setBackground(Color.DARK_GRAY);
        
        // Use this constraint object to position elements
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        
        // Create panel so that I can use key binding which requires JComponent
        // mainPanel is where flashcards are displayed
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridBagLayout());
        Dimension cardDimension = new Dimension(700, 500);
        mainPanel.setPreferredSize(cardDimension);
        
        // Holds buttons during show
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setLayout(new GridBagLayout());

        // Use GridBagLayout to center the mainPanel and buttonPanel in 
        // fullscreen mode
        setLayout(new GridBagLayout());
        gridBagConstraints.gridy = 0;
        add(mainPanel, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        add(buttonPanel, gridBagConstraints);
        
        // Button dimensions
        Dimension defaultButton = new Dimension(240, 50);
        
        // Subject presses this button to start the show
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JButton beginButton = new JButton("Begin");
        beginButton.setActionCommand("Begin");
        beginButton.setPreferredSize(defaultButton);
        beginButton.setFont(new Font("Arial", Font.PLAIN, 30));
        buttonPanel.add(beginButton, gridBagConstraints);
        
        // Button to flip the card (only allowed for certain PaceListners)
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JButton flipButton = new JButton("Flip Card");
        flipButton.setActionCommand("Flip Card");
        flipButton.setPreferredSize(defaultButton);
        flipButton.setFont(new Font("Arial", Font.PLAIN, 30));
        buttonPanel.add(flipButton, gridBagConstraints);
        flipButton.setVisible(false);
        
        // Button to move to next word (only allowed for certain PaceListeners).
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        JButton nextWordButton = new JButton("Next Card");
        nextWordButton.setActionCommand("Next Card");
        nextWordButton.setPreferredSize(defaultButton);
        nextWordButton.setFont(new Font("Arial", Font.PLAIN, 30));
        buttonPanel.add(nextWordButton, gridBagConstraints);
        nextWordButton.setVisible(false);
        
        // Text Panel will hold the word and its pronunciation or the definition
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new GridBagLayout());
        mainPanel.add(textPanel, gridBagConstraints);
        
        // Holds the word or the definition
        JLabel textOne = new JLabel("Begin?");
        textOne.setFont(new Font("Arial", Font.PLAIN, 58));
        gridBagConstraints.gridy = 0;
        textPanel.add(textOne, gridBagConstraints);
        
        // Holds the pronunciation, setVisible is false when the definition is 
        // being shown
        JLabel textTwo = new JLabel("");
        textTwo.setFont(new Font("Arial", Font.PLAIN, 58));
        gridBagConstraints.gridy = 1;
        textPanel.add(textTwo, gridBagConstraints);
              
        // Escape Key Binding
        String escape = "ESCAPE";
        Action escapeAction = new EscapeAction();
        // Gets the mainPanel InputMap and pairs the key to the action
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(escape), "doEscapeAction");
        // This line pairs the AbstractAction escapeAction to the action "doEscapeAction"
        mainPanel.getActionMap().put("doEscapeAction", escapeAction);
        
        // Disable spacebar clicking a focused button
        InputMap im = (InputMap)UIManager.get("Button.focusInputMap");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
        
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
        
        switch(experimentSettings.getPace()) {
            case "Automatic Pace":
                timer = new Timer(experimentSettings.getTime(), null);
                paceListener = new AutoPaceListener(experimentSettings, timer, textOne, textTwo, beginButton, flipButton, nextWordButton);
                timer.addActionListener(paceListener);
                timer.setActionCommand("Timer");
                break;
            case "Automatic Pace/Spacebar to Pause":
                timer = new Timer(experimentSettings.getTime(), null);
                paceListener = new AutoPaceSpacebarPauseListener(experimentSettings, timer, mainPanel, textOne, textTwo, beginButton, flipButton, nextWordButton);
                timer.addActionListener(paceListener);
                timer.setActionCommand("Timer");
                break;
            case "Automatic Pace/Automatic Pause":
                timer = new Timer(experimentSettings.getTime(), null);
                paceListener = new AutoPaceAutoPauseListener(experimentSettings, timer, textOne, textTwo, beginButton, flipButton, nextWordButton);
                timer.addActionListener(paceListener);
                timer.setActionCommand("Timer");
                break;
            case "Self Pace":
                paceListener = new SelfPaceListener(experimentSettings, textOne, textTwo, beginButton, flipButton, nextWordButton);
                break;
        }
        beginButton.addActionListener(paceListener);
        flipButton.addActionListener(paceListener);
        nextWordButton.addActionListener(paceListener);
    }
    
    // Enter fullscreen exclusive mode
    public void display() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice displayDevice = graphicsEnvironment.getDefaultScreenDevice();
        if (displayDevice.isFullScreenSupported()) {
            // Make the JFrame Undecorated (no title bar or framing)
            setUndecorated(true);
            // Make the JFrame non-resizable
            setResizable(false);
            // Make the JFrame be the fullscreen window for the default display device
            displayDevice.setFullScreenWindow(this);
            /*******************************************************************
             * This two statements are a bug fix for Mac OSX with Java 1.7 and 
             * later. They prevent the loss of key binding function due to 
             * loss of focus upon entering full screen exclusive mode.
             * http://stackoverflow.com/questions/14317352/
             ******************************************************************/
            this.setVisible(false);
            this.setVisible(true);
            validate();
        }
        else {
            System.out.println("Full-screen mode not supported");
        }
    }
    
    // Key Binding of Escape to close program
    private class EscapeAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}