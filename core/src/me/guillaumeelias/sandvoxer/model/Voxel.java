package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.model.trigger.Trigger;
import me.guillaumeelias.sandvoxer.view.VoxelType;

public class Voxel {

    public static final int CUBE_SIZE = 10;

    public final int xi;
    public final int yi;
    public final int zi;

    ModelInstance modelInstance;
    VoxelType type;

    BoundingBox boundingBox;

    me.guillaumeelias.sandvoxer.model.trigger.Trigger trigger;

    public Voxel(int xi, int yi, int zi, VoxelType voxelType){
        this.xi = xi;
        this.yi = yi;
        this.zi = zi;

        this.type = voxelType;

        Vector3 worldPosition = new Vector3(xi * CUBE_SIZE, yi * CUBE_SIZE, zi * CUBE_SIZE);
        this.boundingBox = new BoundingBox(worldPosition, new Vector3(worldPosition).add(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));

        modelInstance = new ModelInstance(voxelType.getModel());
        modelInstance.transform.translate(xi * Voxel.CUBE_SIZE,yi * Voxel.CUBE_SIZE,zi * Voxel.CUBE_SIZE);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setTrigger(me.guillaumeelias.sandvoxer.model.trigger.Trigger trigger) {
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public VoxelType getType() {
        return type;
    }
}
