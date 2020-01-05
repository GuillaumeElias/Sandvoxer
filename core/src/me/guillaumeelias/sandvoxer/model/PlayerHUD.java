package me.guillaumeelias.sandvoxer.model;

import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.ArrayList;
import java.util.List;

public class PlayerHUD {

    List<VoxelType> voxelTypes;

    int selection;

    public PlayerHUD(){
        voxelTypes = new ArrayList<>();
        voxelTypes.add(VoxelType.SAND);

        selection = 0; //SAND
    }

    public VoxelType getSelectedVoxelType(){
        return voxelTypes.get(selection);
    }
}
