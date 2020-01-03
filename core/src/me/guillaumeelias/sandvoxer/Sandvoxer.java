package me.guillaumeelias.sandvoxer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import me.guillaumeelias.sandvoxer.controller.InputManager;
import me.guillaumeelias.sandvoxer.model.Player;
import me.guillaumeelias.sandvoxer.model.World;
import me.guillaumeelias.sandvoxer.view.VoxelModelFactory;

public class Sandvoxer extends ApplicationAdapter {

	public Environment environment;
	public PerspectiveCamera cam;

	public ModelBatch modelBatch;
	public SpriteBatch spriteBatch;

	public me.guillaumeelias.sandvoxer.controller.InputManager inputManager;
	Pixmap pixmap;
	Texture pixmapTexture;

	Model debugModel;
	ModelInstance debugInstance;

	VoxelModelFactory voxelModelFactory;

	World world;
	Player player;

	@Override
	public void create () {

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
		voxelModelFactory = new VoxelModelFactory();
		world = new World(this, voxelModelFactory);
		player = new Player(world, cam);
		world.setPlayer(player);

		//INITIALIZE MODELS
		modelBatch = new ModelBatch();

		createDebugVector(Vector3.Zero, Vector3.Y);

		//SET UP INPUTS
		inputManager = new InputManager(cam, player, world);

		Gdx.input.setCursorPosition(0, 0);
		Gdx.input.setInputProcessor(inputManager);
		Gdx.input.setCursorCatched(true);

		spriteBatch = new SpriteBatch();

		//INITIALIZE SPRITES
		//cursor
		pixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0, 0, 4, 4);
		pixmapTexture = new Texture(pixmap, Pixmap.Format.RGB888, false);
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
		debugModel = modelBuilder.end();
		debugInstance = new ModelInstance(debugModel);
	}

	@Override
	public void render () {

		float deltaTime = Gdx.graphics.getDeltaTime();

		player.gravity(deltaTime);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		inputManager.update(deltaTime);

		//RENDER MODELS
		modelBatch.begin(cam);
		modelBatch.render(world.getModelInstances(), environment);
		modelBatch.render(debugInstance, environment); //RENDER DEBUG MODELS
		modelBatch.end();

		//RENDER SPRITES
		spriteBatch.begin();
		spriteBatch.draw(pixmapTexture, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		spriteBatch.end();
	}


	@Override
	public void dispose () {
		modelBatch.dispose();
		pixmap.dispose();
		voxelModelFactory.disposeAll();
	}
}
