package engine.lua.type.object.insts;

import org.luaj.vm2.LuaValue;
import engine.lua.type.object.Instance;
import engine.lua.type.object.TreeViewable;
import ide.layout.windows.icons.Icons;

public class BoneTree extends Instance implements TreeViewable {

	public BoneTree() {
		super("BoneTree");
		
		this.setLocked(true);
		this.setInstanceable(false);

		this.getField("Archivable").setLocked(true);
	}

	@Override
	protected LuaValue onValueSet(LuaValue key, LuaValue value) {
		return value;	
	}

	@Override
	protected boolean onValueGet(LuaValue key) {
		return true;
	}

	@Override
	public void onDestroy() {
		//
	}

	@Override
	public Icons getIcon() {
		return Icons.icon_animation_data;
	}
}
