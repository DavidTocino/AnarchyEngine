package engine.lua.type.object.services;

import org.luaj.vm2.LuaValue;

import engine.lua.type.LuaEvent;
import engine.lua.type.object.Service;

public class RunService extends Service {

	public RunService() {
		super("RunService");
		
		this.rawset("Heartbeat", new LuaEvent());
		this.rawset("RenderStepped", new LuaEvent());
		this.rawset("PhysicsStepped", new LuaEvent());
		this.setLocked(true);
	}

	@Override
	protected LuaValue onValueSet(LuaValue key, LuaValue value) {
		return value;
	}

	@Override
	protected boolean onValueGet(LuaValue key) {
		return true;
	}
	
	public LuaEvent heartbeatEvent() {
		return (LuaEvent) this.get("Heartbeat");
	}
	
	public LuaEvent renderSteppedEvent() {
		return (LuaEvent) this.get("RenderStepped");
	}
	
	public LuaEvent physicsSteppedEvent() {
		return (LuaEvent) this.get("PhysicsStepped");
	}
}
