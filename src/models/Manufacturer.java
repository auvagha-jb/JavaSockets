package models;

import java.util.Map;

public class Manufacturer {
    private int manufacturerId;
    private final String companyName;
    private String streetAddress;
    private int zipCode;
    private String country;
    private String createdAt;

    
    public int getManufacturerId() {
        return manufacturerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    /*For manual insert queries*/
    public Manufacturer(String companyName, String streetAddress, int zipCode, String country) {
        this.companyName = companyName;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.country = country;
    }
    
    
    public Manufacturer(Map<String, String>formInput) {
        this.companyName = formInput.get(COMPANY_NAME);
        this.streetAddress = formInput.get(STREET_ADDRESS);
        this.zipCode = Integer.parseInt(formInput.get(ZIP_CODE));
        this.country = formInput.get(COUNTRY);
    }
    
    /*For select all queries*/
    public Manufacturer(int manufacturerId, String companyName, String streetAddress, int zipCode, String country, String createdAt) {
        this.manufacturerId = manufacturerId;
        this.companyName = companyName;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.country = country;
        this.createdAt = createdAt;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
       
    
    /*For select company name queries*/
    public Manufacturer(int companyId, String companyName) {
        this.companyName = companyName;
    }
    
    
    public String manufacturerInfoToString() {
        return String.format("[%s, %s, %s, %s, %s]", manufacturerId, companyName,streetAddress,zipCode,country);
    }
    
     public String identificationDetailsToString() {
         return String.format("%s - %s", manufacturerId, companyName);
     }
    
     
    public static final String COMPANY_NAME = "companyName";
    public static final String STREET_ADDRESS = "streetAddress";
    public static final String ZIP_CODE = "zipCode";
    public static final String COUNTRY = "country";
}
