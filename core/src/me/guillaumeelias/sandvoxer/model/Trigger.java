package me.guillaumeelias.sandvoxer.model;

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
