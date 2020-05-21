package work.lclpnet.core.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockLocation extends BlockPos{

	private final World world;
	
	public BlockLocation(World worldIn, Vec3d vecIn) {
		super(vecIn);
		this.world = worldIn;
	}
	
	public BlockLocation(World worldIn, int x, int y, int z) {
		super(x, y, z);
		this.world = worldIn;
	}
	
	public World getWorld() {
		return world;
	}
	
}
