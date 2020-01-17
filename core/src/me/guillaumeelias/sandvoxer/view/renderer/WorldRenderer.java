/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.view.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import me.guillaumeelias.sandvoxer.model.Item;
import me.guillaumeelias.sandvoxer.model.Voxel;
import me.guillaumeelias.sandvoxer.model.World;

import java.util.ArrayList;
import java.util.List;

public class WorldRenderer {

    public static final WorldRenderer instance = new WorldRenderer();

    private static final float VISIBLE_VOXEL_RADIUS = Voxel.CUBE_SIZE * 2;
    private static final float VISIBLE_ITEM_RADIUS = Voxel.CUBE_SIZE;

    private World world;
    private Environment environment;

    private List<Voxel> drawableVoxels;

    public void initialize(Environment environment){
        drawableVoxels = new ArrayList<>();

        this.environment = environment;
    }

    public void initializeLevel(World world){

        clear();

        this.world = world;

        Voxel cubes[][][] = world.getCubes();

        //parse all cubes                               //TODO merge all of them together to create a single mesh
        for (int xi = 0; xi < World.GRID_SIZE; xi += 1) {
            for (int yi = 0; yi < World.GRID_SIZE; yi += 1) {
                for (int zi = 0; zi < World.GRID_SIZE; zi += 1) {
                    Voxel cube = cubes[xi][yi][zi];
                    if(cube != null){
                        drawableVoxels.add(cube);
                    }
                }
            }
        }
    }

    public void removeVoxel(Voxel voxel){
        drawableVoxels.remove(voxel);
    }

    public void addVoxel(Voxel voxel){
        drawableVoxels.add(voxel);
    }

    public void clear(){
        drawableVoxels.clear();
    }

    public void render(ModelBatch modelBatch, Camera cam) {

       // int counter = 0;

        for(Voxel voxel : drawableVoxels){
            if(isVoxelVisible(cam, voxel)){
                modelBatch.render(voxel.getModelInstance(), environment);
                //counter++;
            }
        }

        for(Item item : world.getItems()){
            if(isItemVisible(cam, item)){
                modelBatch.render(item.getModelInstance(), environment);
            }
        }

        //Gdx.app.log("render", "counter: "+counter);
    }

    private boolean isVoxelVisible(final Camera cam, final Voxel voxel) {
        return cam.frustum.sphereInFrustum(voxel.getCenter(), VISIBLE_VOXEL_RADIUS);
    }

    private boolean isItemVisible(final Camera cam, final Item item) {
        return cam.frustum.sphereInFrustum(item.getCenter(), VISIBLE_ITEM_RADIUS);
    }
}
