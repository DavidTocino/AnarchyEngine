package engine.gl.ibl;

import engine.gl.Pipeline;
import engine.io.Image;
import engine.util.TextureUtils;

public class SkySphereIBL extends SkySphere {
	private SkySphereIBL lightSphere;
	private int buffers;
	private float lightMultiplier = 2f;
	private float power = 3f;
	
	private SkySphereIBL(Image reflection) {
		this(reflection, SKYBOX_TEXTURE_SIZE);
	}
	
	private SkySphereIBL(Image reflection, int size) {
		super(reflection, size);
	}
	
	public void setLightPower( float power ) {
		this.power = power;
	}

	public float getLightPower() {
		return power;
	}
	
	@Override
	public boolean draw(Pipeline pipeline) {
		if ( buffers > 5 )
			return true;
		
		pipeline.shader_reset();
		
		buffers++;
		return super.draw(pipeline);
	}
	
	public void setLightMultiplier(float i) {
		this.lightMultiplier = i;
	}
	

	public float getLightMultiplier() {
		return lightMultiplier;
	}

	public SkySphere getLightSphere() {
		return lightSphere;
	}

	/**
	 * Convenience method to create a SkySphereIBL. For this method to work, your IBL must be structured like this:
	 * <br>
	 * <br>
	 * SkySPhereIBL ibl = SkySphereIBL.create("path/office.hdr");
	 */
	public static SkySphereIBL create(String path) {
		Image refi = TextureUtils.loadImage(path);
		
		return new SkySphereIBL(refi);
	}
}
