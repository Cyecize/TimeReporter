package com.cyecize.reporter.display.models;

public class WindowPreferences {
    private int windowWidth;

    private int windowHeight;

    private int extendedState;

    public WindowPreferences() {

    }

    public int getWindowWidth() {
        return this.windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return this.windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public int getExtendedState() {
        return this.extendedState;
    }

    public void setExtendedState(int extendedState) {
        this.extendedState = extendedState;
    }
}
