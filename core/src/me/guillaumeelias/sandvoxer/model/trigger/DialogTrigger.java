/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.model.trigger;

import me.guillaumeelias.sandvoxer.model.Dialog;
import me.guillaumeelias.sandvoxer.model.Player;
import me.guillaumeelias.sandvoxer.view.renderer.DialogRenderer;

public class DialogTrigger extends Trigger {

    private Dialog dialog;
    private Dialog dialogIfAlreadyTriggered;

    public DialogTrigger(Dialog dialog, Dialog dialogIfAlreadyTriggered, int triggerId){
        super(triggerId);

        this.dialog = dialog;
        this.dialogIfAlreadyTriggered = dialogIfAlreadyTriggered;
    }

    @Override
    protected void doStartTrigger() {
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
    }
}
