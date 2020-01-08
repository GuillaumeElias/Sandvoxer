package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.guillaumeelias.sandvoxer.Sandvoxer;
import me.guillaumeelias.sandvoxer.model.Dialog;
import me.guillaumeelias.sandvoxer.util.Utils;

public class DialogRenderer{

    public static final DialogRenderer instance = new DialogRenderer();

    private static final int TEXT_BACKGROUND_HEIGHT = 30;
    private static final int TEXT_BACKGROUND_MARGINS = 50;
    private static final int FONT_LINE_PADDING = 2;

    private static final double LETTER_TIME = 0.05; //seconds
    private static final double SENTENCE_END_TIME = 1; //must be greater than LETTER_TIME

    private Dialog currentDialog;
    private BitmapFont font;

    private int currentLine;
    private int currentLetter;
    private int currentNbOfLetters;
    private Texture background;

    private int dialogWidth;
    private float fontHeight;
    private float marginYTextWithinBackground;

    private double _timer;
    private float lineLeftMargin;

    private DialogRenderer(){
        currentDialog = null;
        currentLine = -1;
    }

    public void initialize(BitmapFont font){

        this.font = font;
        this.fontHeight = font.getLineHeight() - FONT_LINE_PADDING;

        Gdx.app.log("font", "font height"+fontHeight);
        this.marginYTextWithinBackground = (TEXT_BACKGROUND_HEIGHT - fontHeight) / 2;

        this.dialogWidth = Gdx.graphics.getWidth() - TEXT_BACKGROUND_MARGINS * 2;

        background = createBackground();
    }

    private Texture createBackground() {
        Pixmap backgroundPixmap = Utils.getPixmapRoundedRectangle(dialogWidth, TEXT_BACKGROUND_HEIGHT, 3, Sandvoxer.BACKGROUND_COLOR);
        return new Texture(backgroundPixmap);
    }

    public void setCurrentDialog(Dialog currentDialog) {
        this.currentDialog = currentDialog;
        this.currentLine = -1;
        goToNextLine();
    }

    public void stopCurrentDialog(){
        this.currentDialog = null;
        currentLine = -1;
    }

    public Dialog getCurrentDialog() {
        return currentDialog;
    }

    /**
     * @return true if there is a next line false if there isn't
     */
    private boolean goToNextLine(){

        currentLine++;
        currentLetter = 0;
        _timer = 0;

        if(currentLine >= currentDialog.getNumberOfLines()){
            currentDialog.setPlayed(true);
            currentDialog = null;
            currentLine = -1;
            return false;
        }

        String lineText = currentDialog.getLine(currentLine);

        currentNbOfLetters = lineText.length();

        GlyphLayout layout = new GlyphLayout(font, lineText);
        float lineWidth = layout.width;
        lineLeftMargin = (dialogWidth - lineWidth) / 2;

        return true;
    }


    public void render(SpriteBatch batch, float deltaTime){

        if(currentDialog == null){
            return;
        }

        _timer += deltaTime;
        if(_timer > LETTER_TIME){

            if(currentLetter >= currentNbOfLetters){
                if(_timer > LETTER_TIME + SENTENCE_END_TIME){
                    if(goToNextLine() == false){
                        return;
                    }
                }

            }else{
                _timer = 0;
                currentLetter++;
            }
        }

        batch.draw(background, TEXT_BACKGROUND_MARGINS, currentDialog.getBottomMargin());

        String text = currentDialog.getLine(currentLine).substring(0, currentLetter);

        font.draw(batch, text, TEXT_BACKGROUND_MARGINS + lineLeftMargin, currentDialog.getBottomMargin() + marginYTextWithinBackground + fontHeight);
    }

}
