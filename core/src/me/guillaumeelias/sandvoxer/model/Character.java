package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.view.CharacterType;

public class Character {

    private Vector3 position;
    private BoundingBox boundingBox;

    private AnimationController animationController;
    private ModelInstance modelInstance;

    public Character(Vector3 position, CharacterType type){
        this.position = position;

        modelInstance = new ModelInstance(type.getModel());

        modelInstance.transform.setFromEulerAngles(200,0,0).trn(position);

        //TODO refactor that in CharacterView
        animationController = new AnimationController(modelInstance);
        animationController.setAnimation(type.getDefaultAnimationId(), -1, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animation) {
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
                Gdx.app.log("INFO","Animation Ended");
            }
        });

        float size = type.getBoundingBoxSize();
        this.boundingBox = new BoundingBox(position, position.cpy().add(size,size,size));
    }


    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public AnimationController getAnimationController() {
        return animationController;
    }
}
