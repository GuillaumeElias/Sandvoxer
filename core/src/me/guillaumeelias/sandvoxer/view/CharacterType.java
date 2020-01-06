package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.g3d.Model;

public enum CharacterType {
    CHICKEN("models/chicken-rigged.g3db",
            "chicken-rig|chicken-rig|chicken-rig|idle|chicken-rig|idle", 30, 2),
    WOLF("models/wolf.g3db", "Armature|Beta Idle", 200, 1);

    private Model model;
    private String defaultAnimationId;
    private int rotation;
    private int voxelMargins;

    CharacterType(String modelPath, String defaultAnimationId, int rotation, int voxelMargins){
        model = CharacterModelFactory.buildCharacterModel(modelPath);
        this.defaultAnimationId = defaultAnimationId;
        this.rotation = rotation;
        this.voxelMargins = voxelMargins;
    }

    public Model getModel() {
        return model;
    }

    public String getDefaultAnimationId() {
        return defaultAnimationId;
    }

    public int getRotation() {
        return rotation;
    }

    public int getVoxelMargins() {
        return voxelMargins;
    }
}
