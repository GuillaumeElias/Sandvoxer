package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.view.VoxelType;

public class Voxel {

    public static final int CUBE_SIZE = 10;

    public final int xi;
    public final int yi;
    public final int zi;

    ModelInstance modelInstance;
    VoxelType type;

    BoundingBox boundingBox;

    public Voxel(int xi, int yi, int zi, ModelInstance modelInstance, VoxelType voxelType){
        this.xi = xi;
        this.yi = yi;
        this.zi = zi;

        this.modelInstance = modelInstance;
        this.type = voxelType;

        Vector3 worldPosition = new Vector3(xi * CUBE_SIZE, yi * CUBE_SIZE, zi * CUBE_SIZE);
        this.boundingBox = new BoundingBox(worldPosition, new Vector3(worldPosition).add(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
