package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class Player {

    final static Vector3 UP = Vector3.Y;

    public static final int PLAYER_HEIGHT = 2;
    public static final int PLAYER_WIDTH = 7;
    public static final int PLAYER_DEPTH = 7;
    public static final float NEW_BLOCK_REACH = 40f;

    private final float MOVE_VELOCITY = 50;
    private final float GRAVITY_VELOCITY = 0.8f;
    private final float FALL_MAX_VELOCITY = 3f;
    private final float JUMP_VELOCITY = 1.2f;
    private final float ROT_SPEED = 0.2f;

    private Vector3 position;
    private boolean inAir;

    private World world;

    private Camera cam;

    private float yVelocity;

    private Vector3 _tmp;
    private Vector3 _oldPosition;

    private int _mouseX;
    private int _mouseY;

    public Player(World world, Camera camera) {
        this.position = new Vector3(100 * Voxel.CUBE_SIZE, PLAYER_HEIGHT + 20, 100 * Voxel.CUBE_SIZE);

        this.world = world;

        this._tmp = new Vector3();
        this._oldPosition = new Vector3();

        this.cam = camera;
        this.cam.lookAt(10, PLAYER_HEIGHT, 10);

        yVelocity = 0;
    }

    public void gravity(float deltaTime) {

        if (yVelocity > 0) {
            if (yVelocity > -FALL_MAX_VELOCITY) {
                yVelocity -= GRAVITY_VELOCITY * deltaTime;
            }
        }

        _oldPosition.set(position);

        _tmp.set(UP).nor().scl(yVelocity);
        position.add(_tmp);

        //if(world.isGround((int)position.x, (int)(position.y - PLAYER_HEIGHT), (int)position.z)){
        if (checkCollision()) {
            position.set(_oldPosition);

            inAir = false;
            //TODO if bounce yVelocity++;

        } else {
            inAir = true;
            if (yVelocity == 0) {
                yVelocity = -GRAVITY_VELOCITY * deltaTime;
            } else if (yVelocity > -FALL_MAX_VELOCITY) {
                yVelocity -= GRAVITY_VELOCITY * deltaTime;
            }
        }
    }



    public void mouseMoved(int screenX, int screenY){
        int magX = (int) Math.abs(_mouseX - screenX);
        int magY = (int) Math.abs(_mouseY - screenY);

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
    }

    public void strafeRight(float deltaTime){
        _tmp.set(cam.direction).crs(UP).nor().scl(deltaTime * MOVE_VELOCITY);
        _oldPosition.set(position);

        position.add(_tmp);

        if(checkCollision()){
            position.set(_oldPosition);
        }
    }

    private boolean checkCollision(){
        return world.checkBoxCollision(position.x, position.y, position.z, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_DEPTH);
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

}
