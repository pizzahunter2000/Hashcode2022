import java.util.ArrayList;

public class Project implements Comparable<Project> {
    private String name;
    private int duration;
    private int score;
    private int bestBefore;

    private ArrayList<Role> roles;

    public Project(String name, int duration, int score, int bestBefore, ArrayList<Role> roles) {
        this.name = name;
        this.duration = duration;
        this.score = score;
        this.bestBefore = bestBefore;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(int bestBefore) {
        this.bestBefore = bestBefore;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int compareTo(Project p) {
        if(score / duration > p.getScore() / p.getDuration()){
            return -1;
        }
        if(score / duration < p.getScore() / p.getDuration()){
            return 1;
        }
        if(bestBefore > p.getBestBefore()){
            return 1;
        }

        if(bestBefore < p.getBestBefore()){
            return -1;
        }
        return 0;
    }
}
