package me.guillaumeelias.sandvoxer.model;

import com.badlogic.gdx.math.Vector3;
import me.guillaumeelias.sandvoxer.model.trigger.DialogTrigger;
import me.guillaumeelias.sandvoxer.model.trigger.EndLevelTrigger;
import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelGenerator {

    public static final int PLATFORM_SIZE = 20;

    public static Voxel[][][] initializeLevel(int level, List<Item> itemList, PlayerHUD playerHUD){

        Voxel[][][] cubes = new Voxel[World.WORLD_SIDE_LENGTH][World.WORLD_SIDE_LENGTH][World.WORLD_SIDE_LENGTH];

        switch (level) {
            case 0:
                playerHUD.setInfiniteMaterials(true);

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
                cubes[40][81][160].setTrigger(new EndLevelTrigger(3));

                break;
            case 1:

                playerHUD.setInfiniteMaterials(false);
                playerHUD.setVoxelTypes(new ArrayList<>(Arrays.asList(new VoxelType[]{VoxelType.SAND})));
                playerHUD.setSelection(0);
                playerHUD.setVoxelTypeQuantity(VoxelType.SAND,3);
                playerHUD.setVoxelTypeQuantity(VoxelType.BOUNCY_STUFF,9);

                //INITIALIZE PLATFORMS
                createPlatform(0, 90, 90, VoxelType.DIRT, cubes);
                createPlatform(7, 120, 120, VoxelType.GRASS_2, cubes);
                createPlatform(30, 150, 30, VoxelType.WOOD, cubes);
                createPlatform(90, 100, 60, VoxelType.BLUE_STUFF, cubes);

                //ADD AD-HOC BLOCKS
                cubes[110][91][70] = new Voxel (110,91,70, VoxelType.RED_COLOR);

                //ADD ITEMS
                createNewItem(new Vector3(130 * Voxel.CUBE_SIZE, 8 * Voxel.CUBE_SIZE,Voxel.CUBE_SIZE * 130), VoxelType.SAND, itemList);
                createNewItem(new Vector3(160 * Voxel.CUBE_SIZE, 31 * Voxel.CUBE_SIZE,Voxel.CUBE_SIZE * 35), VoxelType.BOUNCY_STUFF, itemList);
                createNewItem(new Vector3(165 * Voxel.CUBE_SIZE, 31 * Voxel.CUBE_SIZE,Voxel.CUBE_SIZE * 40), VoxelType.SAND, itemList);

                //ADD TRIGGERS
                cubes[100][0][101].setTrigger(new DialogTrigger(Dialog.BEAR_DIALOG_1, Dialog.BEAR_DIALOG_REPEAT, 1));
                cubes[135][7][135].setTrigger(new DialogTrigger(Dialog.JOEYTHESHEEP_DIALOG_1, Dialog.JOEYTHESHEEP_DIALOG_REPEAT, 1));
                cubes[159][30][41].setTrigger(new DialogTrigger(Dialog.TIKI_DIALOG_1, Dialog.TIKI_DIALOG_REPEAT, 1));
                cubes[110][91][70].setTrigger(new EndLevelTrigger(4));

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
