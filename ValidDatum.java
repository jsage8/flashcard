package flashcard;

// @author Jonathan Sage
// @phone 206-498-5442
// @email jsage8@gmail.com

public class ValidDatum {
    private boolean isValid;
    private String errorMessage;
    private boolean fileExists;
    
    public ValidDatum(boolean isValid) {
        this.isValid = isValid;
        this.errorMessage = null;
    }
    
    public ValidDatum(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }
    
    public ValidDatum(boolean isValid, String errorMessage, boolean fileExists) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
        this.fileExists = fileExists;
    }
    
    public boolean isValid() {
        return isValid;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public boolean fileExists() {
        return fileExists;
    }
}
