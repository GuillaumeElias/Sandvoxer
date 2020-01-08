package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.view.VoxelModelFactory;
import me.guillaumeelias.sandvoxer.view.VoxelType;

public class Item {

    public static final float ITEM_SIDE_LENGTH = 1;
    public static final float ROTATION_SPEED = 120f;

    private Vector3 position;

    private BoundingBox boundingBox;

    private ModelInstance modelInstance;

    private VoxelType yieldedVoxelType;


    public Item(Vector3 position, VoxelType yieldedVoxelType){

        this.position = position;
        this.yieldedVoxelType = yieldedVoxelType;

        this.boundingBox = new BoundingBox(this.position, this.position.cpy().add(ITEM_SIDE_LENGTH,ITEM_SIDE_LENGTH,ITEM_SIDE_LENGTH));

        //TODO put that in ItemView
        this.modelInstance = new ModelInstance(VoxelModelFactory.buildModelForItem(yieldedVoxelType));
        modelInstance.transform.translate(position.x, position.y, position.z);
        modelInstance.transform.rotate(Vector3.Z, 30);
        modelInstance.transform.rotate(Vector3.Y, 30);
        modelInstance.transform.rotate(Vector3.X, 30);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void rotate(float deltaTime) {
        modelInstance.transform.rotate(Vector3.Y, deltaTime * ROTATION_SPEED);
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public VoxelType getYieldedVoxelType() {
        return yieldedVoxelType;
    }
}
