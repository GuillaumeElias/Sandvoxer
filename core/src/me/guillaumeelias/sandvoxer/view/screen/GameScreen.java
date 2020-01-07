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
import me.guillaumeelias.sandvoxer.model.PlayerHUD;
import me.guillaumeelias.sandvoxer.model.World;
import me.guillaumeelias.sandvoxer.view.CharacterManager;
import me.guillaumeelias.sandvoxer.view.DialogRenderer;
import me.guillaumeelias.sandvoxer.view.VoxelType;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    public static final int HUD_MARGIN_RIGHT = 20;
    public static final int HUD_MARGIN_BOTTOM = 20;
    public static final int HUD_FONT_MARGIN_BOTTOM = 30;
    public static final int HUD_FONT_MARGIN_LEFT = 3;


    private Environment environment;
    private PerspectiveCamera cam;

    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;
    private BitmapFont font;

    public InputManager inputManager;
    Pixmap pixmap;
    Texture pixmapTexture;

    //List<Model> debugModels;
    List<ModelInstance> debugInstances = new ArrayList<>();

    private Sandvoxer sandvoxer;

    World world;
    Player player;
    PlayerHUD playerHUD;
    CharacterManager characterManager;

    public GameScreen(Sandvoxer sandvoxer){
        this.sandvoxer = sandvoxer;

        this.font = new BitmapFont(Gdx.files.internal("skin/font_pro_font_windows_20pt.fnt"), false);
        DialogRenderer.instance.initialize(font);

        //INITIALIZE LIGHT
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0, -10, 0));

        //INITIALIZE CAMERA
        cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam.near = 1.f;
        cam.far = 1000.f;
        cam.update();

        //INITIALIZE DATA
        characterManager = new CharacterManager();
        world = new World(characterManager, this);
        playerHUD = new PlayerHUD();
        player = new Player(world, cam, playerHUD);
        world.setPlayer(player);

        //INITIALIZE MODELS
        modelBatch = new ModelBatch();

        //SET UP INPUTS
        inputManager = new InputManager(cam, player, world, sandvoxer);

        spriteBatch = new SpriteBatch();

        //INITIALIZE SPRITES
        //cursor
        pixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, 0, 4, 4);
        pixmapTexture = new Texture(pixmap, Pixmap.Format.RGB888, false);
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

        //render
        renderModels();
        renderSprites(width, height, deltaTime);

        if(player.isDead()){
            inputManager.update(deltaTime);
            player.birth();
            cam.up.set(Vector3.Y);
            world.restoreSpawnBlockIfDestructed();
            //TODO show message
        }
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

        //draw cursor
        spriteBatch.draw(pixmapTexture, width / 2, height / 2);

        //draw selected voxel type
        VoxelType selectedVoxelType = playerHUD.getSelectedVoxelType();

        if(selectedVoxelType != null){
            Texture texture = selectedVoxelType.getTexture();

            float hudX = width - texture.getWidth() - HUD_MARGIN_RIGHT;
            spriteBatch.draw(texture, hudX, HUD_MARGIN_BOTTOM);
            font.draw(spriteBatch, selectedVoxelType.getName(), hudX + HUD_FONT_MARGIN_LEFT, HUD_MARGIN_BOTTOM + texture.getHeight() + HUD_FONT_MARGIN_BOTTOM);
        }

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
    public void dispose () {
        modelBatch.dispose();
        pixmap.dispose();
    }
}
