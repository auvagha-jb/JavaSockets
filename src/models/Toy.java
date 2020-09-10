package models;

import java.util.Map;

public class Toy {

    private int toyCode;
    private final String name;
    private String description;
    private double price;
    private int manufacturerId;
    private String manufacturer;
    private String manufactureDate;
    private int batchNumber;
    private String createdAt;

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setToyCode(int toyCode) {
        this.toyCode = toyCode;
    }

    
    public int getToyCode() {
        return toyCode;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public double getPrice() {
        return price;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    /*When inserting from manual input*/
    public Toy(String name, String description, double price, int manufacturerId, String manufactureDate, int batchNumber) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.manufacturerId = manufacturerId;
        this.manufactureDate = manufactureDate;
        this.batchNumber = batchNumber;
    }

    /*When inserting from form input */
    public Toy(Map<String, String> formInput) {
        this.name = formInput.get(NAME);
        this.description = formInput.get(DESCRIPTION);
        this.price = Double.parseDouble(formInput.get(PRICE));
        this.manufacturerId = Integer.parseInt(formInput.get(MANUFACTURER_ID));
        this.manufactureDate = formInput.get(MANUFACTURE_DATE);
        this.batchNumber = Integer.parseInt(formInput.get(BATCH_NUMBER));
    }

    /*When selecting toys */
    public Toy(int toyCode, String name, String description, double price, String manufacturer, String manufactureDate, int batchNumber, String createdAt) {
        this.toyCode = toyCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.manufacturer = manufacturer;
        this.manufactureDate = manufactureDate;
        this.batchNumber = batchNumber;
        this.createdAt = createdAt;
    }

    public Toy(int toyCode, String name) {
        this.toyCode = toyCode;
        this.name = name;
    }

    public String toyIdentificationToString() {
        return String.format("[TS%s, %s]", toyCode, name);
    }

    public String toyInformationToString() {
        return String.format("[%s, %s, %s, %s, %s, Manufactured by: %s]", name, description, price, manufactureDate, batchNumber, manufacturer);
    }
    
    
    public static String NAME = "name";
    public static String DESCRIPTION = "description";
    public static String PRICE = "price";
    public static String MANUFACTURER_ID = "manufacturerId";
    public static String MANUFACTURE_DATE = "manufactureDate";
    public static String BATCH_NUMBER = "batchNumber";
    
}
