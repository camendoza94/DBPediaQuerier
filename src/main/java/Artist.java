public class Artist {
    private String name;
    private String nacionality;
    private int birthYear;

    Artist(String name, String nacionality, int birthYear) {
        this.name = name;
        this.nacionality = nacionality;
        this.birthYear = birthYear;
    }

    String getNacionality() {
        return nacionality;
    }

    public void setNacionality(String nacionality) {
        this.nacionality = nacionality;
    }

    int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}