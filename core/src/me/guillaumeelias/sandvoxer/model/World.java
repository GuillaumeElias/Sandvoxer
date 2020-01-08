package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.util.Utils;
import me.guillaumeelias.sandvoxer.view.CharacterManager;
import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static final int DISTANCE_MAX = 60;
    public static final int RAY_CASTING_STEP = 1;

    public static final int GRID_SIZE = 200;
    public static final int WORLD_SIDE_LENGTH = GRID_SIZE;

    private Voxel cubes[][][];
    private Player player;
    private List<Item> items;
    private int currentLevel;

    private List<ModelInstance> modelInstances;
    private CharacterManager characterManager;

    public World(){
        this.currentLevel = 0;
        this.modelInstances = new ArrayList<>(WORLD_SIDE_LENGTH*WORLD_SIDE_LENGTH);
        this.items = new ArrayList<>();
        this.characterManager = new CharacterManager();

        startLevel();
    }

    public void startNextLevel(){
        currentLevel++;
        startLevel();
    }

    private void startLevel(){
        this.modelInstances.clear();
        this.items.clear();

        cubes = LevelGenerator.initializeLevel(currentLevel, items);
        populateModelInstancesFromLevel();
        characterManager.initializeCharactersForLevel(currentLevel);
    }

    public void populateModelInstancesFromLevel(){ //TODO move elsewhere

        //parse all cubes
        for (int xi = 0; xi < GRID_SIZE; xi += 1) {
            for (int yi = 0; yi < GRID_SIZE; yi += 1) {
                for (int zi = 0; zi < GRID_SIZE; zi += 1) {
                    Voxel cube = cubes[xi][yi][zi];
                    if(cube != null){
                        modelInstances.add(cube.modelInstance);
                    }
                }
            }
        }

        //parse item
        for(Item item : items){
            this.modelInstances.add(item.getModelInstance());
        }
    }

    public void onClickBlock(Vector3 rayFrom, Vector3 rayTo){
        HitVoxel hitVoxel = findCubeAtRay(rayFrom, rayTo);

        int newXi;
        int newYi;
        int newZi;

        if(hitVoxel == null)
        {
            rayTo.nor();
            rayTo.scl(Player.NEW_BLOCK_REACH);
            rayFrom.add(rayTo);

            newXi = Math.round(rayFrom.x / Voxel.CUBE_SIZE );
            newYi = Math.round(rayFrom.y / Voxel.CUBE_SIZE );
            newZi = Math.round(rayFrom.z / Voxel.CUBE_SIZE );

        }else{
            Voxel pointedVoxel = hitVoxel.voxel;

            newXi = pointedVoxel.xi;
            newYi = pointedVoxel.yi;
            newZi = pointedVoxel.zi;

            int aXi = Math.round(hitVoxel.incisionPoint.x / Voxel.CUBE_SIZE);
            int aYi = Math.round(hitVoxel.incisionPoint.y / Voxel.CUBE_SIZE);
            int aZi = Math.round(hitVoxel.incisionPoint.z / Voxel.CUBE_SIZE);

            if (aYi > pointedVoxel.yi) { //CLICKED ON TOP FACE
                newYi++;
            }else if (aYi < pointedVoxel.yi){ //CLICKED ON BOTTOM FACE
                newYi--;
            }else if(aXi < pointedVoxel.xi){
                newXi--;
            }else if(aZi < pointedVoxel.zi){
                newZi--;
            }else if(aZi > pointedVoxel.zi){
                newZi++;
            }else {
                newXi++;
            }

        }

        if( newXi < 0 || newXi >= GRID_SIZE ||
                newYi < 0 || newYi >= GRID_SIZE ||
                newZi < 0 || newZi >= GRID_SIZE  ){
            Gdx.app.log("onClickBlock", "Can't add block outside world.");
            return;
        }

        Voxel newVoxel = getCube(newXi, newYi, newZi);

        if(newVoxel != null){
            //TODO cope with above block already exists
            return;
        }

        addBlock(newXi, newYi, newZi);
    }

    public void onRightClickBlock(Vector3 rayFrom, Vector3 rayTo) {
        HitVoxel hitVoxel = findCubeAtRay(rayFrom, rayTo);

        if(hitVoxel != null)
        {
            Voxel pointedVoxel = hitVoxel.voxel;
            /*if(pointedVoxel.type == VoxelType.RANDOM_COLOR){
                pointedVoxel.getModelInstance().model.dispose();
            }*/
            modelInstances.remove(pointedVoxel.getModelInstance());
            cubes[pointedVoxel.xi][pointedVoxel.yi][pointedVoxel.zi] = null;
        }
    }

    public void addBlock(int newXi, int newYi, int newZi){

        VoxelType voxelTypeSelected = player.getPlayerHUD().getSelectedVoxelType();
        if(voxelTypeSelected == null) {
            return;
        }

        //CHECK IF PLAYER IS AT SAME POSITION
        if(canBuildBlock(newXi, newYi, newZi) == false){
            return;
        }

        //BUILD BOX
        placeBlock(newXi, newYi, newZi, voxelTypeSelected);
    }

    private void placeBlock(int xi, int yi, int zi, VoxelType voxelType){
        cubes[xi][yi][zi] = new Voxel(xi, yi, zi, voxelType);
        modelInstances.add(cubes[xi][yi][zi].modelInstance);
    }

    public void restoreSpawnBlockIfDestructed(){
        if(getCube(Player.PLAYER_INIT_XI,Player.PLAYER_INIT_YI,Player.PLAYER_INIT_ZI) == null){
            placeBlock(Player.PLAYER_INIT_XI,Player.PLAYER_INIT_YI,Player.PLAYER_INIT_ZI, VoxelType.GRASS);
        }
    }

    private boolean canBuildBlock(int newXi, int newYi, int newZi) {

        int newX = newXi*Voxel.CUBE_SIZE;
        int newY = newYi*Voxel.CUBE_SIZE;
        int newZ = newZi*Voxel.CUBE_SIZE;

        Vector3 min = new Vector3();
        Vector3 max = new Vector3();
        min.set(newX, newY, newZ);
        max.set(newX + Voxel.CUBE_SIZE, newY + Voxel.CUBE_SIZE, newZ + Voxel.CUBE_SIZE);
        BoundingBox newVoxelBox = new BoundingBox(min, max);

        //gameScreen.createDebugBox(newVoxelBox);

        BoundingBox playerBox = player.calculateBoundingBox();

        if(playerBox.intersects(newVoxelBox)){
            Gdx.app.log("addBlock", "Can't place box on player position");
            return false;
        }

        if(checkItemCollision(newX, newY, newZ, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE, Voxel.CUBE_SIZE) != null){
            Gdx.app.log("addBlock", "Can't place box on item");
            return false;
        }

        if(characterManager.checkCharacterCollision(newVoxelBox) != null){
            Gdx.app.log("addBlock", "Can't place box on character");
            return false;
        }

        return true;
    }


    public HitVoxel findCubeAtRay(Vector3 rayFrom, Vector3 rayTo){
        Vector3 tmp = new Vector3(rayFrom);
        Vector3 step = new Vector3(rayTo);

        for(int i = 0; i < DISTANCE_MAX; i += RAY_CASTING_STEP){

            Voxel voxel = getVoxel(tmp.x, tmp.y, tmp.z);
            if(voxel != null)
            {
                return new HitVoxel(voxel, tmp.sub(step));
            }
            tmp.add(step);
        }
        return null;
    }

    public Voxel getCube(int xi, int yi, int zi) {

        return cubes[xi][yi][zi];
    }

    public Voxel getCubeOrNull(int xi, int yi, int zi) {

        if( xi < 0 || xi >= GRID_SIZE ||
                yi < 0 || yi >= GRID_SIZE ||
                zi < 0 || zi >= GRID_SIZE  ){
            return null;
        }

        return cubes[xi][yi][zi];
    }

    public boolean isGround(int x, int y, int z) {

        int xi = x / Voxel.CUBE_SIZE;
        int yi = y / Voxel.CUBE_SIZE;
        int zi = z / Voxel.CUBE_SIZE;

        return getCubeOrNull(xi, yi, zi) != null;
    }

    public boolean checkBoxCollision(float x, float y, float z, int width, int height, int depth){

        BoundingBox playerBox = Utils.buildBoundingBox(x, y, z, width, height, depth);

        if(checkVoxelCollision(playerBox) != null){
            return true;
        }

        if(characterManager.checkCharacterCollision(playerBox) != null){
            return true;
        }

        return false;
    }

    public Voxel checkVoxelCollision(BoundingBox playerBox){

        int xiMin = Math.round(playerBox.min.x / Voxel.CUBE_SIZE ) - 1;
        int yiMin = Math.round(playerBox.min.y / Voxel.CUBE_SIZE ) - 1;
        int ziMin = Math.round(playerBox.min.z / Voxel.CUBE_SIZE ) - 1;

        int xiMax = Math.round((playerBox.min.x + playerBox.getWidth()) / Voxel.CUBE_SIZE ) + 1; //TODO actually if we're only using this with the player, we know how many tiles to check
        int yiMax = Math.round((playerBox.min.y + playerBox.getHeight())/ Voxel.CUBE_SIZE ) + 1;
        int ziMax = Math.round((playerBox.min.z + playerBox.getDepth()) / Voxel.CUBE_SIZE ) + 1;

        for (int xi = xiMin; xi < xiMax; xi += 1) {
            for (int yi = yiMin; yi < yiMax; yi += 1) {
                for (int zi = ziMin; zi < ziMax; zi += 1) {

                    Voxel cube = getCubeOrNull(xi,yi,zi);
                    if(cube == null) continue;

                    if(playerBox.intersects(cube.boundingBox)){
                        return cube;
                    }
                }

            }
        }

        return null;
    }

    public Voxel getVoxel(float x, float y, float z) {

        int xi = Math.round(x / Voxel.CUBE_SIZE );
        int yi = Math.round(y / Voxel.CUBE_SIZE );
        int zi = Math.round(z / Voxel.CUBE_SIZE );

        if( xi < 0 || xi >= GRID_SIZE ||
                yi < 0 || yi >= GRID_SIZE ||
                zi < 0 || zi >= GRID_SIZE  ){
            return null;
        }

        return cubes[xi][yi][zi];
    }

    public List<ModelInstance> getModelInstances() {
        return modelInstances;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Item checkItemCollision(float x, float y, float z, int width, int height, int depth){
        BoundingBox playerBox = player.calculateBoundingBox();

        for(Item item : items){
            if(playerBox.intersects(item.getBoundingBox())){
                return item;
            }
        }

        return null;
    }

    public void removeItem(Item item) {
        modelInstances.remove(item.getModelInstance());
        items.remove(item);
    }

    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    private static class HitVoxel {
        Voxel voxel;
        Vector3 incisionPoint;

        public HitVoxel(Voxel voxel, Vector3 tmp) {
            this.voxel = voxel;
            this.incisionPoint = tmp;
        }
    }
}
