package se.group.backendgruppuppgift.tasker.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class Action {

    private String action;

    protected Action() {

    }

    @JsonCreator
    public Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
