package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

public class CharacterModelFactory {

    private static final UBJsonReader jsonReader = new UBJsonReader();
    private static final G3dModelLoader loader = new G3dModelLoader(jsonReader);

    public static Model buildCharacterModel(String modelPath){
        return loader.loadModel(Gdx.files.internal(modelPath));
    }

}


