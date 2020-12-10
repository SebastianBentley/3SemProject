package dtos;

public class FSDTO {

    private String name, status, species, gender, hair;

    public FSDTO(String name, String status, String species, String genter, String hair) {
        this.name = name;
        this.status = status;
        this.species = species;
        this.gender = genter;
        this.hair = hair;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGenter() {
        return gender;
    }

    public void setGenter(String genter) {
        this.gender = genter;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

}
