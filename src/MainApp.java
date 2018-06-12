

import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;

import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import edu.berlin.htw.ds.cg.helper.GLDrawHelper;


public class MainApp {
	
	private String title = "Mobile Visualisierung - Metawee Langka";
	
	private boolean endThisApp = false;
	
	private int width = 1024;
	private int height = 640;
	
	private float len = 350;
	private float wid = 200;
	
	FloatBuffer noAmbient = GLDrawHelper.directFloatBuffer(new float[] {0.0f, 0.0f, 0.0f, 1.0f});
	FloatBuffer whiteDiffuse = GLDrawHelper.directFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
	FloatBuffer position = GLDrawHelper.directFloatBuffer(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
	
	private Fork fork;
	
	float[] cameraPos = new float[] {0, height, 2000, 0};
	
	public static void main(String[] args) {
		MainApp app = new MainApp();
		app.run();
	}
	
	private void run() {
		init();
		while(!endThisApp){
			draw();
			update();
		}
		finish();
	}
	
	private void update() {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()){
			endThisApp = true;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			cameraPos[1]--;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			cameraPos[1]++;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			cameraPos[3]++;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			cameraPos[3]--;
		}

	}
	
	private void setupMobile() {
        // Main fork
        fork = new Fork(40, 70, len, wid,
                // Child forks
                new Fork(40, 70, len, wid, new Fork(), new Fork()),
                new Fork(40, 70, len, wid,
                        new Fork(40, 70, len*0.75f, wid*0.75f, new Fork(), new Fork()),
                        new Fork(40, 70, len*0.75f, wid*0.75f, new Fork(), new Fork())));
    }
	
	
	private void init() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setTitle(title);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.f, (float)width/(float)height, 0.1f, 6000.f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL_LINE_SMOOTH);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, noAmbient);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, whiteDiffuse);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		GL11.glColorMaterial ( GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE );
		GL11.glEnable ( GL11.GL_COLOR_MATERIAL );
		
		setupMobile();
	}
	
	
	private void draw() {
		GL11.glClearColor(0.3f, 0.3f, 0.3f, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

		GLU.gluLookAt(cameraPos [0], cameraPos[1], cameraPos[2], cameraPos[3], -height, 0, 0, 1, 0);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
        
        fork.render();
        Display.update();
	}
	
	
	public void finish() {
		fork.finish();
		Display.destroy();
	}

}
