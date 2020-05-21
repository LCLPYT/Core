package work.lclpnet.core.util;

import net.minecraft.world.World;

public class LocationRotation extends Location{

	public final float yaw, pitch;
	
	public LocationRotation(Location locIn, float yawIn, float pitchIn) {
		super(locIn);
		this.yaw = yawIn;
		this.pitch = pitchIn;
	}
	
	public LocationRotation(World worldIn, double xIn, double yIn, double zIn, float yawIn, float pitchIn) {
		super(worldIn, xIn, yIn, zIn);
		this.yaw = yawIn;
		this.pitch = pitchIn;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getPitch() {
		return pitch;
	}

}
