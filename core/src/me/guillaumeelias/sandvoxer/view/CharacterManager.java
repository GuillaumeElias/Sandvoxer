package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
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
        createCharacter(new Vector3(1400,185,1400), CharacterType.WOLF);
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

    public List<Character> getCharacterList() {
        return characterList;
    }

    public Character checkCharacterCollision(BoundingBox boundingBox){

        for(Character c : characterList){
            if(boundingBox.intersects(c.getBoundingBox())){
                return c;
            }
        }

        return null;
    }
}