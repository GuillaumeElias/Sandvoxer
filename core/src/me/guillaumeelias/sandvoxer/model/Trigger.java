package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.Gdx;
import me.guillaumeelias.sandvoxer.view.DialogRenderer;

public class Trigger {

    private int id;
    private boolean triggered;

    private Dialog dialog;
    private Dialog dialogIfAlreadyTriggered;

    public Trigger(Dialog dialog, Dialog dialogIfAlreadyTriggered, int triggerId){
        this.dialog = dialog;
        this.dialogIfAlreadyTriggered = dialogIfAlreadyTriggered;
        this.id = triggerId;

        triggered = false;
    }

    public void startTrigger(){

        Gdx.app.log("Trigger", "startTrigger id:"+id);

        if(triggered){

            switch (id){
                case 1:

                    //TODO make character face player

                    //TODO check if player picked up the sand

                    DialogRenderer.instance.setCurrentDialog(dialog);

                    break;
                default:
                    DialogRenderer.instance.setCurrentDialog(dialog);
            }

        }else{
            DialogRenderer.instance.setCurrentDialog(dialogIfAlreadyTriggered);
        }
    }
}
