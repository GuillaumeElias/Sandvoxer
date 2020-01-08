package me.guillaumeelias.sandvoxer.model.trigger;

import com.badlogic.gdx.Gdx;
import me.guillaumeelias.sandvoxer.model.Trigger;
import me.guillaumeelias.sandvoxer.view.screen.GameScreen;

public class EndLevelTrigger extends Trigger {

    public EndLevelTrigger(int triggerId) {
        super(triggerId);
    }

    @Override
    protected void doStartTrigger() {
        switch (id){

            case 3:
                Gdx.app.log("EndLevelTrigger", "endLevel");
                GameScreen.getInstance().startNextLevel();

        }
    }
}
