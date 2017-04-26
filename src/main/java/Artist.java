public class Artist {
    private String name;
    private String nacionality;
    private int birthYear;

    public Artist(String name, String nacionality, int birthYear) {
        this.name = name;
        this.nacionality = nacionality;
        this.birthYear = birthYear;
    }

    public String getNacionality() {
        return nacionality;
    }

    public void setNacionality(String nacionality) {
        this.nacionality = nacionality;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}