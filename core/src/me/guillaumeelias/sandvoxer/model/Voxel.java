package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import me.guillaumeelias.sandvoxer.view.VoxelType;

public class Voxel {

    public static final int CUBE_SIZE = 10;

    public final int xi;
    public final int yi;
    public final int zi;

    ModelInstance modelInstance;
    VoxelType type;

    public Voxel(int xi, int yi, int zi, ModelInstance modelInstance, VoxelType voxelType){
        this.xi = xi;
        this.yi = yi;
        this.zi = zi;

        this.modelInstance = modelInstance;
        this.type = voxelType;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }
}
