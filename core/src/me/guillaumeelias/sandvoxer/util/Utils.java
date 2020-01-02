package me.guillaumeelias.sandvoxer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Utils {

    public static Material randomColorMaterial(){
        return new Material(ColorAttribute.createDiffuse(new Color(MathUtils.random(0, 1), MathUtils.random(0, 1), MathUtils.random(0, 1), 0)));
    }

    public static Vector2 getWorldMousePosition(Camera camera){
        Vector3 mouseInWorld3D = new Vector3();
        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        camera.unproject(mouseInWorld3D);

        Vector2 mouseInWorld2D = new Vector2();
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;

        return mouseInWorld2D;
    }
}
