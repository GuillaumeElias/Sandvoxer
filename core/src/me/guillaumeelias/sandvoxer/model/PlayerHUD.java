package me.guillaumeelias.sandvoxer.model;

import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.ArrayList;
import java.util.List;

public class PlayerHUD {

    List<VoxelType> voxelTypes;

    int selection;

    public PlayerHUD(){
        voxelTypes = new ArrayList<>();

        selection = 0; //SAND
    }

    public VoxelType getSelectedVoxelType(){
        if(voxelTypes.isEmpty()) return null;

        return voxelTypes.get(selection);
    }

    public void addVoxelType(VoxelType yieldedVoxelType) {
        voxelTypes.add(yieldedVoxelType);
    }
}
