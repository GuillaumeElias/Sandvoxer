package me.guillaumeelias.sandvoxer.view;

import me.guillaumeelias.sandvoxer.model.Dialog;

public class DialogRenderer{

    public static final DialogRenderer instance = new DialogRenderer();

    private Dialog currentDialog;

    private DialogRenderer(){
        currentDialog = null;
    }

    public void initialize(){

    }

    public void setCurrentDialog(Dialog currentDialog) {
        this.currentDialog = currentDialog;
    }

    public void render(){

    }

}
