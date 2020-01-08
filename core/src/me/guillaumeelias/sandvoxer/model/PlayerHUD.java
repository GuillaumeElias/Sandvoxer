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

    public void selectionUp(){
        if(voxelTypes.size() - 1 > selection){
            selection++;
        }
    }

    public void selectionDown(){
        if(selection > 0){
            selection--;
        }
    }

    public void setSelection(int selection) {
        if(selection >= 0 && selection < voxelTypes.size()){
            this.selection = selection;
        }
    }

    public int getSelection() {
        return selection;
    }

    public VoxelType getSelectedVoxelType(){
        if(voxelTypes.isEmpty()) return null;

        return voxelTypes.get(selection);
    }

    public List<VoxelType> getVoxelTypes() {
        return voxelTypes;
    }

    public void addVoxelType(VoxelType yieldedVoxelType) {
        voxelTypes.add(yieldedVoxelType);
    }
}
