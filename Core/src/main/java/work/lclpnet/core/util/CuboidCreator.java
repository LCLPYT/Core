package work.lclpnet.core.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class CuboidCreator {

	public static List<BlockPos> generateCuboid(BlockPos first, BlockPos second, boolean hollow) {
		List<BlockPos> cuboidBlocks = new ArrayList<>();
		final int ax = Math.min(first.getX(), second.getX()),
				ay = Math.min(first.getY(), second.getY()),
				az = Math.min(first.getZ(), second.getZ()),
				bx = Math.max(first.getX(), second.getX()),
				by = Math.max(first.getY(), second.getY()),
				bz = Math.max(first.getZ(), second.getZ());

		for (int x = ax; x <= bx; x++) {
			for (int y = ay; y <= by; y++) {
				for (int z = az; z <= bz; z++) {
					BlockPos l = new BlockPos(x, y, z);
					if (!hollow) cuboidBlocks.add(l);
					else if (x == ax || 
							y == ay || 
							z == az || 
							x == bx || 
							y == by || 
							z == bz) {
						cuboidBlocks.add(l);
					}
				}
			}	
		}

		return cuboidBlocks;
	}

	public static List<BlockPos> generatePlatform(BlockPos first, BlockPos second, boolean hollow) {
		List<BlockPos> cuboidBlocks = new ArrayList<>();
		final int ax = Math.min(first.getX(), second.getX()),
				az = Math.min(first.getZ(), second.getZ()),
				bx = Math.max(first.getX(), second.getX()),
				bz = Math.max(first.getZ(), second.getZ());

		for (int x = ax; x <= bx; x++) {
			for (int z = az; z <= bz; z++) {
				BlockPos l = new BlockPos(x, first.getY(), z);
				if (!hollow) cuboidBlocks.add(l);
				else if (x == ax || 
						z == az || 
						x == bx || 
						z == bz) {
					cuboidBlocks.add(l);
				}
			}
		}

		return cuboidBlocks;
	}
	
	public static List<BlockPos> generateCuboid(BlockPos loc, int sizex, int sizey, int sizez, boolean hollow) {
		return generateCuboid(loc.add(-sizex, -sizey, -sizez), loc.add(sizex, sizey, sizez), hollow);
	}

	public static List<BlockPos> generatePlatform(BlockPos loc, int sizex, int sizez, boolean hollow) {
		return generatePlatform(loc.add(-sizex, 0, -sizez), loc.add(sizex, 0, sizez), hollow);
	}
	
}
