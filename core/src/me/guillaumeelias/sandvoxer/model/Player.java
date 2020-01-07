package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.util.Utils;
import me.guillaumeelias.sandvoxer.view.DialogRenderer;

public class Player {

    private static Player instance; //TODO avoid pseudo-singletons

    final static Vector3 UP = Vector3.Y;

    public static final int PLAYER_HEIGHT = 2;
    public static final int PLAYER_WIDTH = 7;
    public static final int PLAYER_DEPTH = 7;
    public static final float NEW_BLOCK_REACH = 40f;

    public static final int PLAYER_INIT_XI = 91;
    public static final int PLAYER_INIT_ZI = 91;
    public static final int PLAYER_INIT_YI = 0;

    private static final float MOVE_VELOCITY = 50;
    private static final float GRAVITY_VELOCITY = 0.8f;
    private static final float FALL_MAX_VELOCITY = 3f;
    private static final float JUMP_VELOCITY = 1.2f;
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

    private Voxel _lastTouchedVoxel;

    public Player(World world, Camera camera, PlayerHUD playerHUD) {
        this.playerHUD = playerHUD;
        this.world = world;

        this._tmp = new Vector3();
        this._oldPosition = new Vector3();

        this.cam = camera;
        birth();

        Player.instance = this;
    }

    public void birth(){
        yVelocity = 0;
        inAir = false;

        this.position = new Vector3(PLAYER_INIT_XI * Voxel.CUBE_SIZE, PLAYER_INIT_YI + PLAYER_HEIGHT + 20, PLAYER_INIT_ZI * Voxel.CUBE_SIZE);
        this.cam.position.set(this.position);
        this.cam.lookAt(1000, PLAYER_INIT_YI + PLAYER_HEIGHT + 20, 1000);
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

        BoundingBox playerBox = calculateBoundingBox();

        Voxel touchedVoxel = world.checkVoxelCollision(playerBox);
        if( touchedVoxel != null){

            checkTriggers(touchedVoxel);
            _lastTouchedVoxel = touchedVoxel;

            return true;
        }

        if(world.getCharacterManager().checkCharacterCollision(playerBox) != null){
            return true;
        }

        return false;
    }

    private void checkTriggers(Voxel touchedVoxel){

        //RE-ENABLE LAST VOXEL'S TRIGGER
        if(_lastTouchedVoxel != touchedVoxel){
            if(_lastTouchedVoxel != null && _lastTouchedVoxel.getTrigger() != null){
                _lastTouchedVoxel.getTrigger().reenable();

                if(DialogRenderer.instance.getCurrentDialog() != null){
                    DialogRenderer.instance.stopCurrentDialog();
                }
            }
        }

        //START THE TRIGGER IF THERE IS ONE ASSOCIATED WITH THIS VOXEL
        if(touchedVoxel.getTrigger() != null){
            touchedVoxel.getTrigger().startTrigger();
        }
    }

    private void checkItemsCollision(){
        Item item = world.checkItemCollision(position.x, position.y, position.z, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_DEPTH);
        if(item != null){
            //TODO show dialog
            playerHUD.addVoxelType(item.getYieldedVoxelType());
            world.removeItem(item);
        }
    }

    public BoundingBox calculateBoundingBox(){
        return Utils.buildBoundingBox(position, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_DEPTH);
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

    public PlayerHUD getPlayerHUD() {
        return playerHUD;
    }

    public static Player getInstance(){
        return instance;
    }
}
