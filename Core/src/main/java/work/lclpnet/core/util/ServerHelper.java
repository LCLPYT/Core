package work.lclpnet.core.util;

import java.lang.reflect.Field;

import net.minecraft.server.MinecraftServer;

public class ServerHelper {

	public static boolean isBonusChestEnabled(MinecraftServer server) {
		try {
			Field f = MinecraftServer.class.getDeclaredField("enableBonusChest");
			f.setAccessible(true);
			boolean b = (boolean) f.get(server);
			f.setAccessible(false);
			return b;
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
