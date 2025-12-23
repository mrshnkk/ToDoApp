package de.thws.Domain.DomainModels;

public enum TeamRole {
    OWNER(true, true, true),
    MEMBER(false, true, false),
    VIEWER(false, false, false);

    final boolean canManageRoles;
    final boolean canManageProjects;
    final boolean canEditTeam;

    TeamRole(boolean canManageRoles, boolean canManageProjects, boolean canEditTeam){
        this.canManageRoles = canManageRoles;
        this.canManageProjects = canManageProjects;
        this.canEditTeam = canEditTeam;
    }

    public boolean canManageRoles() {
        return canManageRoles;
    }
    public boolean canManageProjects() {
        return canManageProjects;
    }
    public boolean canEditTeam() {
        return canEditTeam;
    }

}
