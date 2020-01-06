package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.g3d.Model;

public enum CharacterType {
    CHICKEN("models/chicken.g3db", "Armature|Idle"),
    WOLF("models/wolf.g3db", "Armature|Beta Idle");

    private Model model;
    private String defaultAnimationId;
    private int boundingBoxSize;

    CharacterType(String modelPath, String defaultAnimationId){
        model = CharacterModelFactory.buildCharacterModel(modelPath);
        this.defaultAnimationId = defaultAnimationId;
    }

    public Model getModel() {
        return model;
    }

    public String getDefaultAnimationId() {
        return defaultAnimationId;
    }
}
