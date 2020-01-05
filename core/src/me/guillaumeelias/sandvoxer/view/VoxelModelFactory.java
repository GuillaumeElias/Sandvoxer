package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import me.guillaumeelias.sandvoxer.model.Voxel;

import java.util.HashMap;
import java.util.Map;

public class VoxelModelFactory {

    private static Texture GRASS_TEXTURE = new Texture("textures/grass.png");
    private static Texture WOOD_TEXTURE = new Texture("textures/wood.png");
    private static Texture SAND_TEXTURE = new Texture("textures/sand.png");

    static {
        GRASS_TEXTURE.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        GRASS_TEXTURE.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        WOOD_TEXTURE.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        WOOD_TEXTURE.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        SAND_TEXTURE.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        SAND_TEXTURE.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private static Material GRASS_MATERIAL = new Material(TextureAttribute.createDiffuse(GRASS_TEXTURE));
    private static Material WOOD_MATERIAL = new Material(TextureAttribute.createDiffuse(WOOD_TEXTURE));
    private static Material SAND_MATERIAL = new Material(TextureAttribute.createDiffuse(SAND_TEXTURE));

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
                break;
            case WOOD:
                model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                        WOOD_MATERIAL, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
                existingTypes.put(VoxelType.WOOD, model);
                break;

            case SAND:
                model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                        SAND_MATERIAL, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
                existingTypes.put(VoxelType.SAND, model);
                break;

            case RANDOM_COLOR:

                Material randomColorMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));
                model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                        randomColorMaterial, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
                existingTypes.put(VoxelType.RANDOM_COLOR, model);


                /*Material randomColorMaterial = Utils.randomColorMaterial();
                model = modelBuilder.createBox(Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,
                        randomColorMaterial, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);*/
        }

        return model;
    }

    public void disposeAll() {
        for(Map.Entry<VoxelType, Model> entry : existingTypes.entrySet()){
            entry.getValue().dispose();
        }
    }
}
