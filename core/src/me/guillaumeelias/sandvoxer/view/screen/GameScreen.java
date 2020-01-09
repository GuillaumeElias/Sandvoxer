package me.guillaumeelias.sandvoxer.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import me.guillaumeelias.sandvoxer.Sandvoxer;
import me.guillaumeelias.sandvoxer.controller.InputManager;
import me.guillaumeelias.sandvoxer.model.Item;
import me.guillaumeelias.sandvoxer.model.Player;
import me.guillaumeelias.sandvoxer.model.World;
import me.guillaumeelias.sandvoxer.view.CharacterManager;
import me.guillaumeelias.sandvoxer.view.EnvironmentCubemap;
import me.guillaumeelias.sandvoxer.view.renderer.DialogRenderer;
import me.guillaumeelias.sandvoxer.view.renderer.PlayerHUDRenderer;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private static GameScreen instance; //TODO avoid pseudo-singletons

    private Environment environment;
    private PerspectiveCamera cam;

    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;
    private BitmapFont font;

    public InputManager inputManager;

    //List<Model> debugModels;
    List<ModelInstance> debugInstances = new ArrayList<>();

    private Sandvoxer sandvoxer;

    World world;
    Player player;
    PlayerHUDRenderer playerHUDRenderer;
    CharacterManager characterManager;

    boolean levelFinished;

    EnvironmentCubemap cubemap;

    public GameScreen(Sandvoxer sandvoxer){
        instance = this;
        this.sandvoxer = sandvoxer;

        levelFinished = false;

        this.font = new BitmapFont(Gdx.files.internal("skin/font_pro_font_windows_20pt.fnt"), false);
        DialogRenderer.instance.initialize(font);

        //INITIALIZE LIGHT
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0, -10, 0));

        //INITIALIZE CAMERA
        cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam.near = 1.f;
        cam.far = 1200.f;
        cam.update();

        //INITIALIZE DATA
        player = new Player(cam);
        world = new World(player);
        player.setWorld(world);
        characterManager = world.getCharacterManager();
        playerHUDRenderer = new PlayerHUDRenderer(player.getPlayerHUD(), world, font);

        //INITIALIZE MODELS
        modelBatch = new ModelBatch();

        //SET UP INPUTS
        inputManager = new InputManager(cam, player, world, sandvoxer);

        spriteBatch = new SpriteBatch();

        //INITIALIZE SPRITES
        playerHUDRenderer.initialize();

        Pixmap skyboxPixmap = new Pixmap(Gdx.files.internal("textures/skybox/skybox.jpg"));

        cubemap = new EnvironmentCubemap(
                skyboxPixmap,
                skyboxPixmap,
                new Pixmap(Gdx.files.internal("textures/skybox/skyboxTop.jpg")),
                new Pixmap(Gdx.files.internal("textures/skybox/skyboxBottom.jpg")),
                skyboxPixmap,
                skyboxPixmap
        );
    }

    @Override
    public void show() {
        Gdx.input.setCursorPosition(0, 0);
        inputManager.clearMouse();

        Gdx.input.setInputProcessor(inputManager);
        Gdx.input.setCursorCatched(true);

        Gdx.gl.glEnable(Gdx.gl20.GL_DEPTH_TEST);
        Gdx.gl20.glDepthFunc(Gdx.gl20.GL_LEQUAL);

        /*createDebugVector(new Vector3(characterManager.getCharacterList().get(0).getBoundingBox().getCenterX(),
                characterManager.getCharacterList().get(0).getBoundingBox().getCenterY(),
                characterManager.getCharacterList().get(0).getBoundingBox().getCenterZ()),
                characterManager.getCharacterList().get(0).getPosition()
        );
        createDebugBox(characterManager.getCharacterList().get(0).getBoundingBox());

        for (int xi = 0; xi < World.GRID_SIZE; xi += 1) {
            for (int yi = 0; yi < World.GRID_SIZE; yi += 1) {
                for (int zi = 0; zi < World.GRID_SIZE; zi += 1) {
                    Voxel cube = world.getCube(xi,yi,zi);
                    if(cube == null)continue;
                    createDebugBox(cube.getBoundingBox());
                }
            }
        }*/

    }

    public void createDebugVector (Vector3 from, Vector3 to) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        MeshPartBuilder builder = modelBuilder.part("axes", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.RED);
        builder.line(from, to);

        builder.sphere(new Matrix4().translate(to).rotate(Vector3.Z, 45).scale(1, 1, 1), 1f, 1f, 1f, 12, 16);
        builder.setColor(Color.GREEN);
        builder.sphere(new Matrix4().translate(from).rotate(Vector3.Z, 45).scale(0.5f, 0.5f, 0.5f), 1f, 1f, 1f, 12, 16);
        Model debugModel = modelBuilder.end();
        debugInstances.add(new ModelInstance(debugModel));
    }

    public void createDebugBox (BoundingBox boundingBox) {
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        builder.node();
        MeshPartBuilder mpb = builder.part("box", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        BoxShapeBuilder.build(mpb, boundingBox.min.x, boundingBox.min.y, boundingBox.min.z, boundingBox.getWidth(),  boundingBox.getHeight(), boundingBox.getDepth());
        Model debugModel = builder.end();

        debugInstances.add(new ModelInstance(debugModel));
    }

    @Override
    public void render (float deltaTime) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        player.gravity(deltaTime);
        animateModels(deltaTime);

        Gdx.gl.glViewport(0, 0, width, height);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //poll inputs and update camera
        inputManager.update(deltaTime);

        cubemap.render(inputManager.getQ());

        //render
        renderModels();
        renderSprites(width, height, deltaTime);

        if(player.isDead()){
            initPlayer();
            world.onPlayerDeath();
        }else if(levelFinished){

            if(world.getCurrentLevel() == 1){ //end of game
                sandvoxer.gameFinished();
                world.setCurrentLevel(0);
            }else{
                world.startNextLevel();
                initPlayer();
                levelFinished = false;
            }
        }
    }

    public void startNextLevel(){
        levelFinished = true;
    }

    public void initPlayer(){
        player.birth();
        cam.up.set(Vector3.Y);
    }

    private void renderModels(){
        modelBatch.begin(cam);
        modelBatch.render(world.getModelInstances(), environment);
        //modelBatch.render(debugInstances, environment); //RENDER DEBUG MODELS
        modelBatch.render(characterManager.getModelInstances(), environment);
        modelBatch.end();
    }


    private void renderSprites(float width, int height, float deltaTime){

        spriteBatch.begin();

        //render dialog if present
        DialogRenderer.instance.render(spriteBatch, deltaTime);

        //render Player HUD
        playerHUDRenderer.render(spriteBatch, width, height);

        spriteBatch.end();
    }

    public void animateModels(float deltaTime){

        characterManager.updateAnimations(deltaTime);

        for(Item item : world.getItems()){
            item.rotate(deltaTime); //TODO migrate to item view
        }
    }

    @Override
    public void resize(int width, int height) {
        if(cam.viewportWidth != width || cam.viewportHeight != height){
            cam.viewportWidth = width;
            cam.viewportHeight = height;

            spriteBatch = new SpriteBatch(); //re-create projection matrix of the sprites

            DialogRenderer.instance.initialize(font); //make dialog renderer aware of width change

            cam.update();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.app.log("resume", "");
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        playerHUDRenderer.dispose();
    }

    public static GameScreen getInstance() {
        return instance;
    }
}
