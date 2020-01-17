/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.graphics.g3d.Model;

public enum CharacterType {
    CHICKEN("models/chicken-rigged.g3db",
            "chicken-rig|chicken-rig|chicken-rig|idle|chicken-rig|idle", 30, 2),
    WOLF("models/wolf.g3db", "Armature|Beta Idle", 200, 1),
    BEAR("models/bear.g3db", "Armature.BearO|Armature.BearO|Armature.BearO|Armature.BearOAction|Armature.Bea", 200, 1),
    JOEYTHESHEEP("models/joeythesheep.g3db", "Armature.JoeyTheSheep|Armature.JoeyTheSheep|Armature.JoeyTheSheep|Armature.JoeyTheShe", 200, 1),
    TIKI("models/tiki.g3db", "Malamute.Armature|Malamute.Armature|Malamute.Armature|ArmatureAction|Malamute.Arm", 50, 1);

    private Model model;
    private String defaultAnimationId;
    private int rotation;
    private int voxelMargins;

    CharacterType(String modelPath, String defaultAnimationId, int rotation, int voxelMargins){
        model = CharacterModelFactory.buildCharacterModel(modelPath);
        this.defaultAnimationId = defaultAnimationId;
        this.rotation = rotation;
        this.voxelMargins = voxelMargins;
    }

    public Model getModel() {
        return model;
    }

    public String getDefaultAnimationId() {
        return defaultAnimationId;
    }

    public int getRotation() {
        return rotation;
    }

    public int getVoxelMargins() {
        return voxelMargins;
    }
}
