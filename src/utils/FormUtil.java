/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import models.Message;

/**
 *
 * @author Jerry Auvagha
 */
public class FormUtil {

    private final JTextField[] textFields;
    public static String MISSING_FIELDS_TITLE = "Form is incomplete";
    public static String FORM_SUBMITTED_TITLE = "Submission successful";
    public static String WRONG_FORMAT_TITLE = "Wrong format detected";
    
    public FormUtil(JTextField[] textFields) {
        this.textFields = textFields;
    }
    
    
    public Message validateForm(Map<String, String> inputs) {
        boolean valid = true;
        String feedbackMessage = "Please fill the following fields: \n";

        try {
            for (Map.Entry<String, String> input : inputs.entrySet()) {
            if (input.getValue().equals("")) {
                valid = false;
                feedbackMessage += String.format("%s\n", input.getKey());
            }
        }
        }catch(NullPointerException e) {
            System.err.printf("There was a null input field detected %s\n", e.getMessage());
        }

        return new Message(valid, feedbackMessage);
    }

    
    public static void successFeedback(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    public static void errorFeedback(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    
    public void clearTextFields() {
       for(JTextField textField : textFields) {
           textField.setText("");
       }
    }

//    public static void main(String[] args) {
//        Message msg = validateForm(
//                new HashMap(
//                        Map.ofEntries(
//                                Map.entry("companyName", ""),
//                                Map.entry("streetAddress", "streetAddress"),
//                                Map.entry("zipCode", "zipCode"),
//                                Map.entry("country", "country")
//                        )
//                )
//        );
//        
//        if(!msg.isStatus()) {
//            errorFeedback("Missing input", msg.getMessage());
//        }
//    }

}
