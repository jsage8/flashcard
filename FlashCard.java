package flashcard;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class FlashCard extends JFrame {
    private final PacePanel paceOptionCardPanel;
    
    private final JMenuItem overwriteChoice;
    
    // Declare text fields for taking experiment information
    private final JTextField subjectNumberText;
    private final JTextField subjectAgeText;
    private final JTextField experimentInitialsText;
    private final JTextField experimentDateText;
    
    // Declare dropdown menu fields for taking experiment information
    private final JComboBox<String> paceComboBox;
    
    // Declare pace array
    private String[] paceArray = {"Automatic Pace", "Automatic Pace/Spacebar to Pause", "Automatic Pace/Automatic Pause", "Self Pace"};
    
    // Declare button to start flashcards
    private final JButton launchButton;
    
    // Turning on overwrite permission will set this to true
    private boolean overwrite = false;
    
    // ActionListener that validates and passes information from the 
    // experimenter GUI to the show.
    FrameListener passer;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FlashCard gui = new FlashCard();
                gui.setVisible(true);
            }
        });
    }
    
    public FlashCard() {
        // JFrame Title
        super("Flash Cards");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Add a set of image icons to the current JFrame for Windows jar application
        URL flashcard16 = getClass().getClassLoader().getResource("resources/img/flashcard16.png");
        URL flashcard32 = getClass().getClassLoader().getResource("resources/img/flashcard32.png");
        URL flashcard64 = getClass().getClassLoader().getResource("resources/img/flashcard64.png");
        URL flashcard128 = getClass().getClassLoader().getResource("resources/img/flashcard128.png");
        List<Image> icons = new ArrayList<>();
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
        
        /***********************************************************************
         * Begin Menu Bar
         **********************************************************************/
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        
        // Menu option to enter overwrite mode with allows previous file
        // overwriting.
        overwriteChoice = new JMenuItem("Enter Overwrite Mode");
        overwriteChoice.setActionCommand("Overwrite");
        overwriteChoice.addActionListener(new MenuListener());
        fileMenu.add(overwriteChoice);
        
        // Menu option to clear all data fields.
        JMenuItem clearChoice = new JMenuItem("Clear Fields");
        clearChoice.addActionListener(new MenuListener());
        fileMenu.add(clearChoice);
        
        fileMenu.addSeparator();
        
        // Menu option to quit program
        JMenuItem exitChoice = new JMenuItem("Exit");
        exitChoice.addActionListener(new MenuListener());
        fileMenu.add(exitChoice);
        
        // Menu option to open about window
        JMenuItem aboutChoice = new JMenuItem("About This Program");
        aboutChoice.setActionCommand("About");
        aboutChoice.addActionListener(new MenuListener());
        helpMenu.add(aboutChoice);
        
        JMenuBar bar = new JMenuBar();
        bar.add(fileMenu);
        bar.add(helpMenu);
        setJMenuBar(bar);
        /***********************************************************************
         * End Menu Bar
         **********************************************************************/
        
        /***********************************************************************
         * Begin Main Content Panel
         **********************************************************************/
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));
        mainPanel.setBackground(new Color(218, 218, 218));
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel holds experiment information not related to pace or word list
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setBorder(new EmptyBorder(10,0,10,0));
        basicInfoPanel.setBackground(Color.WHITE);
        basicInfoPanel.setLayout(new BorderLayout());
        mainPanel.add(basicInfoPanel, BorderLayout.NORTH);
        
        // Panel holds the labels for each field in the basicInfoPanel
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(5, 1, 10, 10));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setBorder(new EmptyBorder(0,5,0,5));
        basicInfoPanel.add(labelPanel, BorderLayout.WEST);
        
        // Panel holds the form fields for basicInfoPanel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(0,5,0,5));
        basicInfoPanel.add(formPanel, BorderLayout.CENTER);
        
        // Panel holds pace and word list options
        JPanel wordPacePanel = new JPanel();
        wordPacePanel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        wordPacePanel.setBackground(Color.WHITE);
        mainPanel.add(wordPacePanel, BorderLayout.CENTER);
        
        // Panel holds word list options
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new GridBagLayout());
        wordPanel.setBackground(Color.WHITE);
        wordPacePanel.add(wordPanel, gridBagConstraints);
        
        // Panel holds pace options
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        JPanel pacePanel = new JPanel();
        pacePanel.setLayout(new GridBagLayout());
        pacePanel.setBackground(Color.WHITE);
        wordPacePanel.add(pacePanel, gridBagConstraints);
        
        // Panel holds submit button
        JPanel submitPanel = new JPanel();
        submitPanel.setBorder(new EmptyBorder(10,5,10,5));
        submitPanel.setLayout(new BorderLayout());
        submitPanel.setBackground(Color.WHITE);
        mainPanel.add(submitPanel, BorderLayout.SOUTH);
        /***********************************************************************
         * End Main Content Panel
         **********************************************************************/
        
        /***********************************************************************
         * Begin labelPanel and formPanel Content Panel
         **********************************************************************/
        JLabel subjectNumberLabel = new JLabel("Subject Number: ");
        labelPanel.add(subjectNumberLabel);
        subjectNumberText = new JTextField(30);
        subjectNumberText.setText(getNextSubjectNumber());
        formPanel.add(subjectNumberText);

        JLabel subjectAgeLabel = new JLabel("Subject Age: ");
        labelPanel.add(subjectAgeLabel);
        subjectAgeText = new JTextField(30);
        formPanel.add(subjectAgeText);

        JLabel subjectSexLabel = new JLabel("Subject Sex: ");
        labelPanel.add(subjectSexLabel);
        String[] sexArray = new String[2];
        sexArray[0] = "Male";
        sexArray[1] = "Female";
        JComboBox<String> sexComboBox = new JComboBox<>(sexArray);
        formPanel.add(sexComboBox);
        
        JLabel experimentInitialsLabel = new JLabel("Experimenter's Initials: ");
        labelPanel.add(experimentInitialsLabel);
        experimentInitialsText = new JTextField(30);
        formPanel.add(experimentInitialsText);

        JLabel experimentDateLabel = new JLabel("Experiment Date: ");
        labelPanel.add(experimentDateLabel);
        experimentDateText = new JTextField(30);
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        experimentDateText.setText(dateFormat.format(date));
        experimentDateText.setEditable(false);
        formPanel.add(experimentDateText);
        /***********************************************************************
         * End labelPanel and formPanel Content Panel
         **********************************************************************/
        
        // Get Word List Files
        String[] wordListArray = getWordListFiles();
        
        /***********************************************************************
         * Begin wordPanel Content
         **********************************************************************/
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(0, 5, 0, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        wordPanel.add(new JLabel("Practice Word List:"), gridBagConstraints);
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(0, 5, 0, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        wordPanel.add(new JLabel("Experiment Word List:"), gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(5, 5, 0, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        JComboBox<String> practiceWordListComboBox = new JComboBox<>(wordListArray);
        practiceWordListComboBox.setEditable(false);
        practiceWordListComboBox.setSelectedIndex(checkForDefault(wordListArray, "practice"));
        wordPanel.add(practiceWordListComboBox, gridBagConstraints);
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(5, 5, 0, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        JComboBox<String> experimentWordListComboBox = new JComboBox<>(wordListArray);
        experimentWordListComboBox.setEditable(false);
        practiceWordListComboBox.setSelectedIndex(checkForDefault(wordListArray, "experiment"));
        wordPanel.add(experimentWordListComboBox, gridBagConstraints);
        /***********************************************************************
         * End wordPanel Content
         **********************************************************************/
        
        /***********************************************************************
         * Begin pacePanel Content
         **********************************************************************/
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(5, 5, 0, 25);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        pacePanel.add(new JLabel("Pace"), gridBagConstraints);
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new Insets(5, 5, 0, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        pacePanel.add(new JLabel("Pace Options"), gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(5, 5, 0, 5);
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        paceComboBox = new JComboBox<>(paceArray);
        paceComboBox.addActionListener(new PaceListener());
        pacePanel.add(paceComboBox, gridBagConstraints);
        
        // Use PacePanel Class to make a card layout for pace options
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(5, 5, 0, 0);
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        paceOptionCardPanel = new PacePanel(paceArray);
        pacePanel.add(paceOptionCardPanel, gridBagConstraints);
        /***********************************************************************
         * End wordPanel Content
         **********************************************************************/    
                
        /****************************************** 
         * Begin Key Binding
         * Declare, pair, and map actions used for keybinding
         * pressing 'ENTER' will act like clicking the launchButton.
         ******************************************/
        // Key bound AbstractAction items 
        Action enterAction = new EnterAction();
        // Gets the mainImagePanel InputMap and pairs the key to the action
        mainPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ENTER"), "doEnterAction");
        // This line pairs the AbstractAction enterAction to the action "doEnterAction"
        mainPanel.getActionMap().put("doEnterAction", enterAction);
        /******************************************
         * End Key Binding
         ******************************************/
        
        /***********************************************************************
         * Begin launchButton
         **********************************************************************/
        launchButton = new JButton("Begin Flash Cards");
        launchButton.setActionCommand("Begin Flash Cards");
        submitPanel.add(launchButton);
          
        passer = new FrameListener(subjectNumberText, subjectAgeText, sexComboBox, experimentInitialsText, experimentDateText, practiceWordListComboBox, experimentWordListComboBox, paceComboBox);
        launchButton.addActionListener(passer);
        /***********************************************************************
         * End launchButton
         **********************************************************************/
        
        pack();
    }
    
    // ActionListener that controls the bar menu behaviour
    private class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            switch (actionCommand) {
                case "Overwrite":
                    ConfirmWindow confirmation = new ConfirmWindow("Are you sure you want to enter Overwrite Mode?");
                    confirmation.setVisible(true);
                    break;
                case "Clear Fields":
                    subjectNumberText.setText("");
                    subjectAgeText.setText("");
                    experimentInitialsText.setText("");
                    experimentDateText.setText("");
                    break;
                case "Exit":
                    System.exit(0);
                case "About":
                    AboutWindow aboutWindow = new AboutWindow();
                    aboutWindow.setVisible(true);
                    break;
            }
        }
    }
    
    // ActionListener that controls the CardLayout of pace options
    private class PaceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout c1 = (CardLayout)(paceOptionCardPanel.getLayout());
            c1.show(paceOptionCardPanel, (String)paceComboBox.getSelectedItem());
        }
    }
    
    private class FrameListener implements ActionListener {
        private final JTextField subjectNumberText;
        private final JTextField subjectAgeText;
        private final JComboBox sexComboBox;
        private final JTextField experimentInitialsText;
        private final JTextField experimentDateText;
        private final JComboBox practiceWordListComboBox;
        private final JComboBox experimentWordListComboBox;
        private JComboBox paceComboBox;
        
        public FrameListener(JTextField subjectNumberText, 
                JTextField subjectAgeText, 
                JComboBox sexComboBox, 
                JTextField experimentInitialsText, 
                JTextField experimentDateText,
                JComboBox practiceWordListComboBox,
                JComboBox experimentWordListComboBox, 
                JComboBox paceComboBox) {
            this.subjectNumberText = subjectNumberText;
            this.subjectAgeText = subjectAgeText;
            this.sexComboBox = sexComboBox;
            this.experimentInitialsText = experimentInitialsText;
            this.experimentDateText = experimentDateText;
            this.practiceWordListComboBox = practiceWordListComboBox;
            this.experimentWordListComboBox = experimentWordListComboBox;
            this.paceComboBox = paceComboBox;
        }
               
        public void setPaceInputArray(JComboBox paceComboBox) {
            this.paceComboBox = paceComboBox;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            
            if(actionCommand.equals("Begin Flash Cards")) {
                String subjectNumber = subjectNumberText.getText();
                String subjectAge = subjectAgeText.getText();
                String subjectSex = (String)sexComboBox.getSelectedItem();
                String experimentInitials = experimentInitialsText.getText();
                String experimentDate = experimentDateText.getText();
                String practiceWordList = (String)practiceWordListComboBox.getSelectedItem();
                String experimentWordList = (String)experimentWordListComboBox.getSelectedItem();
                String experimentPace = (String)paceComboBox.getSelectedItem();
                int paceIndex = paceComboBox.getSelectedIndex();
                HashMap<String, String> paceOptions = paceOptionCardPanel.getPaceOptions(paceIndex);
                
                if(validateUserInput(subjectNumber, subjectAge, experimentInitials, experimentDate, paceOptions)) {
                    printUserInput(subjectNumber, subjectAge, subjectSex, experimentInitials, experimentDate, practiceWordList, experimentWordList, experimentPace, paceOptions);
                    ExperimentSettings[] experimentSettings = setExperimentSettings(subjectNumber, readWordList(practiceWordList), readWordList(experimentWordList), experimentPace, paceOptions);
                    FlashCard.this.dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new CardShow(experimentSettings).display();
                        }
                    });
                }
            }
        }
        
        private boolean validateUserInput(String subjectNumber, 
                String subjectAge, 
                String experimentInitials, 
                String experimentDate,
                HashMap<String, String> paceOptions) { 
            
            ValidDatum[] validData = new ValidDatum[5];
            validData[0] = validateSubjectNumber(subjectNumber);
            
            boolean isValid = true;
            String errorMessage = "<html>";
            
            //If subjectNumber already exists, check to see if user wants to overwrite
            if(validData[0].fileExists()) {
                ConfirmWindow confirmation = new ConfirmWindow(validData[0].getErrorMessage());
                confirmation.setVisible(true);
                return !isValid;
            }
            
            validData[1] = validateSubjectAge(subjectAge);
            validData[2] = validateExperimentInitials(experimentInitials);
            validData[3] = validateExperimentDate(experimentDate);
            validData[4] = validatePaceOptions(paceOptions);
            
            //Check each validated field, set isValid to false if any field is invalid
            for(int i = 0; i < validData.length; i++) {
                if(!validData[i].isValid()) {
                    isValid = false;
                    errorMessage += validData[i].getErrorMessage() + "<br>";
                }
            }
            //Create a specific error message based on which data failed to validate
            if(!isValid) {
                errorMessage += "</html>";
                ErrorWindow smallWindow = new ErrorWindow(errorMessage);
                smallWindow.setVisible(true);
            }
            return isValid;
        }
        
        private ValidDatum validateSubjectNumber(String subjectNumber) {
            if(!(subjectNumber.equals(""))) {
                File fileObject = new File(subjectNumber + ".txt");
                if(fileObject.exists() && !overwrite) {
                    String errorMessage = "The file already exists, Overwrite Mode?";
                    return new ValidDatum(true, errorMessage, true);
                }
                else if(!(fileObject.exists()) || overwrite) {
                    return new ValidDatum(true);
                }
            }
            String errorMessage = "Subject Number Invalid";
            return new ValidDatum(false, errorMessage);
        }
        
        private ValidDatum validateSubjectAge(String subjectAge) {
            if(subjectAge.matches("\\d{1,3}+(\\.\\d+)?")) {
                return new ValidDatum(true);
            }
            else {
                String errorMessage = "Subject Age Invalid";
                return new ValidDatum(false, errorMessage);
            }
        }
        
        private ValidDatum validateExperimentInitials(String experimentInitials) {
            if(experimentInitials.matches("[a-zA-Z]{2,5}+")) {
                return new ValidDatum(true);
            }
            else {
                String errorMessage = "Experimenter Initials Invalid";
                return new ValidDatum(false, errorMessage);
            }
        }
        
        private ValidDatum validateExperimentDate(String experimentDate) {
            if(experimentDate.matches("\\d{1,2}+-{1}+\\d{1,2}+-{1}+\\d{4}+")) {
                return new ValidDatum(true);
            }
            else {
                String errorMessage = "Experiment Date Invalid: Use format MM-DD-YYYY";
                return new ValidDatum(false, errorMessage);
            }
        }
        
        private ValidDatum validatePaceOptions(HashMap<String, String> paceOptions) {
            boolean validTime = paceOptions.get("time").matches("\\d+");
            boolean validFlipCount = paceOptions.get("flipCount").matches("\\d+");
            boolean validWordCount = paceOptions.get("wordCount").matches("\\d+");
            if(validTime && validFlipCount && validWordCount) {
                return new ValidDatum(true);
            }
            else {
                String errorMessage = "Pace Options Error";
                if(!validTime) {
                    errorMessage += "<br>Pace timing option invalid, must be an integer";
                }
                if(!validFlipCount) {
                    errorMessage += "<br>Pace flips option invalid, must be an integer";
                }
                if(!validWordCount) {
                    errorMessage += "<br>Pace pause after words option invalid, must be an integer";
                }
                return new ValidDatum(false, errorMessage);
            }
        }
        
        private void printUserInput(String subjectNumber, 
                String subjectAge, 
                String subjectSex, 
                String experimentInitials, 
                String experimentDate, 
                String practiceWordList, 
                String experimentWordList, 
                String experimentPace, 
                HashMap<String, String> paceOptions) {
            File fileObject = new File(subjectNumber + ".txt");
            PrintWriter outputStream = writeFile(fileObject);
            outputStream.println("Subject Number:\t" + subjectNumber);
            outputStream.println("Subject Age:\t" + subjectAge);
            outputStream.println("Subject Sex:\t" + subjectSex);
            outputStream.println("Experimenter Initials:\t" + experimentInitials);
            outputStream.println("Experiment Date:\t" + experimentDate);
            outputStream.println("Practice Word List:\t" + practiceWordList);
            outputStream.println("Experiment Word List:\t" + experimentWordList);
            outputStream.println("Experiment Pace:\t" + experimentPace);
            switch(experimentPace) {
                case "Automatic Pace":
                    outputStream.println("Timing:\t" + paceOptions.get("time"));
                    outputStream.println("Flip Count:\t" + paceOptions.get("flipCount") + "\n");
                    break;
                case "Automatic Pace/Spacebar to Pause":
                    outputStream.println("Timing:\t" + paceOptions.get("time"));
                    outputStream.println("Flip Count:\t" + paceOptions.get("flipCount") + "\n");
                    break;
                case "Automatic Pace/Automatic Pause":
                    outputStream.println("Timing:\t" + paceOptions.get("time"));
                    outputStream.println("Flip Count:\t" + paceOptions.get("flipCount"));
                    outputStream.println("Pause After Word Count:\t" + paceOptions.get("wordCount") + "\n");
                    break;
                case "Self Pace":
                    // No Options Applicable
                    break;
            }
            outputStream.close();
        }
        
        private ArrayList<Word> readWordList(String wordList) {
            ArrayList<Word> words = new ArrayList<>();
            File fileObject = new File(wordList);
            try {
                FileReader inputStream = new FileReader(fileObject);
                BufferedReader bufferedReader = new BufferedReader(inputStream);
                String line;
                String[] lineArray;
                while((line = bufferedReader.readLine()) != null) {
                    lineArray = line.split("\\t");
                    String word = lineArray[0].trim();
                    String pronunciation = lineArray[1].trim();
                    String definition = lineArray[2].trim();
                    words.add(new Word(word, pronunciation, definition));
                }
                return words;
            }
            catch(IOException e) {
                System.out.println("Cannot find file " + fileObject.getName() + ".txt or it could not be opened.");
                System.exit(0);
                return null;
            }
        }
    }
    
    private ExperimentSettings[] setExperimentSettings(String subjectNumber, ArrayList<Word> practiceWordList, ArrayList<Word> experimentWordList, String experimentPace, HashMap<String, String> paceOptions) {
        File outputFile = new File(subjectNumber + ".txt");
        ExperimentSettings[] experimentSettings = new ExperimentSettings[2];
        Word[] practiceWordListArray = practiceWordList.toArray(new Word[practiceWordList.size()]);
        Word[] experimentWordListArray = experimentWordList.toArray(new Word[experimentWordList.size()]);
        int time = Integer.parseInt(paceOptions.get("time"));
        int flipCount = Integer.parseInt(paceOptions.get("flipCount"));
        int wordCount = Integer.parseInt(paceOptions.get("wordCount"));
        String practiceBegin = "Begin Practice?";
        String practiceEnd = "End Practice";
        String experimentBegin = "Begin Experiment?";
        String experimentEnd = "End";
        switch(experimentPace) {
            case "Automatic Pace":
                experimentSettings[0] = new ExperimentSettings(outputFile, practiceWordListArray, experimentPace, time, flipCount, practiceBegin, practiceEnd);
                experimentSettings[1] = new ExperimentSettings(outputFile, experimentWordListArray, experimentPace, time, flipCount, experimentBegin, experimentEnd);
                break;
            case "Automatic Pace/Spacebar to Pause":
                experimentSettings[0] = new ExperimentSettings(outputFile, practiceWordListArray, experimentPace, time, flipCount, practiceBegin, practiceEnd);
                experimentSettings[1] = new ExperimentSettings(outputFile, experimentWordListArray, experimentPace, time, flipCount, experimentBegin, experimentEnd);
                break;
            case "Automatic Pace/Automatic Pause":
                experimentSettings[0] = new ExperimentSettings(outputFile, practiceWordListArray, experimentPace, time, flipCount, wordCount, practiceBegin, practiceEnd);
                experimentSettings[1] = new ExperimentSettings(outputFile, experimentWordListArray, experimentPace, time, flipCount, wordCount, experimentBegin, experimentEnd);
                break;
            case "Self Pace":
                experimentSettings[0] = new ExperimentSettings(outputFile, practiceWordListArray, experimentPace, practiceBegin, practiceEnd);
                experimentSettings[1] = new ExperimentSettings(outputFile, experimentWordListArray, experimentPace, experimentBegin, experimentEnd);
                break;
        }
        return experimentSettings;
    }
        
    private class ErrorWindow extends JFrame {
        public ErrorWindow(String e) {
            String errorMessage = e;
            getContentPane().setBackground(Color.WHITE);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BorderLayout());
            errorPanel.setBorder(new EmptyBorder(25,25,25,25));
            add(errorPanel);
            
            JLabel errorLabel = new JLabel(errorMessage);
            errorLabel.setBorder(new EmptyBorder(0,0,10,0));
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            
            JButton exitButton = new JButton("Close");
            exitButton.addActionListener(new ExitListener());
            errorPanel.add(exitButton, BorderLayout.SOUTH);
            pack();
        }
    }
    
    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            JButton sourceButton = (JButton)e.getSource();
            Component frameCheck = sourceButton;
            String frameTest = "null";
            Class<?> c;
            while(!frameTest.equals("javax.swing.JFrame")) {
                frameCheck = frameCheck.getParent();
                c = frameCheck.getClass();
                frameTest = c.getSuperclass().getName();
            }
            JFrame frame = (JFrame)frameCheck;
            switch (actionCommand) {
                case "Close":
                    frame.dispose();
                    break;
                case "Overwrite":
                    overwriteChoice.setText("Quit Overwrite Mode");
                    overwrite = true;
                    launchButton.setText("Begin [Overwrite Mode]");
                    launchButton.setBackground(Color.RED);
                    launchButton.setOpaque(true);
                    frame.dispose();
                    break;
                case "Quit Overwrite":
                    overwriteChoice.setText("Enter Overwrite Mode");
                    overwrite = false;
                    launchButton.setText("Begin Flash Cards");
                    //Reset default button color
                    launchButton.setBackground(new JButton().getBackground());
                    launchButton.setOpaque(false);
                    frame.dispose();
                    break;
                case "Cancel":
                    frame.dispose();
                    break;
            }
        }
    }
    
    private class ConfirmWindow extends JFrame {
        private final String confirmMessage;
        
        public ConfirmWindow(String msg) {
            this.confirmMessage = msg;
            
            setTitle("Confirm Overwrite Permission");
            getContentPane().setBackground(Color.WHITE);
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setLayout(new BorderLayout());
            
            JLabel confirmLabel = new JLabel(confirmMessage);
            confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(confirmLabel, BorderLayout.CENTER);
            
            JPanel confirmPanel = new JPanel();
            confirmPanel.setLayout(new FlowLayout());
            add(confirmPanel,BorderLayout.SOUTH);
            
            JButton overwriteButton;
            if(overwrite) {
                overwriteButton = new JButton("Quit Overwrite");
            }
            else {
                overwriteButton = new JButton("Overwrite");
            }
            overwriteButton.addActionListener(new ExitListener());
            confirmPanel.add(overwriteButton);
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ExitListener());
            confirmPanel.add(cancelButton);
            pack();
        }
    }
    
    private class AboutWindow extends JFrame {
        public AboutWindow() {
            setTitle("About This Program");
            getContentPane().setBackground(Color.WHITE);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            
            JPanel aboutPanel = new JPanel();
            aboutPanel.setLayout(new BorderLayout());
            aboutPanel.setBorder(new EmptyBorder(20,0,20,0));
            add(aboutPanel);
            
            JLabel line1 = new JLabel("Paced FlashCard v1.1");
            JLabel line2 = new JLabel("Written by Jonathan Sage");
            JLabel line3 = new JLabel("Compiled 2014");
            
            line1.setHorizontalAlignment(JLabel.CENTER);
            line2.setHorizontalAlignment(JLabel.CENTER);
            line3.setHorizontalAlignment(JLabel.CENTER);
            
            aboutPanel.add(line1, BorderLayout.NORTH);
            aboutPanel.add(line2, BorderLayout.CENTER);
            aboutPanel.add(line3, BorderLayout.SOUTH);
            
            JButton exitButton = new JButton("Close");
            exitButton.addActionListener(new ExitListener());
            add(exitButton, BorderLayout.SOUTH);
            pack();
        }
    }
    
    // Key Binding of Enter to the launch button to begin the show
    private class EnterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            launchButton.doClick();
        }
    }
    
    private String[] getWordListFiles() {
        ArrayList<String> wordListFiles = new ArrayList<>(100); 
        String wordListName;
        File folder = new File(".");
        File[] allFiles = folder.listFiles();

        for (File allFile : allFiles) {
            if (allFile.isFile()) {
                wordListName = allFile.getName();
                if(wordListName.endsWith(".txt")) {
                    wordListFiles.add(wordListName);
                }
            }
        }
        String[] wordListArray = wordListFiles.toArray(new String[wordListFiles.size()]);
        return wordListArray;
    }
    
    /***************************************************************************
     * Returns a subject number based on the data text files in the same working
     * directory. For example, if subject_0001 exists, but not subject_0002, it 
     * will return subject_0002.
     **************************************************************************/ 
    private String getNextSubjectNumber() {
        boolean exists = true;
        int digits = 4;
        int num;
        String format;
        String result;
        String subjectNumber = null;
        for(int i = 1; exists && i < 9999; i++) {
            exists = false;
            num = i;
            format = String.format("%%0%dd", digits);
            result = String.format(format, num);
            subjectNumber = "subject_" + result;
            File fileObject = new File(subjectNumber + ".txt");
            if(fileObject.exists()) {
                exists = true;
            }
        }
        return subjectNumber;
    }
    
    private int checkForDefault(String[] wordListArray, String prefix) {
        for(int i = 0; i < wordListArray.length; i++) {
            if(wordListArray[i].matches("practice.*")) {
                return i;
            }
        }
        return 0;
    }
    
    // Returns PrintWriter object for writing experiment information to file
    private PrintWriter writeFile(File fileObject) {
        PrintWriter outputStream;
        try {
            outputStream = new PrintWriter(new FileOutputStream(fileObject, false));
            return outputStream;
        }
        catch(FileNotFoundException e) {
            System.out.println("Cannot find file " + fileObject.getName() + ".txt or it could not be opened.");
            System.exit(0);
            return null;
        }
    }
}
