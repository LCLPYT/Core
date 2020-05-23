package work.lclpnet.core.util;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;

public class EntityHelper {

	public static void teleport(Entity entityIn, ServerWorld worldIn, double x, double y, double z, float yaw, float pitch) {
		teleport(entityIn, worldIn, x, y, z, EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class), yaw, pitch);
	}

	public static void teleport(Entity entityIn, ServerWorld worldIn, double x, double y, double z, Set<SPlayerPositionLookPacket.Flags> relativeList, float yaw, float pitch) {
		if (entityIn instanceof ServerPlayerEntity) {
			ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
			worldIn.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, entityIn.getEntityId());
			entityIn.stopRiding();
			if (((ServerPlayerEntity) entityIn).isSleeping()) {
				((ServerPlayerEntity)entityIn).wakeUp();
			}

			if (worldIn == entityIn.world) {
				((ServerPlayerEntity) entityIn).connection.setPlayerLocation(x, y, z, yaw, pitch, relativeList);
			} else {
				((ServerPlayerEntity) entityIn).teleport(worldIn, x, y, z, yaw, pitch);
			}

			entityIn.setRotationYawHead(yaw);
		} else {
			float f1 = MathHelper.wrapDegrees(yaw);
			float f = MathHelper.wrapDegrees(pitch);
			f = MathHelper.clamp(f, -90.0F, 90.0F);
			if (worldIn == entityIn.world) {
				entityIn.setLocationAndAngles(x, y, z, f1, f);
				entityIn.setRotationYawHead(f1);
			} else {
				entityIn.detach();
				entityIn.dimension = worldIn.dimension.getType();
				Entity entity = entityIn;
				entityIn = entityIn.getType().create(worldIn);
				if (entityIn == null) {
					return;
				}

				entityIn.copyDataFromOld(entity);
				entityIn.setLocationAndAngles(x, y, z, f1, f);
				entityIn.setRotationYawHead(f1);
				addFromOtherDimension(worldIn, entityIn);
			}
		}

		if (!(entityIn instanceof LivingEntity) || !((LivingEntity)entityIn).isElytraFlying()) {
			entityIn.setMotion(entityIn.getMotion().mul(1.0D, 0.0D, 1.0D));
			entityIn.onGround = true;
		}
	}

	public static void addFromOtherDimension(ServerWorld w, Entity entity) {
		boolean flag = entity.forceSpawn;
		entity.forceSpawn = true;
		w.summonEntity(entity);
		entity.forceSpawn = flag;
		w.chunkCheck(entity);
	}

	public static void teleportToEntity(Collection<? extends Entity> targets, Entity destination) {
		targets.forEach(e -> teleportToEntity(e, destination));
	}

	public static void teleportToEntity(Entity which, Entity destination) {
		teleport(which, (ServerWorld) destination.world, destination.getPosX(), destination.getPosY(), destination.getPosZ(), EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class), destination.rotationYaw, destination.rotationPitch);
	}

}
