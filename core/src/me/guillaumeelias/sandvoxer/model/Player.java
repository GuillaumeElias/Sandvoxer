package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class Player {

    final static Vector3 UP = Vector3.Y;

    public static final int PLAYER_HEIGHT = 2;
    public static final int PLAYER_WIDTH = 7;
    public static final int PLAYER_DEPTH = 7;
    public static final float NEW_BLOCK_REACH = 40f;

    public static final int PLAYER_INIT_XI = 100;
    public static final int PLAYER_INIT_ZI = 100;
    public static final int PLAYER_INIT_YI = 0;

    private static final float MOVE_VELOCITY = 50;
    private static final float GRAVITY_VELOCITY = 0.8f;
    private static final float FALL_MAX_VELOCITY = 3f;
    private static final float JUMP_VELOCITY = 1.2f;
    private static final float ROT_SPEED = 0.2f;
    private static final float UP_CHECK_MARGIN = 5.f;

    private final int Y_DEATH_LIMIT = 30;

    private Vector3 position;
    private boolean inAir;

    private World world;
    private PlayerHUD playerHUD;

    private Camera cam;

    private float yVelocity;

    private Vector3 _tmp;
    private Vector3 _oldPosition;

    private int _mouseX;
    private int _mouseY;

    public Player(World world, Camera camera, PlayerHUD playerHUD) {
        this.playerHUD = playerHUD;
        this.world = world;

        this._tmp = new Vector3();
        this._oldPosition = new Vector3();

        this.cam = camera;
        birth();
    }

    public void birth(){
        this.position = new Vector3(PLAYER_INIT_XI * Voxel.CUBE_SIZE, PLAYER_INIT_YI + PLAYER_HEIGHT + 20, PLAYER_INIT_ZI * Voxel.CUBE_SIZE);
        this.cam.lookAt(10, PLAYER_HEIGHT, 10);

        yVelocity = 0;
    }

    public void gravity(float deltaTime) {

        if (yVelocity > 0) {

            if(world.checkBoxCollision(position.x, position.y + yVelocity + UP_CHECK_MARGIN, position.z, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_DEPTH)){
                yVelocity = 0;
                //TODO set to minus something if bouncy material
            }

            yVelocity -= GRAVITY_VELOCITY * deltaTime;
        }

        _oldPosition.set(position);

        _tmp.set(UP).nor().scl(yVelocity);
        position.add(_tmp);

        //if(world.isGround((int)position.x, (int)(position.y - PLAYER_HEIGHT), (int)position.z)){
        if (checkCollision()) {
            position.set(_oldPosition);

            inAir = false;
            //TODO if bouncy material => yVelocity++;

        } else {
            inAir = true;
            if (yVelocity == 0) {
                yVelocity = -GRAVITY_VELOCITY * deltaTime;
            } else if (yVelocity > -FALL_MAX_VELOCITY) {
                yVelocity -= GRAVITY_VELOCITY * deltaTime;
            }
        }

        checkItemsCollision();
    }



    public void mouseMoved(int screenX, int screenY){
        int magX = (int) Math.abs(_mouseX - screenX);
        int magY = (int) Math.abs(_mouseY - screenY);

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
    }

    public void moveForward(float deltaTime) {


        double angleRad = getCameraAngleRad();
        _tmp.set((float) Math.sin(angleRad), 0, -(float) Math.cos(angleRad)).nor().scl(deltaTime * MOVE_VELOCITY);

        //APPLY X
        _oldPosition.set(position);

        position.sub(_tmp.x,0,0);
        if(checkCollision()){
            position.set(_oldPosition);
        }

        //APPLY Y
        _oldPosition.set(position);

        position.sub(0,0,_tmp.z);
        if(checkCollision()){
            position.set(_oldPosition);
        }

        checkItemsCollision();

        //if (world.isGround((int) position.x, (int) (position.y - PLAYER_HEIGHT), (int) position.z)) {
        //if(checkCollision()){
            //position.set(_oldPosition);
        //}
    }

    public void moveBackward(float deltaTime){
        double angleRad = getCameraAngleRad();
        _tmp.set((float)Math.sin(angleRad), 0, -(float)Math.cos(angleRad)).nor().scl(deltaTime * MOVE_VELOCITY);

        //APPLY X
        _oldPosition.set(position);

        position.add(_tmp.x,0,0);
        if(checkCollision()){
            position.set(_oldPosition);
        }

        //APPLY Y
        _oldPosition.set(position);

        position.add(0,0,_tmp.z);
        if(checkCollision()){
            position.set(_oldPosition);
        }

        checkItemsCollision();
    }

    public void jump(float deltaTime) {
        if(!inAir){
            yVelocity = JUMP_VELOCITY;
        }
    }

    public void strafeLeft(float deltaTime){
        _tmp.set(cam.direction).crs(UP).nor().scl(-deltaTime * MOVE_VELOCITY);

        _oldPosition.set(position);

        position.add(_tmp);

        if(checkCollision()){
            position.set(_oldPosition);
        }

        checkItemsCollision();
    }

    public void strafeRight(float deltaTime){
        _tmp.set(cam.direction).crs(UP).nor().scl(deltaTime * MOVE_VELOCITY);
        _oldPosition.set(position);

        position.add(_tmp);

        if(checkCollision()){
            position.set(_oldPosition);
        }

        checkItemsCollision();
    }

    public boolean isDead(){
        int yi = Math.round(getY() / Voxel.CUBE_SIZE );
        if( yi < -Y_DEATH_LIMIT ){
            return true;
        }
        return false;
    }

    private boolean checkCollision(){
        return world.checkBoxCollision(position.x, position.y, position.z, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_DEPTH);
    }

    private void checkItemsCollision(){
        Item item = world.checkItemCollision(position.x, position.y, position.z, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_DEPTH);
        if(item != null){
            //TODO show dialog
            playerHUD.addVoxelType(item.getYieldedVoxelType());
            world.removeItem(item);
        }
    }

    public double getCameraAngleRad(){
        return -(float)Math.atan2(cam.direction.x, cam.direction.z);
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    public void clearMouse() {
        _mouseX = 0;
        _mouseY = 0;
    }

    public PlayerHUD getPlayerHUD() {
        return playerHUD;
    }
}
