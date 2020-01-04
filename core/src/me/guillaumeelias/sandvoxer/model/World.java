package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.Sandvoxer;
import me.guillaumeelias.sandvoxer.view.VoxelModelFactory;
import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static final int DISTANCE_MAX = 60;
    public static final int RAY_CASTING_STEP = 1;

    public static final int GRID_SIZE = 200;
    public static final int WORLD_SIDE_LENGTH = GRID_SIZE;

    public static final int PLATFORM_SIZE = 20;

    static final double FOURTH_OF_PI = Math.PI / 4;
    static final double THREE_FOURTH_OF_PI = FOURTH_OF_PI * 3;

    private final VoxelModelFactory voxelModelFactory;

    private Voxel cubes[][][];
    private Sandvoxer sandvoxer;
    private Player player;

    private List<ModelInstance> modelInstances;

    public World(Sandvoxer sandvoxer, VoxelModelFactory voxelModelFactory){
        this.sandvoxer = sandvoxer;
        this.voxelModelFactory = voxelModelFactory;
        this.modelInstances = new ArrayList<>(WORLD_SIDE_LENGTH*WORLD_SIDE_LENGTH);

        cubes = new Voxel[WORLD_SIDE_LENGTH][WORLD_SIDE_LENGTH][WORLD_SIDE_LENGTH];

        //INITIALIZE PLATFORMS
        initializePlatform(0, 90, 90, VoxelType.GRASS);
        initializePlatform(20, 60, 120, VoxelType.WOOD);
    }

    private void initializePlatform(int yi, int posX, int posZ, VoxelType voxelType){
        for (int xi = posX; xi < posX + PLATFORM_SIZE; xi += 1) {
            for (int zi = posZ; zi < posZ + PLATFORM_SIZE; zi += 1) {
                Model model = voxelModelFactory.buildModelType(voxelType);

                ModelInstance modelInstance = new ModelInstance(model);
                modelInstance.transform.translate(xi * Voxel.CUBE_SIZE, yi * Voxel.CUBE_SIZE, zi * Voxel.CUBE_SIZE);

                cubes[xi][yi][zi] = new Voxel(xi, yi, zi, modelInstance, voxelType);
                modelInstances.add(modelInstance);
            }
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

        //CHECK IF PLAYER IS AT SAME POSITION
        Vector3 min = new Vector3();
        Vector3 max = new Vector3();
        min.set(newXi*Voxel.CUBE_SIZE, newYi*Voxel.CUBE_SIZE, newZi*Voxel.CUBE_SIZE);
        max.set(newXi*Voxel.CUBE_SIZE + Voxel.CUBE_SIZE, newYi*Voxel.CUBE_SIZE + Voxel.CUBE_SIZE, newZi*Voxel.CUBE_SIZE + Voxel.CUBE_SIZE);

        BoundingBox playerBox = buildBoundingBox(player.getX(), player.getY(), player.getZ(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT, Player.PLAYER_DEPTH);

        if(playerBox.intersects(new BoundingBox(min, max))){
            Gdx.app.log("addBlock", "Can't place box on player position");
            return;
        }

        //BUILD BOX
        Model model = voxelModelFactory.buildModelType(VoxelType.SAND);

        ModelInstance modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(newXi * Voxel.CUBE_SIZE,newYi * Voxel.CUBE_SIZE,newZi * Voxel.CUBE_SIZE);

        cubes[newXi][newYi][newZi] = new Voxel(newXi, newYi, newZi, modelInstance, VoxelType.SAND);
        modelInstances.add(modelInstance);
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

        int xiMin = Math.round(x / Voxel.CUBE_SIZE ) - 1;
        int yiMin = Math.round(y / Voxel.CUBE_SIZE ) - 1;
        int ziMin = Math.round(z / Voxel.CUBE_SIZE ) - 1;

        int xiMax = Math.round((x + width) / Voxel.CUBE_SIZE ) + 1; //TODO actually if we're only using this with the player, we know how many tiles to check
        int yiMax = Math.round((y + height)/ Voxel.CUBE_SIZE ) + 1;
        int ziMax = Math.round((z + depth) / Voxel.CUBE_SIZE ) + 1;

        BoundingBox playerBox = buildBoundingBox(x, y, z, width, height, depth);

        for (int xi = xiMin; xi < xiMax; xi += 1) {
            for (int yi = yiMin; yi < yiMax; yi += 1) {
                for (int zi = ziMin; zi < ziMax; zi += 1) {

                    Voxel cube = getCubeOrNull(xi,yi,zi);
                    if(cube == null) continue;

                    if(playerBox.intersects(cube.boundingBox)){
                        return true;
                    }
                }

            }
        }

        return false;
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    private static BoundingBox buildBoundingBox(float x, float y, float z, int width, int height, int depth ){
        return new BoundingBox(new Vector3(x, y, z), new Vector3(x + width, y + height, z + depth));
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
