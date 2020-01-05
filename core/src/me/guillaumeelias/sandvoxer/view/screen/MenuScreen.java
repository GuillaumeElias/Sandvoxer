package me.guillaumeelias.sandvoxer.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.guillaumeelias.sandvoxer.Sandvoxer;

public class MenuScreen implements Screen {

    private SpriteBatch batch;
    OrthographicCamera camera;
    private Viewport viewport;
    Stage stage;
    Skin skin;
    private Sandvoxer sandvoxer;

    boolean firstTimeShowing = true;

    public MenuScreen(Sandvoxer sandvoxer) {
        this.sandvoxer = sandvoxer;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage = new Stage();

        //Start game button
        final TextButton startButton = createButton(firstTimeShowing ? "Start game" : "Resume");
        startButton.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                MenuScreen.this.sandvoxer.switchToGameScreen();
            }
        });

        //About button
        final TextButton aboutButton = createButton("About");
        aboutButton.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 30f);
        aboutButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                startButton.setText("Timmy O'Toole");   //TODO show credits
            }
        });

        //About button
        final TextButton exitButton = createButton("Exit");
        exitButton.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 50f);
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.exit(0);
            }
        });

        stage.addActor(startButton);
        stage.addActor(aboutButton);
        stage.addActor(exitButton);

        if(!firstTimeShowing){
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(false);

        firstTimeShowing = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        skin.dispose();
    }

    private TextButton createButton(String text){
        TextButton button = new TextButton(text, skin, "default");

        button.setWidth(200f);
        button.setHeight(20f);
        return button;
    }
}
