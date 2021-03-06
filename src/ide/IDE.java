package ide;

import java.io.IOException;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

import engine.Game;
import engine.InternalGameThread;
import engine.InternalRenderThread;
import engine.application.RenderableApplication;
import engine.gl.Pipeline;
import engine.io.Load;
import ide.layout.IdeLayout;
import lwjgui.LWJGUI;
import lwjgui.scene.Window;
import lwjgui.scene.layout.Pane;

public class IDE extends RenderableApplication {
	public static IdeLayout layout;
	public static Window win;
	
	public static final String TITLE = "Anarchy Engine - Build 0.4";
	
	@Override
	public void initialize(String[] args) {
		// Window title
		GLFW.glfwSetWindowTitle(window, TITLE);
		
		// Setup LWJGUI
		win = LWJGUI.initialize(window);
		win.setWindowAutoDraw(false); // We want to draw the main IDE window manually
		win.setWindowAutoClear(false); // We want control of clearing
		
		InternalRenderThread.desiredFPS = 60;
		InternalGameThread.desiredTPS = 60;
		
		// Setup background pane
		Pane background = new Pane();
		background.setBackground(null);
		win.getScene().setRoot(background);
		
		// Redraw window if resized
		GLFW.glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallbackI() {
			@Override
			public void invoke(long handle, int wid, int hei) {
				LWJGUI.render();
				renderThread.forceUpdate();
			}
		});
		
		// Create rendering pipeline
		this.attachRenderable(pipeline = new Pipeline());
		
		// Setup mane IDE layout
		layout = new IdeLayout(background);
		
		// If someone wants to load a game directly
		if ( args != null && args.length > 0 ) {
			
			// Get project args
			String[] tempArgs = new String[Math.max(0, args.length-1)];
			for (int i = 1; i < args.length; i++) {
				tempArgs[i-1] = args[i];
			}
			
			// Tell game we're a client
			if ( args[0].toLowerCase().equals("client") ) {
				game.setServer(false); // Mark this game as a client
			}
			
			// Load project
			if ( tempArgs.length > 0 ) {
				Load.load(tempArgs[0]);
				
				InternalGameThread.runLater(() -> {
					Game.setRunning(true);
				});
			}
		}
	}
	
	@Override
	protected boolean shouldLockMouse() {
		return Game.isLoaded() && layout.getGamePane().isDescendentHovered();
	}
	
	@Override
	protected Vector2f getMouseOffset() {
		return new Vector2f( (float)layout.getGamePane().getX(), (float)layout.getGamePane().getY() );
	}
	
	@Override
	public void render() {
		if ( GLFW.glfwWindowShouldClose(window) )
			return;
		
		// Render UI:
		//   (final 3d scene gets rendered as a component)
		//   (3d scene is attached to render thread)
		LWJGUI.render();
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}
}