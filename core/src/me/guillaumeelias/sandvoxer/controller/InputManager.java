package me.guillaumeelias.sandvoxer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntIntMap;
import me.guillaumeelias.sandvoxer.Sandvoxer;
import me.guillaumeelias.sandvoxer.model.Player;
import me.guillaumeelias.sandvoxer.model.World;
import me.guillaumeelias.sandvoxer.sound.SoundController;
import me.guillaumeelias.sandvoxer.sound.SoundEvent;

public class InputManager extends InputAdapter {

    private static final float ROT_SPEED = 0.2f;

    private final Camera cam;

    private final Player player;
    private final World world;
    private Sandvoxer sandvoxer;

    Quaternion q = new Quaternion();

    private int _mouseX;
    private int _mouseY;

    private final IntIntMap keys = new IntIntMap();

    public InputManager (Camera camera, Player player, World world, Sandvoxer sandvoxer) {
        this.cam = camera;
        this.player = player;
        this.world = world;
        this.sandvoxer = sandvoxer;
    }

    @Override
    public boolean keyDown (int keycode) {
        keys.put(keycode, keycode);

        if(keycode == Keys.NUM_1){
            player.getPlayerHUD().setSelection(0);
        }else if(keycode == Keys.NUM_2){
            player.getPlayerHUD().setSelection(1);
        }

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

        Ray ray = cam.getPickRay(/*screenX +*/  Gdx.graphics.getWidth() / 2, /*screenY +*/  Gdx.graphics.getHeight() / 2 );

        //TODO for android pick relative to mouse X and Y

        Vector3 rayFrom = new Vector3();
        Vector3 rayTo = new Vector3();

        rayFrom.set(ray.origin);
        rayTo.set(cam.direction);
        rayTo.nor();

        if(button == 1){
            world.onRightClickBlock(rayFrom, rayTo);
        }else {
            world.onClickBlock(rayFrom, rayTo);
        }

        return true;
    }


    @Override
    public boolean scrolled(int amount) {

        player.getPlayerHUD().setSelection( player.getPlayerHUD().getSelection() + amount);

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY){
        int magX = Math.abs(_mouseX - screenX);
        int magY = Math.abs(_mouseY - screenY);

        Vector3 oldCamDir = cam.direction.cpy();
        Vector3 oldCamUp = cam.up.cpy();

        if (_mouseX > screenX) {
            cam.rotate(Vector3.Y, 1 * magX * ROT_SPEED);
            cam.update();
        }

        if (_mouseX < screenX) {
            cam.rotate(Vector3.Y, -1 * magX * ROT_SPEED);
            cam.update();
        }

        if (_mouseY < screenY) {
            if (cam.direction.y > -0.965)
                cam.rotate(cam.direction.cpy().crs(Vector3.Y), -1 * magY * ROT_SPEED);
            cam.update();
        }

        if (_mouseY > screenY) {

            if (cam.direction.y < 0.965)
                cam.rotate(cam.direction.cpy().crs(Vector3.Y), 1 * magY * ROT_SPEED);
            cam.update();
        }

        if(cam.up.y < 0){ //if the camera was flipped, revert to old directions //TODO find more elegant solution
            cam.up.set(oldCamUp);
            cam.direction.set(oldCamDir);
            cam.update();
        }

        _mouseX = screenX;
        _mouseY = screenY;

        refreshQ();

        return true;
    }

    public void update (float deltaTime) {

        boolean move = false;

        if (keys.containsKey(Keys.W) || keys.containsKey(Keys.Z)) {
            move = true;
            player.moveForward(deltaTime);
        }
        if (keys.containsKey(Keys.S) ) {
            move = true;
            player.moveBackward(deltaTime);
        }
        if (keys.containsKey(Keys.A) || keys.containsKey(Keys.Q)) {
            player.strafeLeft(deltaTime);
        }
        if (keys.containsKey(Keys.D)) {
            player.strafeRight(deltaTime);
        }
        if (keys.containsKey(Keys.SPACE)) {
            player.jump(deltaTime);
        }
        if(keys.containsKey(Keys.ESCAPE) || keys.containsKey(Keys.MEDIA_PLAY_PAUSE)){
            keys.clear();
            sandvoxer.switchToMenuScreen();
        }

        refreshCameraPosition();

        cam.update(true);

        if(move){
            if(!player.isInAir() && !SoundController.isSoundPlaying(SoundEvent.WALK)){
                SoundController.soundEvent(SoundEvent.WALK);
            }
        }else if(SoundController.isSoundPlaying(SoundEvent.WALK)){
            SoundController.stopSound(SoundEvent.WALK);
        }
    }

    public void refreshQ(){
        cam.view.getRotation( q, true );
        q.conjugate();
    }

    public Quaternion getQ() {
        return q;
    }

    private void refreshCameraPosition() {
        cam.position.set(player.getPosition());
    }

    public void clearMouse() {
        _mouseX = 0;
        _mouseY = 0;
    }
}
