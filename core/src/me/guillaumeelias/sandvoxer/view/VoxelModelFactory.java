package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import me.guillaumeelias.sandvoxer.util.Utils;
import me.guillaumeelias.sandvoxer.model.Voxel;

import java.util.HashMap;
import java.util.Map;

public class VoxelModelFactory {

    private static Texture GRASS_TEXTURE = new Texture("grass.png");
    static { //TODO create
        GRASS_TEXTURE.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        GRASS_TEXTURE.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private static Material GRASS_MATERIAL = new Material(TextureAttribute.createDiffuse(GRASS_TEXTURE));


    private Map<VoxelType, Model> existingTypes;

    private ModelBuilder modelBuilder = new ModelBuilder();

    public VoxelModelFactory(){
        modelBuilder = new ModelBuilder();
        existingTypes = new HashMap<>();
    }

    public Model buildModelType(VoxelType voxelType){

        if(existingTypes.containsKey(voxelType)){
            return existingTypes.get(voxelType);
        }

        Model model = null;

        switch (voxelType){
            case GRASS:
                model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                        GRASS_MATERIAL, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
                existingTypes.put(VoxelType.GRASS, model);
            case RANDOM_COLOR:
                Material randomColorMaterial = Utils.randomColorMaterial();
                model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                        randomColorMaterial, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        }

        return model;
    }

    public void disposeAll() {
        for(Map.Entry<VoxelType, Model> entry : existingTypes.entrySet()){
            entry.getValue().dispose();
        }
    }
}
