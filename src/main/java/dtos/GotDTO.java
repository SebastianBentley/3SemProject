/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author Mibse
 */
public class GotDTO {
    
    private String name;
    private String region;
    private String coatOfArms;
    private String founded;
    private String founder;
    private String diedOut;

    public GotDTO(String name, String region, String coatOfArms, String founded, String founder, String diedOut) {
        this.name = name;
        this.region = region;
        this.coatOfArms = coatOfArms;
        this.founded = founded;
        this.founder = founder;
        this.diedOut = diedOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCoatOfArms() {
        return coatOfArms;
    }

    public void setCoatOfArms(String coatOfArms) {
        this.coatOfArms = coatOfArms;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getDiedOut() {
        return diedOut;
    }

    public void setDiedOut(String diedOut) {
        this.diedOut = diedOut;
    }
    
    
}
