package me.guillaumeelias.sandvoxer.model;

import me.guillaumeelias.sandvoxer.view.DialogRenderer;

public class Trigger {

    private int id;
    private boolean enabled;

    private Dialog dialog;
    private Dialog dialogIfAlreadyTriggered;

    public Trigger(Dialog dialog, Dialog dialogIfAlreadyTriggered, int triggerId){
        this.dialog = dialog;
        this.dialogIfAlreadyTriggered = dialogIfAlreadyTriggered;
        this.id = triggerId;

        enabled = true;
    }

    public void startTrigger(){
        if(enabled == false) return;

        if(dialog.wasPlayed()) {
            DialogRenderer.instance.setCurrentDialog(dialogIfAlreadyTriggered);
        }else{
            switch (id) {
                case 1:
                    if(Player.getInstance().getPlayerHUD().getVoxelTypes().isEmpty()){
                        DialogRenderer.instance.setCurrentDialog(Dialog.CHICKEN_DIALOG_NO_SAND);
                        break;
                    }
                default:
                    DialogRenderer.instance.setCurrentDialog(dialog);
            }
        }

        enabled = false;
    }

    public void reenable() {
        enabled = true;
    }
}
