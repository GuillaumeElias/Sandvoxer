package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public enum VoxelType {

    GRASS("textures/grass.png", "Grass"),
    WOOD("textures/wood.png", "Wood"),
    SAND("textures/sand.png", "Sand");

    private Sprite sprite;
    private Model model;
    private Texture texture;

    private String name;

    VoxelType(String texturePath, String name){
        this.texture = new Texture(texturePath);
        this.name = name;

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Material material = new Material(TextureAttribute.createDiffuse(texture));

        this.model = VoxelModelFactory.buildModelType(this, material);

        this.sprite = new Sprite(texture);
    }

    public Model getModel() {
        return model;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }
}
