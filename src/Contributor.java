import java.util.ArrayList;

public class Contributor {
    private String name;
    //private ArrayList<Role> skills;
    private ArrayList<Role> skills;
    private int availableFrom;

    public Contributor(String name, ArrayList<Role> skills) {
        this.name = name;
        this.skills = skills;
    }

    @Override
    public boolean equals(Object obj) {
        Contributor contributor = (Contributor) obj;
        return name.equals(contributor.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Role> getSkills() {
        return skills;
    }

    public int getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(int availableFrom) {
        this.availableFrom = availableFrom;
    }
}
