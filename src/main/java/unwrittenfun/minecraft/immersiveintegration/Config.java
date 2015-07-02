package unwrittenfun.minecraft.immersiveintegration;

import net.minecraftforge.common.config.Configuration;

/**
 * @author Caitlyn.Mainer@pc-logix.com
 *
 */

public class Config {
	public final int fluixWireRange;
	public final int denseWireRange;

	public Config(Configuration config) {
		config.load();
		fluixWireRange = config.getInt("fluixWireRange", "options", 16, 16, 64, "The maximum range of Fluix Wires before needing a support");
		denseWireRange = config.getInt("denseWireRange", "options", 8, 8, 64, "The maximum range of Dense Wires before needing a support");
		if (config.hasChanged()) {
			config.save();
		}
	}
}
