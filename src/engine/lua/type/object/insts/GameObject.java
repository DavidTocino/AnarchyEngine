package engine.lua.type.object.insts;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.luaj.vm2.LuaValue;

import engine.gl.shader.BaseShader;
import engine.lua.type.NumberClamp;
import engine.lua.type.data.Matrix4;
import engine.lua.type.data.Vector3;
import engine.lua.type.object.Instance;
import engine.lua.type.object.Positionable;
import engine.lua.type.object.PrefabRenderer;
import engine.lua.type.object.TreeViewable;
import engine.observer.RenderableInstance;
import engine.util.AABBUtil;
import engine.util.Pair;
import ide.layout.windows.icons.Icons;

public class GameObject extends Instance implements RenderableInstance,TreeViewable,Positionable {
	
	public GameObject() {
		super("GameObject");
		
		this.defineField("Prefab", LuaValue.NIL, false);
		this.defineField("WorldMatrix", new Matrix4(), false);
		this.defineField("Position", Vector3.newInstance(0, 0, 0), false );
		this.defineField("Transparency", LuaValue.valueOf(0), false);
		this.getField("Transparency").setClamp(new NumberClamp(0, 1));
	}
	
	public void render(BaseShader shader) {
		if ( this.getParent().isnil() )
			return;
		
		if ( this.get("Prefab").isnil() )
			return;
		
		Prefab luaPrefab = (Prefab) this.rawget("Prefab");
		Matrix4 matrix = (Matrix4) this.rawget("WorldMatrix");
		PrefabRenderer prefab = luaPrefab.getPrefab();
		
		prefab.render(shader, matrix.toJoml());
	}
	
	/**
	 * Set the world matrix for this game object.
	 * @param matrix
	 */
	public void setWorldMatrix(Matrix4 matrix) {
		if ( matrix == null )
			matrix = new Matrix4();
		
		this.set("WorldMatrix", matrix);
	}
	
	/**
	 * Set the world matrix for this game object. Convenience function for joml.
	 * @param matrix
	 */
	public void setWorldMatrix(Matrix4f matrix) {
		this.setWorldMatrix(new Matrix4(matrix));
	}
	
	/**
	 * Get the world matrix for this game object.
	 * @return
	 */
	public Matrix4 getWorldMatrix() {
		return (Matrix4) this.get("WorldMatrix");
	}
	
	/**
	 * If no physics object exists within this game object, it will create one and return it.<br>
	 * If a physics object already exists, it will return the current one.
	 * @return
	 */
	public PhysicsObject attachPhysicsObject() {
		PhysicsObject r = getPhysicsObject();
		
		if ( r == null ) {
			PhysicsObject p = new PhysicsObject();
			p.forceSetParent(this);
			return p;
		} else {
			return r;
		}
	}
	
	/**
	 * Returns the physics object that is currently attached to this game object.
	 * @return
	 */
	public PhysicsObject getPhysicsObject() {
		Instance t = this.findFirstChildOfClass("PhysicsObject");
		return t != null?(PhysicsObject)t:null;
	}
	
	@Override
	public void onDestroy() {
		//
	}
	
	@Override
	protected LuaValue onValueSet(LuaValue key, LuaValue value) {
		if ( key.toString().equals("Position") && value instanceof Vector3 ) {
			Matrix4 mat = ((Matrix4)this.rawget("WorldMatrix"));
			mat.setPosition((Vector3) value);
			
			// This is just to trigger Physics object (if it exists)
			this.set("WorldMatrix", mat);
		}
		if ( key.toString().equals("Prefab") ) {
			if ( !value.isnil() && !(value instanceof Prefab) )
				return null;
		}
		return value;
	}
	
	@Override
	protected boolean onValueGet(LuaValue key) {
		if ( key.toString().equals("Position") ) {
			this.rawset("Position", ((Matrix4)this.rawget("WorldMatrix")).getPosition());
		}
		return true;
	}

	@Override
	public Icons getIcon() {
		return Icons.icon_gameobject;
	}

	/**
	 * Set the prefab used to draw this object. Default is nil.
	 * <br>
	 * <br>
	 * nil will not draw anything.
	 * @param p
	 */
	public void setPrefab(Prefab p) {
		if ( p == null ) {
			this.set("Prefab", LuaValue.NIL);
		} else {
			this.set("Prefab", p);
		}
	}

	/**
	 * Return the current prefab used to draw this object.
	 * @return
	 */
	public Prefab getPrefab() {
		LuaValue p = this.get("Prefab");
		return p.equals(LuaValue.NIL)?null:(Prefab)p;
	}

	/**
	 * Returns the vector3 position of this object.
	 */
	public Vector3 getPosition() {
		return (Vector3) this.get("Position");
	}

	/**
	 * Sets the vector3 position of this object.
	 * If there is a physics object inside this object, that will be updated as well.
	 */
	public void setPosition(Vector3 pos) {
		this.set("Position", pos);
	}

	@Override
	public Pair<Vector3f, Vector3f> getAABB() {
		if ( this.getPrefab() == null ) {
			return AABBUtil.newAABB(getPosition().toJoml(), getPosition().toJoml());
		}
		return this.getPrefab().getAABB();
	}

	/**
	 * Returns the transparency of the object.
	 * @return
	 */
	public float getTransparency() {
		return this.get("Transparency").tofloat();
	}
	
	/**
	 * Sets the transparency of the object.
	 * @param f
	 */
	public void setTransparency(float f) {
		this.set("Transparency", LuaValue.valueOf(f));
	}
}
