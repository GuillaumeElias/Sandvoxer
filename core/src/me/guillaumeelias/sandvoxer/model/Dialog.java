/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

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
            "How about a little bouncier?",
            "Scroll down or press the number 2 to select another material",
            "Then try to reach the blue platform up there"
    },200),
    WOLF_DIALOG_NO_BOUNCY(new String[]{
            "You missed an item behind you",
            "Look around this platform"
    },200),
    WOLF_DIALOG_REPEAT(new String[]{
        "Yeah",
        "Bounce away baby",
        "Scroll or press 2 and reach the blue platform"
    },200),
    BEAR_DIALOG_1(new String[]{
            "Yo",
            "This has been cool and all",
            "But now you have a limited amount of blocks",
            "That'll teach you how to be economic!"
    },200),
    BEAR_DIALOG_REPEAT(new String[]{
            "As I said",
            "Be economic",
            "You can right click on a block to pick it back up"
    },200),
    JOEYTHESHEEP_DIALOG_1(new String[]{
            "Yorg",
            "Not easy huh?",
            "Ha ha ha ha ha ha !",
            "The next platform in on your left."
    },200),
    JOEYTHESHEEP_DIALOG_REPEAT(new String[]{
            "Hu hu hu hu!"
    },200),
    TIKI_DIALOG_1(new String[]{
            "Hello !",
            "This last one is going to be hard",
            "Make sure you have enough materials before jumping into the blue",
    },200),
    TIKI_DIALOG_REPEAT(new String[]{
            "Aim for the blue platform"
    },200),
    END_GAME_DIALOG(new String[]{
            "This is the end of this game",
            "I hope you sort of enjoyed it",
            "If so, check out my other games at timmyotoole.itch.io"
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
