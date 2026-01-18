package de.thws.Adapters.web_in.dto;

public class TeamUpdateRequest {
    private String teamName;
    private String description;

    public TeamUpdateRequest() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
