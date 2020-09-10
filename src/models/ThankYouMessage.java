package models;

public class ThankYouMessage {
    private final String uniquId;
    private final String message;

    public String getMessageId() {
        return uniquId;
    }

    public String getMessage() {
        return message;
    }

    public ThankYouMessage(String messageId, String message) {
        this.uniquId = messageId;
        this.message = message;
    }
    
    
    public String messageToString() {
        return String.format("[%s]: %s", uniquId, message);
    }

}
