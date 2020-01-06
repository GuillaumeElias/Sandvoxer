package me.guillaumeelias.sandvoxer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Dialog {

    CHICKEN_DIALOG_NO_SAND(new String[]{
            "Hello",
            "You forgot to pick up the sand behind you, I think."}),
    CHICKEN_DIALOG_1(new String[]{
            "Hello",
            "I'm just a chicken",
            "But I can tell you the meaning of this",
            "You see the platform behing me?",
            "Just jump your way over there."
    }),
    CHICKEN_DIALOG_REPEAT(new String[]{
            "Yeah",
            "The platform is just behind me"
    }),
    WOLF_DIALOG_1(new String[]{
            "",
            ""});



    private List<String> lines;

    Dialog(String [] linesArray){
        lines = new ArrayList<String>();
        Collections.addAll(lines, linesArray);
    }

    String getLine(int lineNumber){
        return lines.get(lineNumber);
    }

    int getNumberOfLines(){
        return lines.size();
    }
}
