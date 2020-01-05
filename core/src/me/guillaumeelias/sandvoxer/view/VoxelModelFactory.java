package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import me.guillaumeelias.sandvoxer.model.Voxel;

import java.util.HashMap;
import java.util.Map;

public class VoxelModelFactory {

    private static Map<VoxelType, Model> existingTypes = new HashMap<>();
    private static ModelBuilder modelBuilder = new ModelBuilder();

    public static Model buildModelType(VoxelType voxelType, Material material){

        if(existingTypes.containsKey(voxelType)){
            return existingTypes.get(voxelType);
        }

        Model model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        existingTypes.put(VoxelType.GRASS, model);

        return model;
    }

    public static void disposeAll() {
        for(Map.Entry<VoxelType, Model> entry : existingTypes.entrySet()){
            entry.getValue().dispose();
        }
    }
}
