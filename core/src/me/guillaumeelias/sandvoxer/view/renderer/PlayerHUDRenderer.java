/**********************************************************************************************************************************
 * This file is part of the Sandvoxer project developed by Guillaume Elias and covered by the Apache 2 license (see LICENSE file).*
 * You may reuse it in accordance with the Apache 2 license and at your own peril.                                                *                                                             *
 **********************************************************************************************************************************/

package me.guillaumeelias.sandvoxer.view.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.guillaumeelias.sandvoxer.Sandvoxer;
import me.guillaumeelias.sandvoxer.model.PlayerHUD;
import me.guillaumeelias.sandvoxer.model.World;
import me.guillaumeelias.sandvoxer.util.Utils;
import me.guillaumeelias.sandvoxer.view.VoxelType;

public class PlayerHUDRenderer {

    public static final int HUD_LEVEL_TOP_MARGIN = 10;
    public static final int HUD_MARGINS = 20;
    public static final int HUD_FONT_PADDING_BOTTOM = 30;
    public static final int HUD_FONT_PADDING_LEFT = 3;

    public static final int HUD_QUANTITY_PADDING_LEFT = 14;
    public static final int HUD_QUANTITY_PADDING_BOTTOM = 18;

    public static final int HUD_QUANTITY_BACKGROUND_WIDTH = 18;
    public static final int HUD_QUANTITY_BACKGROUND_HEIGHT = 18;
    public static final int HUD_QUANTITY_BACKGROUND_PADDING_RIGHT = 19;
    public static final int HUD_QUANTITY_BACKGROUND_PADDING_BOTTOM = 1;


    private PlayerHUD playerHUD;
    private World world;
    private BitmapFont font;

    Pixmap cursorPixmap;
    Texture cursorPixmapTexture;

    Texture quantityBackgroundPixmap;

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

        quantityBackgroundPixmap = new Texture(Utils.getPixmapRoundedRectangle(HUD_QUANTITY_BACKGROUND_WIDTH, HUD_QUANTITY_BACKGROUND_HEIGHT, 3, Sandvoxer.BACKGROUND_COLOR));
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

            //draw quantity left (if not infinite)
            if(playerHUD.isInfiniteMaterials() == false){
                int quantityLeft = playerHUD.getQuantityForVoxelType(selectedVoxelType);

                float quantityX = hudX + texture.getWidth();
                spriteBatch.draw(quantityBackgroundPixmap, quantityX - HUD_QUANTITY_BACKGROUND_PADDING_RIGHT, HUD_MARGINS + HUD_QUANTITY_BACKGROUND_PADDING_BOTTOM);
                font.draw(spriteBatch, String.valueOf(quantityLeft), quantityX - HUD_QUANTITY_PADDING_LEFT, HUD_MARGINS + HUD_QUANTITY_PADDING_BOTTOM);
            }
        }

        //DRAW LEVEL NUMBER
        font.draw(spriteBatch, "Level "+ (world.getCurrentLevel() + 1), HUD_MARGINS, height - HUD_LEVEL_TOP_MARGIN);

    }

    public void dispose(){
        cursorPixmap.dispose();
    }
}
