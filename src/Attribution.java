import java.io.*;
import java.util.*;

public class Attribution {
    private static int nrOfContributors;
    private static int nrOfProjects;
    private static ArrayList<Contributor> contributors = new ArrayList<>();
    private static ArrayList<Project> projects = new ArrayList<>();

    public static void readInput(String fileName){
        try {
            File file = new File(fileName);
            if(file == null)
                throw  new FileNotFoundException("File not found");
            Scanner scanner = new Scanner(file);

            nrOfContributors = scanner.nextInt();
            nrOfProjects = scanner.nextInt();

            //read contributors data
            for(int i = 0; i < nrOfContributors; i++){
                String name = scanner.next();
                int nrOfSkills = scanner.nextInt();

                //create and read skills list
                ArrayList<Role> skills = new ArrayList<>();
                for(int j = 0; j < nrOfSkills; j++){
                    String skillName = scanner.next();
                    int level = scanner.nextInt();

                    Role role = new Role(skillName, level);
                    skills.add(role);
                }

                Contributor contributor = new Contributor(name, skills);
                contributors.add(contributor);
            }

            //read project data
            for(int i = 0; i < nrOfProjects; i++){
                String projectName = scanner.next();
                int daysToComplete = scanner.nextInt();
                int score = scanner.nextInt();
                int before = scanner.nextInt();
                int nrOfRoles = scanner.nextInt();
                ArrayList<Role> roles = new ArrayList<>();

                for(int j = 0; j < nrOfRoles; j++){
                    String roleName = scanner.next();
                    int level = scanner.nextInt();

                    Role role = new Role(roleName, level);
                    roles.add(role);
                }

                Project project = new Project(projectName, daysToComplete, score, before, roles);
                projects.add(project);
            }
        } catch (FileNotFoundException e){
            System.out.println(e);
        }
    }

