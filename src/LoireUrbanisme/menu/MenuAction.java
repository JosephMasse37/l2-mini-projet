package LoireUrbanisme.menu;

public class MenuAction {

    private boolean menuCancelled = false; // pas cancel

    public void cancel() {
        this.menuCancelled = true;
    }

    public boolean isCancelled() {
        return menuCancelled;
    }
}
