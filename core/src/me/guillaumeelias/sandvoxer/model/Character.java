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

        int xi = Math.round(position.x / Voxel.CUBE_SIZE );
        int yi = Math.round(position.y / Voxel.CUBE_SIZE );
        int zi = Math.round(position.z / Voxel.CUBE_SIZE );

        //create bounding box slightly smaller than a voxel
        this.boundingBox = new BoundingBox(new Vector3(xi * Voxel.CUBE_SIZE + 1,yi* Voxel.CUBE_SIZE + 1,zi* Voxel.CUBE_SIZE + 1),
                new Vector3(xi * Voxel.CUBE_SIZE + Voxel.CUBE_SIZE - 1,yi* Voxel.CUBE_SIZE + Voxel.CUBE_SIZE - 1,zi* Voxel.CUBE_SIZE + Voxel.CUBE_SIZE - 1));
    }


    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Vector3 getPosition() {
        return position;
    }
}
