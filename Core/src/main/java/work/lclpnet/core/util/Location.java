package work.lclpnet.core.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Location extends Vec3d{

	public final World world;
	
	public Location(Location other) {
		super(other.x, other.y, other.z);
		this.world = other.world;
	}
	
	public Location(World worldIn, double xIn, double yIn, double zIn) {
		super(xIn, yIn, zIn);
		this.world = worldIn;
	}
	
	public World getWorld() {
		return world;
	}
	
	public BlockPos toBlockPos() {
		return new BlockPos(this);
	}
	
	public BlockLocation toBlockLocation() {
		return new BlockLocation(world, this);
	}
	
	public LocationRotation withRotation(float yawIn, float pitchIn) {
		return new LocationRotation(this, yawIn, pitchIn);
	}

}
