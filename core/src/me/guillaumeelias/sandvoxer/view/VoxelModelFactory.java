/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import me.guillaumeelias.sandvoxer.model.Item;
import me.guillaumeelias.sandvoxer.model.Voxel;

import java.util.HashMap;
import java.util.Map;

public class VoxelModelFactory {

    private static Map<VoxelType, Model> voxelModels = new HashMap<>();
    private static Map<VoxelType, Model> itemsModels = new HashMap<>();
    private static ModelBuilder modelBuilder = new ModelBuilder();

    public static Model buildModelForVoxel(VoxelType voxelType){

        if(voxelModels.containsKey(voxelType)){
            return voxelModels.get(voxelType);
        }

        Model model = createBox(voxelType.getMaterial(), Voxel.CUBE_SIZE);

        voxelModels.put(voxelType, model);

        return model;
    }

    public static Model buildModelForItem(VoxelType voxelType){

        if(itemsModels.containsKey(voxelType)){
            return itemsModels.get(voxelType);
        }

        Model model = createBox(voxelType.getMaterial(), Item.ITEM_SIDE_LENGTH);
        itemsModels.put(voxelType, model);

        return model;
    }

    public static Model createBox(Material material, float sideLength){
        return modelBuilder.createBox(sideLength, sideLength, sideLength,
                material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }

    public static void disposeAll() {
        for(Map.Entry<VoxelType, Model> entry : voxelModels.entrySet()){
            entry.getValue().dispose();
        }
        for(Map.Entry<VoxelType, Model> entry : itemsModels.entrySet()){
            entry.getValue().dispose();
        }
    }
}
