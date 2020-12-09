package dtos;

/**
 *
 * @author Mibse
 */
public class SwDTO {
    
    private String classification;
    private String designation;
    private String average_height;
    private String average_lifespan;
    private String skin_colors;
    private String language;
    private String name;

    public SwDTO(String classification, String designation, String average_height, String average_lifespan, String skin_colors, String language, String name) {
        this.classification = classification;
        this.designation = designation;
        this.average_height = average_height;
        this.average_lifespan = average_lifespan;
        this.skin_colors = skin_colors;
        this.language = language;
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAverage_height() {
        return average_height;
    }

    public void setAverage_height(String average_height) {
        this.average_height = average_height;
    }

    public String getAverage_lifespan() {
        return average_lifespan;
    }

    public void setAverage_lifespan(String average_lifespan) {
        this.average_lifespan = average_lifespan;
    }

    public String getSkin_colors() {
        return skin_colors;
    }

    public void setSkin_colors(String skin_colors) {
        this.skin_colors = skin_colors;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
