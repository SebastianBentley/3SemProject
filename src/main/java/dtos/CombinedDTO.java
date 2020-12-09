package dtos;





public class CombinedDTO {
    private String description1;
    private String description2;
    private String description3;
    private String gotName, gotRegion, gotCoat, gotFounded, gotFounder, gotDiedout;
    private String fsName, fsStatus, fsSpecies, fsGender, fsHair;
    


    public CombinedDTO(MovieDTO movie1, MovieDTO movie2, GotDTO gotDTO, FSDTO fsDTO) {
        this.description1 = movie1.getPlot();
        this.description2 = movie2.getPlot();
        
        this.gotName = gotDTO.getName();
        this.gotRegion = gotDTO.getRegion();
        this.gotCoat = gotDTO.getCoatOfArms();
        this.gotFounded = gotDTO.getFounded();
        this.gotFounder = gotDTO.getFounder();
        this.gotDiedout = gotDTO.getDiedOut();
        this.fsName = fsDTO.getName();
        this.fsStatus = fsDTO.getStatus();
        this.fsSpecies = fsDTO.getSpecies();
        this.fsGender = fsDTO.getGenter();
        this.fsHair = fsDTO.getHair();
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String getGotName() {
        return gotName;
    }

    public void setGotName(String gotName) {
        this.gotName = gotName;
    }

    public String getGotRegion() {
        return gotRegion;
    }

    public void setGotRegion(String gotRegion) {
        this.gotRegion = gotRegion;
    }

    public String getGotCoat() {
        return gotCoat;
    }

    public void setGotCoat(String gotCoat) {
        this.gotCoat = gotCoat;
    }

    public String getGotFounded() {
        return gotFounded;
    }

    public void setGotFounded(String gotFounded) {
        this.gotFounded = gotFounded;
    }

    public String getGotFounder() {
        return gotFounder;
    }

    public void setGotFounder(String gotFounder) {
        this.gotFounder = gotFounder;
    }

    public String getGotDiedout() {
        return gotDiedout;
    }

    public void setGotDiedout(String gotDiedout) {
        this.gotDiedout = gotDiedout;
    }

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String fsName) {
        this.fsName = fsName;
    }

    public String getFsStatus() {
        return fsStatus;
    }

    public void setFsStatus(String fsStatus) {
        this.fsStatus = fsStatus;
    }

    public String getFsSpecies() {
        return fsSpecies;
    }

    public void setFsSpecies(String fsSpecies) {
        this.fsSpecies = fsSpecies;
    }

    public String getFsGender() {
        return fsGender;
    }

    public void setFsGender(String fsGender) {
        this.fsGender = fsGender;
    }

    public String getFsHair() {
        return fsHair;
    }

    public void setFsHair(String fsHair) {
        this.fsHair = fsHair;
    }

    
  
    
}
