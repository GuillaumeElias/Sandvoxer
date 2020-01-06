package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.g3d.Model;

public enum CharacterType {
    CHICKEN("models/chicken.g3db", "Armature|Idle", 5);

    private Model model;
    private String defaultAnimationId;
    private int boundingBoxSize;

    CharacterType(String modelPath, String defaultAnimationId, int boundingBoxSize){
        model = CharacterModelFactory.buildCharacterModel(modelPath);
        this.defaultAnimationId = defaultAnimationId;
        this.boundingBoxSize = boundingBoxSize;
    }

    public Model getModel() {
        return model;
    }

    public String getDefaultAnimationId() {
        return defaultAnimationId;
    }

    public int getBoundingBoxSize() {
        return boundingBoxSize;
    }
}
