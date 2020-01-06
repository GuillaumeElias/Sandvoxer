package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

public class CharacterModelFactory {

    private static final UBJsonReader jsonReader = new UBJsonReader();
    private static final G3dModelLoader loader = new G3dModelLoader(jsonReader);


    public static Model buildCharacterModel(String modelPath){
        return loader.loadModel(Gdx.files.internal(modelPath));
    }

    public static ModelInstance buildCharacterModelInstance(CharacterType type, Vector3 position){
        ModelInstance modelInstance = new ModelInstance(type.getModel());

        modelInstance.transform.setFromEulerAngles(type.getRotation(),0,0).trn(position);

        for(Animation a : modelInstance.animations){
            Gdx.app.log("animation",""+a.id);
        }

        return modelInstance;
    }

}


