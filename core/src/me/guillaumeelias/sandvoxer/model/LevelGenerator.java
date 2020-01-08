package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.math.Vector3;
import me.guillaumeelias.sandvoxer.model.trigger.DialogTrigger;
import me.guillaumeelias.sandvoxer.model.trigger.EndLevelTrigger;
import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.List;

public class LevelGenerator {

    public static final int PLATFORM_SIZE = 20;

    public static Voxel[][][] initializeLevel(int level, List<Item> itemList){

        Voxel[][][] cubes = new Voxel[World.WORLD_SIDE_LENGTH][World.WORLD_SIDE_LENGTH][World.WORLD_SIDE_LENGTH];

        switch (level) {
            case 0:
                //INITIALIZE PLATFORMS
                createPlatform(0, 90, 90, VoxelType.GRASS, cubes);
                createPlatform(18, 130, 130, VoxelType.WOOD, cubes);
                createPlatform(80, 30, 150, VoxelType.BLUE_STUFF, cubes);

                //ADD AD-HOC BLOCKS
                cubes[40][81][160] = new Voxel (40,81,160, VoxelType.RED_COLOR);

                //ADD ITEMS
                createNewItem(new Vector3(95 * Voxel.CUBE_SIZE, Voxel.CUBE_SIZE,Voxel.CUBE_SIZE * 95), VoxelType.SAND, itemList);
                createNewItem(new Vector3(135 * Voxel.CUBE_SIZE, 19 * Voxel.CUBE_SIZE,Voxel.CUBE_SIZE * 135), VoxelType.BOUNCY_STUFF, itemList);

                //ADD TRIGGERS
                cubes[100][0][101].setTrigger(new DialogTrigger(Dialog.CHICKEN_DIALOG_1, Dialog.CHICKEN_DIALOG_REPEAT, 1));
                cubes[139][18][138].setTrigger(new DialogTrigger(Dialog.WOLF_DIALOG_1, Dialog.WOLF_DIALOG_REPEAT, 2));
                /*cubes[40][81][160]*/cubes[100][0][102].setTrigger(new EndLevelTrigger(3));

                break;
            case 1:
                //INITIALIZE PLATFORMS
                createPlatform(0, 90, 90, VoxelType.WOOD, cubes);
                createPlatform(18, 130, 130, VoxelType.BLUE_STUFF, cubes);
                createPlatform(80, 30, 150, VoxelType.GRASS, cubes);

                //ADD AD-HOC BLOCKS
                //TODO

                //ADD ITEMS
                //TODO

                //ADD TRIGGERS
                //TODO

                break;
        }

        return cubes;
    }

    private static void createPlatform(int yi, int posXi, int posZi, VoxelType voxelType, Voxel[][][] cubes){
        for (int xi = posXi; xi < posXi + PLATFORM_SIZE; xi += 1) {
            for (int zi = posZi; zi < posZi + PLATFORM_SIZE; zi += 1) {
                cubes[xi][yi][zi] = new Voxel(xi, yi, zi, voxelType);
            }
        }
    }

    private static void createNewItem(Vector3 position, VoxelType yieldedVoxelType, List<Item> itemList){
        Item item = new Item(position, yieldedVoxelType);
        itemList.add(item);
    }

}
