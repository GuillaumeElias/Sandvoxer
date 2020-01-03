package me.guillaumeelias.sandvoxer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntIntMap;
import me.guillaumeelias.sandvoxer.model.Player;
import me.guillaumeelias.sandvoxer.model.World;

public class InputManager extends InputAdapter {

    private final Camera camera;

    private final Player player;
    private final World world;

    private final IntIntMap keys = new IntIntMap();

    public InputManager (Camera camera, Player player, World world) {
        this.camera = camera;
        this.player = player;
        this.world = world;
    }

    @Override
    public boolean keyDown (int keycode) {
        keys.put(keycode, keycode);
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {
        keys.remove(keycode, 0);
        return true;
    }

    /*@Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
        float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;
        camera.direction.rotate(camera.up, deltaX);
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(tmp, deltaY);
        return true;
    }*/

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Ray ray = camera.getPickRay(/*screenX +*/  Gdx.graphics.getWidth() / 2, /*screenY +*/  Gdx.graphics.getHeight() / 2 );

        //TODO for android pick relative to mouse X and Y

        Vector3 rayFrom = new Vector3();
        Vector3 rayTo = new Vector3();

        rayFrom.set(ray.origin);
        rayTo.set(camera.direction);
        rayTo.nor();

        if(button == 1){
            world.onRightClickBlock(rayFrom, rayTo);
        }else {
            world.onClickBlock(rayFrom, rayTo);
        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        player.mouseMoved(screenX, screenY);

        //refreshCamera();

        return true;
    }

    public void refreshCamera(){
        camera.position.set(player.getPosition());

        update(Gdx.graphics.getDeltaTime());
    }

    public void update (float deltaTime) {
        if (keys.containsKey(Keys.W) || keys.containsKey(Keys.Z)) {
            player.moveForward(deltaTime);
        }
        if (keys.containsKey(Keys.S) ) {
            player.moveBackward(deltaTime);
        }
        if (keys.containsKey(Keys.A) || keys.containsKey(Keys.Q)) {
            player.strafeLeft(deltaTime);
        }
        if (keys.containsKey(Keys.D) || keys.containsKey(Keys.Q)) {
            player.strafeRight(deltaTime);
        }
        if (keys.containsKey(Keys.SPACE)) {
            player.jump(deltaTime);
        }

        camera.update(true);
    }
}