    public static void printOutput(String outputString, String fileName){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            //writer.write((int)countProjects);
            writer.write("\n");
            writer.write(outputString);
            writer.close();
            System.out.println("File was created successfully");
        } catch (IOException e){
            System.out.println(e);
        }

    }

    public static LinkedHashMap<Role, Contributor> searchForContributor(Project project, int currentTime){
        LinkedHashMap<Role, Contributor> team = new LinkedHashMap<>();
        for(Role role: project.getRoles()){
            Contributor chosenContributor = null;
            int minLevel = 100;
            for(Contributor contributor: contributors){
                boolean isValid = false;
                boolean foundPerfect = false;
                for(Role skill: contributor.getSkills()){
                    if(skill.getName().equals(role.getName()) && skill.getLevel() >= role.getLevel() &&
                            minLevel > skill.getLevel() && currentTime >= contributor.getAvailableFrom()
                            && !team.containsValue(contributor)){
                        isValid = true;
                        minLevel = skill.getLevel();
                    }
                    /*
                    // check if a contributor can be mentored
                    // if contributor has skill at level of role - 1
                    else if(skill.getName().equals(role.getName()) && skill.getLevel() == role.getLevel() - 1){
                        for(Map.Entry<Role, Contributor> entry: team.entrySet()){
                            if(entry.getValue().getSkills().contains(role)){
                                boolean found = false;
                                for(Role checkSkills: entry.getValue().getSkills()){
                                    if(checkSkills.getName().equals(role.getName()) && checkSkills.getLevel() >= role.getLevel()){
                                        found = true;
                                        break;
                                    }
                                }
                                if(found){
                                    skill.setLevel(skill.getLevel() + 1);
                                    foundPerfect = true;
                                    chosenContributor = contributor;
                                    break;
                                }
                            }
                        }
                    }
                    // if contributor does not have the skill, but the role is at level 1
                    else if(role.getLevel() == 1){
                        for(Map.Entry<Role, Contributor> entry: team.entrySet()){
                            if(entry.getValue().getSkills().contains(role)){
                                boolean found = false;
                                for(Role checkSkills: entry.getValue().getSkills()){
                                    if(checkSkills.getName().equals(role.getName()) && checkSkills.getLevel() >= role.getLevel()){
                                        found = true;
                                        break;
                                    }
                                }
                                if(found){
                                    contributor.getSkills().add(role);
                                    chosenContributor = contributor;
                                    foundPerfect = true;
                                    break;
                                }
                            }
                        }
                    }*/
                }
                if(foundPerfect){
                    break;
                }
                if(isValid){
                    chosenContributor = contributor;
                }
            }
            if(chosenContributor == null){
                return null;
            }
            team.put(role, chosenContributor);
        }
        return team;
    }

    public static String completeProject(Project project, int currentTime){
        LinkedHashMap<Role, Contributor> team = searchForContributor(project, currentTime);
        if(team == null)
            return "";
        String contributorsString = "";

        for(Map.Entry<Role, Contributor> entry: team.entrySet()){
            entry.getValue().setAvailableFrom(project.getDuration() + currentTime);
            contributorsString += entry.getValue().getName() + " ";
            for(Role skill: entry.getValue().getSkills()){
                // if the role is the same level as the skill
                if(skill.equals(entry.getKey()) && skill.getLevel() == entry.getKey().getLevel()){
                    skill.setLevel(skill.getLevel() + 1);
                }
            }
        }
        contributorsString = contributorsString.substring(0, contributorsString.length() - 1);
        return contributorsString;
    }

    public static void processTest(String inputFileName, String outputFileName){
        readInput(inputFileName);
        Collections.sort(projects);

        int timer = 0, maxProjectLength = 0, minProjectLength = 0;
        boolean canLaunchProject;
        int countProjects = 0;
        String output = "";
        do {
            canLaunchProject = false;
            for(Project project: projects){
                if(project.getBestBefore() + project.getDuration() >= timer){
                    canLaunchProject = true;
                    break;
                }
            }
            if(!canLaunchProject){
                break;
            }
            ArrayList<Project> projectsToDelete = new ArrayList<>();
            for(Project project: projects){
                String contributorsString;
                if(!(contributorsString = completeProject(project, timer)).equals("")){
                    output += project.getName() + "\n";
                    output += contributorsString + "\n";
                    projectsToDelete.add(project);
                    countProjects++;
                }
            }
            for(Project project: projectsToDelete){
                projects.remove(project);
                if(project.getDuration() + timer > maxProjectLength){
                    maxProjectLength = project.getDuration() + timer;
                }
                if(minProjectLength <= timer){
                    minProjectLength = timer + project.getDuration();
                }
                else if(project.getDuration() + timer < minProjectLength){
                    minProjectLength = project.getDuration() + timer;
                }
            }
            if(minProjectLength <= timer){
                timer++;
                minProjectLength = timer;
            } else{
                timer = minProjectLength;
            }
            if(projectsToDelete.isEmpty()){
                timer++;
            }
            //System.out.println(projectsToDelete.size() + " projects deleted at " + timer);
        } while (timer <= maxProjectLength && !projects.isEmpty());
        //System.out.println(countProjects);
        output = Integer.toString(countProjects) + "\n" + output;
        printOutput(output, outputFileName);
        //System.out.println("Stopped at " + timer + ", max being " + maxProjectLength);
        //System.out.println("\n-----------------------------------------------\n");
        projects.removeAll(projects);
        contributors.removeAll(contributors);
    }

    public static void main(String[] args) {
        //processTest("src/inputA.txt", "src/outputA.txt");
        processTest("src/inputB.txt", "src/outputB.txt");
        //processTest("src/inputC.txt", "src/outputC.txt");
        //processTest("src/inputD.txt", "src/outputD.txt");
        //processTest("src/inputE.txt", "src/outputE.txt");
        //processTest("src/inputF.txt", "src/outputF.txt");
    }
}
