package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import me.guillaumeelias.sandvoxer.model.Character;

import java.util.ArrayList;
import java.util.List;

public class CharacterManager {

    List<Character> characterList;
    List<ModelInstance> modelInstances;

    public CharacterManager(){
        characterList = new ArrayList<>();
        modelInstances = new ArrayList<>();

        createCharacter(new Vector3(1010,5,1020), CharacterType.CHICKEN);
    }

    public void createCharacter(Vector3 position, CharacterType characterType){
        Character character = new Character(position, characterType);

        characterList.add(character);
        modelInstances.add(character.getModelInstance());
    }

    public void updateAnimations(float deltaTime){
        for(Character character : characterList){
            character.getAnimationController().update(deltaTime);
        }
    }

    public List<ModelInstance> getModelInstances() {
        return modelInstances;
    }
}
