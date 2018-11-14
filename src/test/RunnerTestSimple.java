package test;

import org.luaj.vm2.LuaValue;
import org.lwjgl.glfw.GLFW;

import engine.Game;
import ide.RunnerClient;
import luaengine.RunnableArgs;
import luaengine.type.data.Color3;
import luaengine.type.data.Vector3;
import luaengine.type.object.insts.GameObject;
import luaengine.type.object.insts.Material;
import luaengine.type.object.insts.Mesh;
import luaengine.type.object.insts.Prefab;
import luaengine.type.object.insts.Texture;

public class RunnerTestSimple extends RunnerClient {
	
	@Override
	public void loadScene(String[] args) {
		
		// Set ambient
		Game.lighting().setAmbient(Color3.newInstance(64, 64, 64));
		
		// Make a sphere
		Mesh mesh = Game.assets().newMesh();
		mesh.setFilePath("Resources/Testing/Sphere.mesh");
		
		// MATERIAL PATH
		String materialPath = "Resources/Testing/PBR/iron/";
		
		// Textures
		Texture texture1 = Game.assets().newTexture();
		texture1.setFilePath( materialPath + "albedo.png");
		texture1.setSRGB(true); // Albedo texture needs this to look correct
		
		Texture texture2 = Game.assets().newTexture();
		texture2.setFilePath( materialPath + "normal.png");
		
		Texture texture3 = Game.assets().newTexture();
		texture3.setFilePath( materialPath + "roughness.png");
		
		Texture texture4 = Game.assets().newTexture();
		texture4.setFilePath( materialPath + "metallic.png");
		
		// Base material
		Material material = Game.assets().newMaterial();
		material.setDiffuseMap(texture1);
		material.setNormalMap(texture2);
		material.setRoughMap(texture3);
		material.setMetalMap(texture4);
		material.setRoughness(1.0f);
		material.setMetalness(1.0f);
		material.setReflective(0.1f);
		
		// Create prefab
		Prefab p = Game.assets().newPrefab();
		p.setName("Prefab0");
		p.addModel(mesh, material);
		
		// Create game object in the world with prefab
		GameObject obj = new GameObject();
		obj.setPrefab(p);
		obj.setParent(Game.workspace());
		
		// Camera controller
		Game.runService().getRenderSteppedEvent().connect( new RunnableArgs() {
			final int CAMERA_DIST = 2;
			double t = Math.PI/2f;
			
			@Override
			public void run(LuaValue[] args) {
				double delta = args[0].todouble();
				
				// Get direction value
				int d = 0;
				if ( Game.userInputService().isKeyDown(GLFW.GLFW_KEY_E) )
					d++;
				if ( Game.userInputService().isKeyDown(GLFW.GLFW_KEY_Q) )
					d--;
				t += d*delta;
				
				// Rotate the camera based on the direction
				float xx = (float) (Math.cos(t) * CAMERA_DIST);
				float yy = (float) (Math.sin(t) * CAMERA_DIST);

				Game.workspace().getCurrentCamera().setPosition(Vector3.newInstance(xx,yy,CAMERA_DIST*0.75f));
				Game.workspace().getCurrentCamera().setLookAt(Vector3.newInstance(0, 0, 0));
			}
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}