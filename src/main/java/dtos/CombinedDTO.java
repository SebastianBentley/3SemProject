package dtos;

public class CombinedDTO {
    private String description1;
    private String description2;
    private String description3;

    public CombinedDTO(MovieDTO movie1, MovieDTO movie2, MovieDTO movie3) {
        this.description1 = movie1.getPlot();
        this.description2 = movie2.getPlot();
        this.description3 = movie3.getPlot();
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
    
}
