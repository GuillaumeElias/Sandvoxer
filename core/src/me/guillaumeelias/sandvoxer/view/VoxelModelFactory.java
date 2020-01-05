package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import me.guillaumeelias.sandvoxer.model.Item;
import me.guillaumeelias.sandvoxer.model.Voxel;

import java.util.HashMap;
import java.util.Map;

public class VoxelModelFactory {

    private static Map<VoxelType, Model> existingTypes = new HashMap<>();
    private static ModelBuilder modelBuilder = new ModelBuilder();

    public static Model buildModelForVoxel(VoxelType voxelType){

        if(existingTypes.containsKey(voxelType)){
            return existingTypes.get(voxelType);
        }

        Model model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                voxelType.getMaterial(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        existingTypes.put(VoxelType.GRASS, model);

        return model;
    }

    public static Model buildModelForItem(VoxelType voxelType){


        Model model = modelBuilder.createBox(Item.ITEM_SIDE_LENGTH, Item.ITEM_SIDE_LENGTH, Item.ITEM_SIDE_LENGTH,
                voxelType.getMaterial(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);


        return model;
    }

    public static void disposeAll() {
        for(Map.Entry<VoxelType, Model> entry : existingTypes.entrySet()){
            entry.getValue().dispose();
        }
    }
}
