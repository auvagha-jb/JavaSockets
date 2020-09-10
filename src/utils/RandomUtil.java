/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Timestamp;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jerry Auvagha
 */
public class RandomUtil {

    private static final int LEFT_LIMIT = 97; // letter 'a'
    private static final int RIGHT_LIMIT = 122; // letter 'z'

    public static int generateRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static String generateRandomString(int length) throws Exception {
        //Fail safe to avoid numbers < 1
        if(length < 1) {
            throw new Exception("The generated string cannot be less than 1 character long");
        }
        
        Random random = new Random();
        
        String generatedString = random.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(generatedString);
        return generatedString;
    }

    public static String getRandomId() {
        String uniqueId = null;
        try {
            int currentTimestamp = TimeUtil.getCurrentTimeStampInMilli();
            String randomString = generateRandomString(6);
            int randomInt = generateRandomInt(100000, 999999);
            
            uniqueId = String.format("%s-%s-%s", currentTimestamp, randomString, randomInt);
        } catch (Exception ex) {
            Logger.getLogger(RandomUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return uniqueId;
    }
    
}
