

import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import edu.berlin.htw.ds.cg.helper.GLDrawHelper;
import edu.berlin.htw.ds.cg.helper.TextureReader;

public class Fork implements InteractiveItem{
	
	private boolean sphere = false;
	
    private float pitch;
    private float pitchPos = 0;
    private float pitchDirection;
    
    private float yaw;
    private float yawPos = 0;
    private float yawDirection;
    
    private float height;
    private float width;
    
    private float maxAngle = 30;
    private float speed = 0.005f;
    
    int x;
    int y;
    
    private Fork leftChild;
    private Fork rightChild;
    
    int textureID;
	TextureReader.Texture texture = null;


    public Fork(float pitch, float yaw, float height, float width, Fork leftChild, Fork rightChild) {
        this.pitch  = pitch;
        this.yaw    = yaw;
        this.height = height;
        this.width  = width;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.pitchDirection = new Random().nextFloat();
        this.yawDirection = new Random().nextFloat();
    }
    
    public Fork() {
        sphere = true;
        setup();
    }

    
    @Override
    public void render() {
    	update();
    	
//    	GL11.glDisable(GL11.GL_DEPTH_TEST);
    	
    	/*
    	 * adapted from Aaron Scheu's code
    	 */

    			GL11.glPushMatrix();
    			if (sphere) {
    				
    				GL11.glEnable(GL11.GL_TEXTURE_2D);
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    				
    				GLDrawHelper.drawSphere(100, 24, 24); 
    				
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    				GL11.glDisable(GL11.GL_TEXTURE_2D);
    			} else {

    				GL11.glLineWidth(3);
    				
    				// vertical
    	            GL11.glBegin(GL11.GL_LINES);
    	            GL11.glVertex2f(0, 0);
    	            GL11.glVertex2f(0, -height);
    	            GL11.glEnd();

    	            GL11.glTranslatef(0, -height, 0);
    	            GL11.glRotatef(yawPos, 0, 1, 0);

    	            // horizontal
    	            GL11.glBegin(GL11.GL_LINES);
    	            GL11.glVertex2f(-width, pitchPos);
    	            GL11.glVertex2f(width, -pitchPos);
    	            GL11.glEnd();

    			}
    			
    			GL11.glTranslatef(-width, pitchPos, 0);
    	        if (leftChild  != null) leftChild.render();
    	        GL11.glTranslatef(2 * width, 2 * -pitchPos,0);
    	        if (rightChild != null) rightChild.render();

    	        GL11.glPopMatrix();
        
    }
    
	@Override
	public void setup() {

		String file = GLDrawHelper.getRandomTexturePath("../CGSS15Ex3MobileDS/dataEx3/Textures");
		try {
			texture = TextureReader.readTexture(file, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
			textureID = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL11.GL_RGB,
					GL11.GL_UNSIGNED_BYTE, texture.getPixels());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	@Override
	public void update() {
	
		if(pitchDirection < 0.5) {
			pitchDirection = -1;
		} else { 
			pitchDirection = 1;
		}
		if(pitchPos <  -maxAngle) {
			pitchDirection = 1;
		} else if(pitchPos > maxAngle) {
			pitchDirection = -1;
		} 
		

		if(pitchPos <  -maxAngle * 3) {
			yawDirection = 1;
		} else if(pitchPos > maxAngle * 3) {
			yawDirection = -1;
		}
		
		pitchPos += pitch * pitchDirection * speed;
		yawPos += yaw * yawDirection * speed * 0.5;
		
	}
	
	@Override
	public void finish() {
	}
	


}
