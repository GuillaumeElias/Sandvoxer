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

    public static final int DISTANCE_MAX = 50;
    public static final float RAY_CASTING_STEP = 1; //TODO

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
        Voxel pointedVoxel = findCubeAtRay(rayFrom, rayTo);

        int newXi;
        int newYi;
        int newZi;

        if(pointedVoxel == null)
        {
            rayTo.nor();
            rayTo.scl(Player.NEW_BLOCK_REACH);
            rayFrom.add(rayTo);

            newXi = Math.round(rayFrom.x / Voxel.CUBE_SIZE );
            newYi = Math.round(rayFrom.y / Voxel.CUBE_SIZE );
            newZi = Math.round(rayFrom.z / Voxel.CUBE_SIZE );

        }else{

            newXi = pointedVoxel.xi;
            newYi = pointedVoxel.yi;
            newZi = pointedVoxel.zi;

            if(pointedVoxel.yi * Voxel.CUBE_SIZE < player.getY() - 1){ //LOOKING AT TOP FACE
                newYi++;
            }else{ //LOOKING AT A SIDE

                double radZiXi = Math.atan2( //find out horizontal angle between player and pointed voxel
                        pointedVoxel.zi * Voxel.CUBE_SIZE - player.getZ(),
                        pointedVoxel.xi * Voxel.CUBE_SIZE - player.getX()
                ) ;

                if(radZiXi > -FOURTH_OF_PI && radZiXi < FOURTH_OF_PI){
                    newXi--;
                }else if( radZiXi > FOURTH_OF_PI && radZiXi < THREE_FOURTH_OF_PI){
                    newZi--;
                }else if( radZiXi < -FOURTH_OF_PI && radZiXi > -THREE_FOURTH_OF_PI){
                    newZi++;
                }else{
                    newXi++;
                }
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
        Voxel pointedVoxel = findCubeAtRay(rayFrom, rayTo);

        if(pointedVoxel != null)
        {
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
        Model model = voxelModelFactory.buildModelType(VoxelType.RANDOM_COLOR);

        ModelInstance modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(newXi * Voxel.CUBE_SIZE,newYi * Voxel.CUBE_SIZE,newZi * Voxel.CUBE_SIZE);

        cubes[newXi][newYi][newZi] = new Voxel(newXi, newYi, newZi, modelInstance, VoxelType.RANDOM_COLOR);
        modelInstances.add(modelInstance);
    }


    public Voxel findCubeAtRay(Vector3 rayFrom, Vector3 rayTo){
        Vector3 tmp = new Vector3(rayFrom);
        Vector3 step = new Vector3(rayTo);

        for(int i = 0; i < DISTANCE_MAX; i += RAY_CASTING_STEP){

            Voxel voxel = getVoxel(tmp.x, tmp.y, tmp.z);
            if(voxel != null)
            {
                Gdx.app.log("onClickBlock","hit !");
                return voxel;
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

        int xiMin = (int) Math.floor(x / Voxel.CUBE_SIZE );
        int yiMin = (int) Math.floor(y / Voxel.CUBE_SIZE );
        int ziMin = (int) Math.floor(z / Voxel.CUBE_SIZE );

        int xiMax = (int) Math.ceil(x + width / Voxel.CUBE_SIZE );
        int yiMax = (int) Math.ceil(y + height / Voxel.CUBE_SIZE );
        int ziMax = (int) Math.ceil(z + depth / Voxel.CUBE_SIZE );

        BoundingBox playerBox = buildBoundingBox(x, y, z, width, height, depth);
        Vector3 min = new Vector3();
        Vector3 max = new Vector3();
        BoundingBox tmpBoundingBox = new BoundingBox();

        for (int xi = xiMin; xi < xiMax; xi += 1) {
            for (int yi = yiMin; yi < yiMax; yi += 1) {
                for (int zi = ziMin; zi < ziMax; zi += 1) {
                    if(getCubeOrNull(xi,yi,zi) == null)continue;

                    min.set(xi*Voxel.CUBE_SIZE, yi*Voxel.CUBE_SIZE, zi*Voxel.CUBE_SIZE);
                    max.set(xi*Voxel.CUBE_SIZE + Voxel.CUBE_SIZE, yi*Voxel.CUBE_SIZE + Voxel.CUBE_SIZE, zi*Voxel.CUBE_SIZE + Voxel.CUBE_SIZE);
                    tmpBoundingBox.set(min, max);
                    if(playerBox.intersects(tmpBoundingBox)){
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
}
