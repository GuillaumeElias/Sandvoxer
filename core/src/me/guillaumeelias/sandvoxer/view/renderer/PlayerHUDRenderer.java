package me.guillaumeelias.sandvoxer.view.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.guillaumeelias.sandvoxer.model.PlayerHUD;
import me.guillaumeelias.sandvoxer.model.World;
import me.guillaumeelias.sandvoxer.view.VoxelType;

public class PlayerHUDRenderer {

    public static final int HUD_LEVEL_TOP_MARGIN = 10;
    public static final int HUD_MARGINS = 20;
    public static final int HUD_FONT_PADDING_BOTTOM = 30;
    public static final int HUD_FONT_PADDING_LEFT = 3;

    private PlayerHUD playerHUD;
    private World world;
    private BitmapFont font;

    Pixmap cursorPixmap;
    Texture cursorPixmapTexture;

    public PlayerHUDRenderer(PlayerHUD playerHUD, World world, BitmapFont font){
        this.playerHUD = playerHUD;
        this.world = world;
        this.font = font;
    }

    public void initialize(){
        cursorPixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
        cursorPixmap.setColor(Color.WHITE);
        cursorPixmap.fillRectangle(0, 0, 4, 4);
        cursorPixmapTexture = new Texture(cursorPixmap, Pixmap.Format.RGB888, false);
    }

    public void render(SpriteBatch spriteBatch, float width, float height){
        //DRAW CURSOR
        spriteBatch.draw(cursorPixmapTexture, width / 2, height / 2);

        //DRAW SELECTED VOXEL TYPE
        VoxelType selectedVoxelType = playerHUD.getSelectedVoxelType();

        if(selectedVoxelType != null){
            Texture texture = selectedVoxelType.getTexture();

            float hudX = width - texture.getWidth() - HUD_MARGINS;
            spriteBatch.draw(texture, hudX, HUD_MARGINS);
            font.draw(spriteBatch, selectedVoxelType.getName(), hudX + HUD_FONT_PADDING_LEFT, HUD_MARGINS + texture.getHeight() + HUD_FONT_PADDING_BOTTOM);

            //draw quantity left
            //TODO
        }

        //DRAW LEVEL NUMBER
        font.draw(spriteBatch, "Level "+ (world.getCurrentLevel() + 1), HUD_MARGINS, height - HUD_LEVEL_TOP_MARGIN);

    }

    public void dispose(){
        cursorPixmap.dispose();
    }
}
