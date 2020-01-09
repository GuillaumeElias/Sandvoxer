package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public enum VoxelType {

    GRASS("textures/grass.png", "Grass"),
    GRASS_2("textures/grass2.png", "Grass2"),
    WOOD("textures/wood.png", "Wood"),
    BLUE_STUFF("textures/blue.png", "BlueStuff"),
    BOUNCY_STUFF("textures/bouncy.png", "Bouncy"),
    RED_COLOR("textures/redColor.png", "RedColor"),
    SAND("textures/sand.png", "Sand"),
    DIRT("textures/dirt.png", "Dirt");

    private Model model;
    private Material material;
    private Texture texture;

    private String name;

    VoxelType(String texturePath, String name){
        this.texture = new Texture(texturePath);
        this.name = name;

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.material = new Material(TextureAttribute.createDiffuse(texture));

        this.model = VoxelModelFactory.buildModelForVoxel(this);
    }

    public Model getModel() {
        return model;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
