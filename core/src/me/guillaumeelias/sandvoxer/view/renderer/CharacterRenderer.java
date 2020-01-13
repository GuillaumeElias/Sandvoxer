package me.guillaumeelias.sandvoxer.view.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import me.guillaumeelias.sandvoxer.model.Character;
import me.guillaumeelias.sandvoxer.model.CharacterManager;
import me.guillaumeelias.sandvoxer.model.Voxel;

public class CharacterRenderer {

    public static final CharacterRenderer instance = new CharacterRenderer();

    private static final float CHARACTER_VISIBLE_RADIUS = Voxel.CUBE_SIZE * 2;


    private CharacterManager characterManager;
    private Environment environment;

    public void initialize(Environment environment){
        this.environment = environment;
    }

    public void setCharacterManager(CharacterManager characterManager) {
        this.characterManager = characterManager;
    }

    public void render(ModelBatch modelBatch, Camera cam){
        for(Character character : characterManager.getCharacterList()){
            if(isCharacterVisible(character, cam)){
                modelBatch.render(character.getModelInstance(), environment);
            }
        }
    }

    private boolean isCharacterVisible(Character character, Camera cam){
        return cam.frustum.sphereInFrustum(character.getPosition(), CHARACTER_VISIBLE_RADIUS);
    }
}
