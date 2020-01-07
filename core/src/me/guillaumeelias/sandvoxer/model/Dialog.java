package me.guillaumeelias.sandvoxer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Dialog {

    CHICKEN_DIALOG_NO_SAND(new String[]{
            "Hello",
            "You forgot to pick up the sand behind you, I think."},200),
    CHICKEN_DIALOG_1(new String[]{
            "Hello",
            "I'm just a chicken",
            "But I can tell you the meaning of this",
            "You see the platform behind me?",
            "Just jump your way over there."
    },200),
    CHICKEN_DIALOG_REPEAT(new String[]{
            "Yes",
            "The platform is just behind me"
    },200),
    WOLF_DIALOG_1(new String[]{
            "You seem like a nice person",
            "I like your style",
            "How about a little more bouncy?"
    },200),
    WOLF_DIALOG_REPEAT(new String[]{
        "Yeah",
        "Bounce away baby"
    },200);


    private List<String> lines;
    private int bottomMargin;
    private boolean played;

    Dialog(String [] linesArray, int bottomMargin){
        this.bottomMargin = bottomMargin;
        lines = new ArrayList<String>();
        Collections.addAll(lines, linesArray);
        played = false;
    }

    public String getLine(int lineNumber){
        return lines.get(lineNumber);
    }

    public int getNumberOfLines(){
        return lines.size();
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public boolean wasPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }
}
