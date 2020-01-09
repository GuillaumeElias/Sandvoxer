package me.guillaumeelias.sandvoxer.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Thanks to Nolesh
 */
public class EnvironmentCubemap implements Disposable {

    protected final Pixmap[] data = new Pixmap[6];
    protected ShaderProgram shader;

    Matrix4 worldTrans;
    protected int u_worldTrans;
    protected Mesh quad;

    protected String vertexShader = " attribute vec3 a_position; \n" +
            " attribute vec3 a_normal; \n" +
            " attribute vec2 a_texCoord0; \n" +
            " uniform mat4 u_worldTrans; \n" +
            " varying vec2 v_texCoord0; \n" +
            " varying vec3 v_cubeMapUV; \n" +
            " void main() { \n" +
            "     v_texCoord0 = a_texCoord0;     \n" +
            "     vec4 g_position = u_worldTrans * vec4(a_position, 1.0); \n" +
            "     v_cubeMapUV = normalize(g_position.xyz); \n" +
            "     gl_Position = vec4(a_position, 1.0); \n" +
            " } \n";

    protected String fragmentShader = "#ifdef GL_ES \n" +
            " precision mediump float; \n" +
            " #endif \n" +
            " uniform samplerCube u_environmentCubemap; \n" +
            " varying vec2 v_texCoord0; \n" +
            " varying vec3 v_cubeMapUV; \n" +
            " void main() {      \n" +
            "   gl_FragColor = vec4(textureCube(u_environmentCubemap, v_cubeMapUV).rgb, 1.0);   \n" +
            " } \n";


    public EnvironmentCubemap(Pixmap positiveX, Pixmap negativeX, Pixmap positiveY, Pixmap negativeY, Pixmap positiveZ, Pixmap negativeZ) {
        data[0] = positiveX;
        data[1] = negativeX;

        data[2] = positiveY;
        data[3] = negativeY;

        data[4] = positiveZ;
        data[5] = negativeZ;

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled())
            throw new GdxRuntimeException(shader.getLog());

        u_worldTrans = shader.getUniformLocation("u_worldTrans");

        quad = createQuad();
        worldTrans = new Matrix4();

        init();
    }

    private void init(){
        Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, 0);
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL20.GL_RGB, data[0].getWidth(), data[0].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[0].getPixels());
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL20.GL_RGB, data[1].getWidth(), data[1].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[1].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL20.GL_RGB, data[2].getWidth(), data[2].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[2].getPixels());
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL20.GL_RGB, data[3].getWidth(), data[3].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[3].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL20.GL_RGB, data[4].getWidth(), data[4].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[4].getPixels());
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL20.GL_RGB, data[5].getWidth(), data[5].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[5].getPixels());

        Gdx.gl20.glTexParameteri ( GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MIN_FILTER,GL20.GL_LINEAR_MIPMAP_LINEAR );
        Gdx.gl20.glTexParameteri ( GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MAG_FILTER,GL20.GL_LINEAR );
        Gdx.gl20.glTexParameteri ( GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE );
        Gdx.gl20.glTexParameteri ( GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE );

        Gdx.gl20.glGenerateMipmap(GL20.GL_TEXTURE_CUBE_MAP);
    }

    public void render(Quaternion quaternion) {

        worldTrans.idt();
        worldTrans.rotate(quaternion); //TODO only rotate when camera is moving?

        shader.begin();
        shader.setUniformMatrix(u_worldTrans, worldTrans.translate(0, 0, -1));

        quad.render(shader, GL20.GL_TRIANGLES);
        shader.end();
    }

    public Mesh createQuad() {
        Mesh mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
        mesh.setVertices(new float[]
                {-1f, -1f, 0, 1, 1, 1, 1, 0, 1,
                        1f, -1f, 0, 1, 1, 1, 1, 1, 1,
                        1f, 1f, 0, 1, 1, 1, 1, 1, 0,
                        -1f, 1f, 0, 1, 1, 1, 1, 0, 0});
        mesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});
        return mesh;
    }

    @Override
    public void dispose() {
        shader.dispose();
        quad.dispose();
        for(int i=0; i<6; i++)
            data[i].dispose();
    }
}