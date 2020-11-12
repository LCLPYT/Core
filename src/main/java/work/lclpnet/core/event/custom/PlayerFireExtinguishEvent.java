package work.lclpnet.core.event.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Called, when a player tries to extinguish a fire.
 */
@Cancelable
public class PlayerFireExtinguishEvent extends PlayerEvent{

	protected BlockPos fireBlock, punchedBlock;
	protected Direction clickedBlockFace;
	
	public PlayerFireExtinguishEvent(PlayerEntity player, BlockPos fireBlock, BlockPos punchedBlock, Direction blockFace) {
		super(player);
		this.fireBlock = fireBlock;
		this.punchedBlock = punchedBlock;
		this.clickedBlockFace = blockFace;
	}
	
	public BlockPos getFireBlock() {
		return fireBlock;
	}
	
	public BlockPos getPunchedBlock() {
		return punchedBlock;
	}
	
	public Direction getClickedBlockFace() {
		return clickedBlockFace;
	}

}
