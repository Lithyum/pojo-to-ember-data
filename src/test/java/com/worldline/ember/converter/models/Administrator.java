package com.worldline.ember.converter.models;

public class Administrator extends User {

    private Boolean canDoThis;

    private Boolean canDoThat;

    public Boolean getCanDoThis() {
        return canDoThis;
    }

    public void setCanDoThis(Boolean canDoThis) {
        this.canDoThis = canDoThis;
    }

    public Boolean getCanDoThat() {
        return canDoThat;
    }

    public void setCanDoThat(Boolean canDoThat) {
        this.canDoThat = canDoThat;
    }
}
