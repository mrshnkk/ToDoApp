package de.thws.Domain.DomainModels;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class Team {
    private final Long teamId;
    private String teamName;
    private String description;
    private List<TeamMember> teamMembers = new ArrayList<>();
    private final LocalDateTime createdAt;
    private final User owner;
    public Team(Long teamId, String teamName, String description, ArrayList<TeamMember> teamMembers, LocalDateTime createdAt, User owner){
        this.teamId = teamId;
        this.teamName = teamName;
        this.description = description;
        this.teamMembers = teamMembers;
        this.createdAt = createdAt;
        this.owner = owner;
        this.teamMembers.add(new TeamMember(teamId, owner, TeamRole.OWNER, createdAt));
    }

    public Long getTeamId() {
        return teamId;}
    public void createTeam() {}
    public void updateTeam(String teamName, String description) {
        this.teamName = teamName;
        this.description = description;}

//TODO deleteTeam
    public void deleteTeam() {

    }
    public void addMember(User user, TeamRole teamRole) {
        teamMembers.add(new TeamMember(teamId, user, teamRole, LocalDateTime.now()));
    }
    public void removeMember(User user) {
        for (int i = 0; i < teamMembers.size(); i++) {
            TeamMember tm = teamMembers.get(i);
            if (tm.getUser().getUserId().equals(user.getUserId())) {
                teamMembers.remove(i);
                break;
            }
        }
    }
    private TeamMember findMemberById(Long memberId){
        for (int i = 0; i < teamMembers.size(); i++) {
            TeamMember tm = teamMembers.get(i);
            if (tm.getUser().getUserId().equals(memberId)) {
                return tm;
            }
        }
        return null;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }
    public void assignProject(Long memberId, Project project) {
        TeamMember member = findMemberById(memberId);
        if (member == null) {
            return;
        }
        if (!member.getRole().canManageProjects()) {
            return;
        }
       // projects.add(project);
    }
//    public List<Project> getTeamProjects() {
//     //return projects;
//
//    }
}
