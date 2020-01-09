package me.guillaumeelias.sandvoxer.model;

import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHUD {


    boolean infiniteMaterials;

    Map<VoxelType, Integer> voxelTypesQuantities;

    List<VoxelType> voxelTypes;

    int selection;

    public PlayerHUD(){
        voxelTypes = new ArrayList<>();

        infiniteMaterials = true;
        voxelTypesQuantities = new HashMap<>();

        selection = 0; //SAND
    }

    public void setSelection(int selection) {
        if(selection >= 0 && selection < voxelTypes.size()){
            this.selection = selection;
        }
    }

    public void setVoxelTypes(List<VoxelType> voxelTypes) {
        this.voxelTypes = voxelTypes;
    }

    public int getSelection() {
        return selection;
    }

    public VoxelType getSelectedVoxelType(){
        if(selection >= voxelTypes.size()) return null;

        return voxelTypes.get(selection);
    }

    public boolean hasVoxelType(VoxelType voxelType){
        return voxelTypesQuantities.containsKey(voxelType);
    }

    public int getQuantityForVoxelType(VoxelType voxelType){
        Integer quantity = voxelTypesQuantities.get(voxelType);
        if(quantity == null){
            return 0;
        }

        return quantity;
    }

    public void setVoxelTypeQuantity(VoxelType voxelType, int quantity){
        voxelTypesQuantities.put(voxelType, quantity);
    }

    public void decrementVoxelTypeQuantity(VoxelType voxelType){
        voxelTypesQuantities.put(voxelType,  getQuantityForVoxelType(voxelType) - 1);
    }

    public void incrementVoxelTypeQuantity(VoxelType voxelType){
        voxelTypesQuantities.put(voxelType,  getQuantityForVoxelType(voxelType) + 1);
    }

    public boolean canPlaceVoxelType(VoxelType voxelType){
        return getQuantityForVoxelType(voxelType) > 0;
    }

    public List<VoxelType> getVoxelTypes() {
        return voxelTypes;
    }

    public void addVoxelType(VoxelType yieldedVoxelType) {
        voxelTypes.add(yieldedVoxelType);
    }

    public void setInfiniteMaterials(boolean infiniteMaterials) {
        this.infiniteMaterials = infiniteMaterials;
    }

    public boolean isInfiniteMaterials() {
        return infiniteMaterials;
    }
}
