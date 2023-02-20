import java.util.Comparator;

public class Role implements Comparator<Role> {
    private String name;
    private int level;

    public Role(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public boolean equals(Role role) {
        return this.name.equals(role.getName()) && this.level == role.getLevel();
    }

    @Override
    public int compare(Role o1, Role o2) {
        if(o1.getName().equals(o2.getName())){
            if(o1.getLevel() >= o2.getLevel()){
                return 1;
            }
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
