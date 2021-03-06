/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.model.trigger;

import com.badlogic.gdx.Gdx;
import me.guillaumeelias.sandvoxer.model.Dialog;
import me.guillaumeelias.sandvoxer.view.renderer.DialogRenderer;
import me.guillaumeelias.sandvoxer.view.screen.GameScreen;

public class EndLevelTrigger extends Trigger {

    boolean fired;

    public EndLevelTrigger(int triggerId) {
        super(triggerId);
        fired = false;
    }

    @Override
    protected void doStartTrigger() {

        if(fired)return;

        fired = true;

        Gdx.app.log("EndLevelTrigger", "endLevel");

        switch (id){

            case 3:
                GameScreen.getInstance().startNextLevel();
                break;
            case 4:
                DialogRenderer.instance.setCurrentDialog(Dialog.END_GAME_DIALOG);
        }
    }
}
