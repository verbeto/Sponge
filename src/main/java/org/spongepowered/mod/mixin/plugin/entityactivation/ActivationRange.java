package org.spongepowered.mod.mixin.plugin.entityactivation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.mod.configuration.SpongeConfig;
import org.spongepowered.mod.interfaces.IMixinEntity;
import org.spongepowered.mod.interfaces.IMixinWorld;

import java.util.ArrayList;
import java.util.Iterator;

public class ActivationRange {

    static AxisAlignedBB maxBB = AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);
    static AxisAlignedBB miscBB = AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);
    static AxisAlignedBB animalBB = AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);
    static AxisAlignedBB monsterBB = AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);

    /**
     * Initializes an entities type on construction to specify what group this
     * entity is in for activation ranges.
     * 
     * @param entity
     * @return group id
     */
    public static byte initializeEntityActivationType(Entity entity) {

        // account for entities that dont extend EntityMob, EntityAmbientCreature, EntityCreature
        if (entity instanceof EntityMob || entity instanceof EntitySlime || entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
            return 1; // Monster
        } else if (entity instanceof EntityCreature || entity instanceof EntityAmbientCreature
                || entity.isCreatureType(EnumCreatureType.CREATURE, false)
                || entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false) || entity.isCreatureType(EnumCreatureType.AMBIENT, false)) {
            return 2; // Animal
        } else {
            return 3; // Misc
        }
    }

    /**
     * These entities are excluded from Activation range checks.
     * 
     * @param entity
     * @param world
     * @return boolean If it should always tick.
     */
    public static boolean initializeEntityActivationState(Entity entity) {
        if (entity.worldObj.isRemote) {
            return true;
        }
        SpongeConfig config = ((IMixinWorld) entity.worldObj).getWorldConfig();

        if ((((IMixinEntity) entity).getActivationType() == 3 && config.getRootNode().getNode("entity-activation", "misc-activation-range").getInt() == 0)
                || (((IMixinEntity) entity).getActivationType() == 2 && config.getRootNode().getNode("entity-activation", "animal-activation-range")
                        .getInt() == 0)
                || (((IMixinEntity) entity).getActivationType() == 1 && config.getRootNode().getNode("entity-activation", "monster-activation-range")
                        .getInt() == 0)
                || (entity instanceof EntityPlayer && !(entity instanceof FakePlayer))
                || entity instanceof EntityThrowable
                || entity instanceof EntityDragon
                || entity instanceof EntityDragonPart
                || entity instanceof EntityWither
                || entity instanceof EntityFireball
                || entity instanceof EntityWeatherEffect
                || entity instanceof EntityTNTPrimed
                || entity instanceof EntityEnderCrystal
                || entity instanceof EntityFireworkRocket
                || entity instanceof EntityVillager
                // force ticks for entities with superclass of Entity and not a creature/monster
                || (entity.getClass().getSuperclass() == Entity.class && !entity.isCreatureType(EnumCreatureType.CREATURE, false)
                        && !entity.isCreatureType(EnumCreatureType.AMBIENT, false) && !entity.isCreatureType(EnumCreatureType.MONSTER, false)
                        && !entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false))) {
            return true;
        }

        return false;
    }

    /**
     * Find what entities are in range of the players in the world and set
     * active if in range.
     * 
     * @param world
     */
    @SuppressWarnings("unchecked")
    public static void activateEntities(World world) {
        final int miscActivationRange =
                ((IMixinWorld) world).getWorldConfig().getRootNode().getNode("entity-activation", "misc-activation-range").getInt();
        final int animalActivationRange =
                ((IMixinWorld) world).getWorldConfig().getRootNode().getNode("entity-activation", "animal-activation-range").getInt();
        final int monsterActivationRange =
                ((IMixinWorld) world).getWorldConfig().getRootNode().getNode("entity-activation", "monster-activation-range").getInt();

        int maxRange = Math.max(monsterActivationRange, animalActivationRange);
        maxRange = Math.max(maxRange, miscActivationRange);
        maxRange = Math.min((6 << 4) - 8, maxRange);

        for (Entity player : new ArrayList<Entity>(world.playerEntities)) {

            ((IMixinEntity) player).setActivatedTick(world.getWorldInfo().getWorldTotalTime());
            maxBB = player.getEntityBoundingBox().expand(maxRange, 256, maxRange);
            miscBB = player.getEntityBoundingBox().expand(miscActivationRange, 256, miscActivationRange);
            animalBB = player.getEntityBoundingBox().expand(animalActivationRange, 256, animalActivationRange);
            monsterBB = player.getEntityBoundingBox().expand(monsterActivationRange, 256, monsterActivationRange);

            int i = MathHelper.floor_double(maxBB.minX / 16.0D);
            int j = MathHelper.floor_double(maxBB.maxX / 16.0D);
            int k = MathHelper.floor_double(maxBB.minZ / 16.0D);
            int l = MathHelper.floor_double(maxBB.maxZ / 16.0D);

            for (int i1 = i; i1 <= j; ++i1) {
                for (int j1 = k; j1 <= l; ++j1) {
                    WorldServer worldserver = (WorldServer) world;
                    if (worldserver.theChunkProviderServer.chunkExists(i1, j1)) {
                        activateChunkEntities(world.getChunkFromChunkCoords(i1, j1));
                    }
                }
            }
        }
    }

    /**
     * Checks for the activation state of all entities in this chunk.
     * 
     * @param chunk
     */
    @SuppressWarnings("rawtypes")
    private static void activateChunkEntities(Chunk chunk) {
        for (int i = 0; i < chunk.getEntityLists().length; ++i) {
            Iterator iterator = chunk.getEntityLists()[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();
                if (entity.worldObj.getWorldInfo().getWorldTotalTime() > ((IMixinEntity) entity).getActivatedTick()) {
                    if (((IMixinEntity) entity).getDefaultActivationState()) {
                        ((IMixinEntity) entity).setActivatedTick(entity.worldObj.getWorldInfo().getWorldTotalTime());
                        continue;
                    }
                    switch (((IMixinEntity) entity).getActivationType()) {
                        case 1:
                            if (monsterBB.intersectsWith(entity.getEntityBoundingBox())) {
                                ((IMixinEntity) entity).setActivatedTick(entity.worldObj.getWorldInfo().getWorldTotalTime());
                            }
                            break;
                        case 2:
                            if (animalBB.intersectsWith(entity.getEntityBoundingBox())) {
                                ((IMixinEntity) entity).setActivatedTick(entity.worldObj.getWorldInfo().getWorldTotalTime());
                            }
                            break;
                        case 3:
                        default:
                            if (miscBB.intersectsWith(entity.getEntityBoundingBox())) {
                                ((IMixinEntity) entity).setActivatedTick(entity.worldObj.getWorldInfo().getWorldTotalTime());
                            }
                    }
                }
            }
        }
    }

    /**
     * If an entity is not in range, do some more checks to see if we should
     * give it a shot.
     * 
     * @param entity
     * @return
     */
    public static boolean checkEntityImmunities(Entity entity) {
        return false;
    }

    /**
     * Checks if the entity is active for this tick.
     * 
     * @param entity
     * @return
     */
    public static boolean checkIfActive(Entity entity) {
        if (entity.worldObj.isRemote) {
            return true;
        }

        IMixinEntity spongeEntity = (IMixinEntity) entity;
        boolean isActive =
                spongeEntity.getActivatedTick() >= entity.worldObj.getWorldInfo().getWorldTotalTime() || spongeEntity.getDefaultActivationState();

        // Should this entity tick?
        if (!isActive) {
            if ((entity.worldObj.getWorldInfo().getWorldTotalTime() - spongeEntity.getActivatedTick() - 1) % 20 == 0) {
                // Check immunities every 20 ticks.
                if (checkEntityImmunities(entity)) {
                    // Triggered some sort of immunity, give 20 full ticks before we check again.
                    spongeEntity.setActivatedTick(entity.worldObj.getWorldInfo().getWorldTotalTime() + 20);
                }
                isActive = true;
            }
            // Add a little performance juice to active entities. Skip 1/4 if not immune.
        } else if (!spongeEntity.getDefaultActivationState() && entity.ticksExisted % 4 == 0 && !checkEntityImmunities(entity)) {
            isActive = false;
        }

        // Make sure not on edge of unloaded chunk
        int x = net.minecraft.util.MathHelper.floor_double(entity.posX);
        int z = net.minecraft.util.MathHelper.floor_double(entity.posZ);
        if (isActive && !entity.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) {
            isActive = false;
        }

        return isActive;
    }
}
