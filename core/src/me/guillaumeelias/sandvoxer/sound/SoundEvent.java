/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.sound;

public enum SoundEvent {
    LETTER,
    PLACE_BLOCK,
    REMOVE_BLOCK,
    JUMP,
    JUMP_BOUNCE,
    BOUNCE,
    REACHED_GROUND,
    NEW_ITEM,
    SELECT_VOXEL_TYPE_ONE,
    SELECT_VOXEL_TYPE_TWO,
    WALK,
    LEVEL_FINISHED
    //TODO add game over sound when falling
}
