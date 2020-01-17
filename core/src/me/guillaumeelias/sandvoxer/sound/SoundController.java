package me.guillaumeelias.sandvoxer.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundController {

    private static final float MUSIC_VOLUME = 0.5f;

    private static Music currentMusic;

    private static Sound letterSound;
    private static Sound placeBlockSound;
    private static Sound removeBlockSound;
    private static Sound jumpSound;
    private static Sound jumpBounceSound;
    private static Sound bounceSound;
    private static Sound reachedGroundSound;
    private static Sound newItemSound;
    private static Sound selectVoxelTypeOneSound;
    private static Sound selectVoxelTypeTwoSound;
    private static Sound walkSound;
    private static Sound levelFinishedSound;


    private static Map<SoundEvent, Long> soundIds = new HashMap<>();

    public static void initialize() {
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/music/level0.mp3"));

        letterSound = Gdx.audio.newSound(Gdx.files.internal("sound/letter.mp3"));
        placeBlockSound = Gdx.audio.newSound(Gdx.files.internal("sound/placeBlock.mp3"));
        removeBlockSound = Gdx.audio.newSound(Gdx.files.internal("sound/removeBlock.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sound/jump.mp3"));
        jumpBounceSound = Gdx.audio.newSound(Gdx.files.internal("sound/jumpBounce.mp3"));
        bounceSound = Gdx.audio.newSound(Gdx.files.internal("sound/bounce.mp3"));
        reachedGroundSound = Gdx.audio.newSound(Gdx.files.internal("sound/reachedGround.mp3"));
        newItemSound = Gdx.audio.newSound(Gdx.files.internal("sound/newItem.mp3"));
        selectVoxelTypeOneSound = Gdx.audio.newSound(Gdx.files.internal("sound/selectVoxelTypeOne.mp3"));
        selectVoxelTypeTwoSound = Gdx.audio.newSound(Gdx.files.internal("sound/selectVoxelTypeTwo.mp3"));
        walkSound = Gdx.audio.newSound(Gdx.files.internal("sound/walk.mp3"));
        levelFinishedSound = Gdx.audio.newSound(Gdx.files.internal("sound/levelFinished.mp3"));
    }

    public static void startMusic(){
        currentMusic.play();
        currentMusic.setLooping(true);
        currentMusic.setVolume(MUSIC_VOLUME);
    }

    public static void changeMusicForLevel(int level){

        currentMusic.stop();
        currentMusic.dispose();

        switch (level) {
            case 0:
                currentMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/music/level0.mp3"));
                break;
            case 1:
                currentMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/music/level1.mp3"));
                break;
        }

        startMusic();
    }

    public static void soundEvent(SoundEvent soundEvent){
        long id = 0;

        switch (soundEvent){
            case LETTER:
                id = letterSound.play();
                letterSound.setVolume(id,0.7f);
                break;
            case PLACE_BLOCK:
                id = placeBlockSound.play();
                break;
            case REMOVE_BLOCK:
                id = removeBlockSound.play();
                break;
            case JUMP:
                id  = jumpSound.play();
                break;
            case JUMP_BOUNCE:
                id  = jumpBounceSound.play();
                break;
            case BOUNCE:
                id  = bounceSound.play();
                break;
            case REACHED_GROUND:
                id  = reachedGroundSound.play();
                break;
            case NEW_ITEM:
                id  = newItemSound.play();
                break;
            case SELECT_VOXEL_TYPE_ONE:
                id  = selectVoxelTypeOneSound.play();
                break;
            case SELECT_VOXEL_TYPE_TWO:
                id  = selectVoxelTypeTwoSound.play();
                break;
            case WALK:
                id  = walkSound.play();
                walkSound.setLooping(id, true);
                break;
            case LEVEL_FINISHED:
                id  = levelFinishedSound.play();
                break;

        }

        soundIds.put(soundEvent, id);
    }

    public static void stopSound(SoundEvent soundEvent){
        switch (soundEvent){
            case JUMP:
                jumpSound.stop();
                break;
            case JUMP_BOUNCE:
                jumpBounceSound.stop();
                break;
            case REACHED_GROUND:
                reachedGroundSound.stop();
                break;
            case WALK:
                walkSound.stop();
                break;
        }

        soundIds.remove(soundEvent);
    }

    public static boolean isSoundPlaying(SoundEvent soundEvent){
        return soundIds.containsKey(soundEvent);
    }

    public static void pauseMusic() {
        currentMusic.pause();
    }

    public static void resumeMusic() {
        currentMusic.play();
    }

    public static void stopMusic() {
        currentMusic.stop();
    }

    public static void disposeAll(){

        currentMusic.stop();
        currentMusic.dispose();

        letterSound.dispose();
        placeBlockSound.dispose();
        removeBlockSound.dispose();
        jumpSound.dispose();
        jumpBounceSound.dispose();
        bounceSound.dispose();
        reachedGroundSound.dispose();
        newItemSound.dispose();
        selectVoxelTypeOneSound.dispose();
        selectVoxelTypeTwoSound.dispose();
        walkSound.dispose();
        levelFinishedSound.dispose();
    }
}
