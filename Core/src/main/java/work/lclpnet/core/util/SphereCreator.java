package work.lclpnet.core.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class SphereCreator {

	public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {
		List<BlockPos> sphereBlocks = new ArrayList<>();
		int bx = centerBlock.getX(),
				by = centerBlock.getY(),
				bz = centerBlock.getZ();

		for(int x = bx - radius; x <= bx + radius; x++) {
			for(int y = by - radius; y <= by + radius; y++) {
				for(int z = bz - radius; z <= bz + radius; z++) {
					double distance = ((bx-x)*(bx-x)+((bz-z)*(bz-z))+((by-y)*(by-y)));

					if(distance < radius * radius && !(hollow && distance < ((radius - 1)*(radius - 1)))) {
						BlockPos l = new BlockPos(x, y, z);
						sphereBlocks.add(l);
					}
				}	
			}
		}

		return sphereBlocks;
	}
	
	public static List<BlockPos> generateCircle(BlockPos centerBlock, int radius, boolean hollow) {
		List<BlockPos> circleBlocks = new ArrayList<>();
		int bx = centerBlock.getX(),
				by = centerBlock.getY(),
				bz = centerBlock.getZ();

		for(int x = bx - radius; x <= bx + radius; x++) {
			for(int z = bz - radius; z <= bz + radius; z++) {
				double distance = ((bx-x)*(bx-x)+((bz-z)*(bz-z)));

				if(distance < (radius * radius) && !(hollow && distance < ((radius - 1)*(radius - 1)))) {
					BlockPos l = new BlockPos(x, by, z);
					circleBlocks.add(l);
				}
			}	
		}

		return circleBlocks;
	}

}
