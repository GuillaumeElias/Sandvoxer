/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.model.trigger;

public abstract class Trigger {

    protected int id;
    protected boolean enabled;

    protected abstract void doStartTrigger();

    protected Trigger(int triggerId){
        this.id = triggerId;

        enabled = true;
    }
    public void startTrigger(){
        if(enabled == false) return;

        doStartTrigger();

        enabled = false;
    }

    public void reenable() {
        enabled = true;
    }

}
