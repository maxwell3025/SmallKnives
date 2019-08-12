package small_knives.entities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import small_knives.items.ItemKnife;
import small_knives.items.ModItems;
import small_knives.misc.ModDamageSources;
import small_knives.network.KnifeLandBlockPacket;
import small_knives.network.KnifeLandEntityPacket;
import small_knives.network.PacketHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntityKnife extends Entity implements IEntityAdditionalSpawnData {
    private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    });
    private static final Map<ItemKnife, Integer> floatTimes = new HashMap<ItemKnife, Integer>() {{
        put(ModItems.wooden_knife, 5);
        put(ModItems.stone_knife, 8);
        put(ModItems.iron_knife, 13);
        put(ModItems.golden_knife, 22);
        put(ModItems.diamond_knife, 20);
    }};
    public int xTile;
    public int yTile;
    public int zTile;
    private Block inTile;
    private int inData;
    public boolean inGround;
    protected int timeInGround;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    public int ticksFloating;
    private double damage;
    private boolean insidePlayer = true;
    public int number;
    private int durability;
    public static int amount = 0;
    public ItemKnife item = ModItems.iron_knife;

    public EntityKnife(World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.damage = 2.0D;
        this.setSize(0.5F, 0.5F);
        amount++;
        number = amount;
        this.inGround = false;
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    public EntityKnife(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityKnife(World worldIn, EntityLivingBase shooter, ItemKnife type, int durability) {
        this(worldIn, shooter.posX, shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
        this.shootingEntity = shooter;
        this.item = type;
        this.ticksFloating = floatTimes.get(type);
        this.durability = durability;
    }

    /**
     * Checks if the entity is in range to render.
     */
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;

        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }
        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    protected void entityInit() {

    }

    public void shoot(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
        //System.out.print("\n" + number + " " + world.isRemote + "\tshoot");
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
        this.motionX += shooter.motionX;
        this.motionZ += shooter.motionZ;

        if (!shooter.onGround) {
            this.motionY += shooter.motionY;
        }
        shootingEntity = shooter;
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
//        System.out.print(String.format("(%.3f,\t%.3f,\t%.3f)\t(%.3f,\t%.3f)\n", x, y, z, yaw, pitch));
        //new Error().printStackTrace();
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
            this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    public void landBlock(double posX, double posY, double posZ, int tileX, int tileY, int tileZ, float rotationYaw, float rotationPitch, boolean hasDurability) {
        if (hasDurability) {
            this.inGround = true;
            this.xTile = tileX;
            this.yTile = tileY;
            this.zTile = tileZ;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.rotationYaw = rotationYaw;
            this.rotationPitch = rotationPitch;
            IBlockState iblockstate = this.world.getBlockState(new BlockPos(xTile, yTile, zTile));
            this.inTile = iblockstate.getBlock();
            this.inData = this.inTile.getMetaFromState(iblockstate);
        } else {
            for (int i = 0; i < 4; i++) {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, Math.random() * 0.2 - 0.1 - this.motionX * 0.3, Math.random() * 0.2 - 0.1 - this.motionY * 0.3, Math.random() * 0.2 - 0.1 - this.motionZ * 0.3, Item.REGISTRY.getIDForObject(this.item));
            }
            this.setDead();

        }
    }
    public void landEntity(double posX, double posY, double posZ, double motionX, double motionY, double motionZ, boolean hasDurability) {

        if(!hasDurability) {
            for (int i = 0; i < 4; i++) {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ, Math.random() * 0.2 - 0.1 - motionX * 0.3, Math.random() * 0.2 - 0.1 - motionY * 0.3, Math.random() * 0.2 - 0.1 - motionZ * 0.3, Item.REGISTRY.getIDForObject(this.item));
            }
            this.setDead();

        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();
        //SmallKnives.out.print("\npos: ("+this.posX+",\t"+this.posY+",\t"+this.posZ+")\trot: ("+this.rotationYaw+",\t"+this.rotationPitch+")");
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
        if (shootingEntity != null) {
            if (!this.getEntityBoundingBox().intersects(shootingEntity.getEntityBoundingBox())) {
                this.insidePlayer = false;
            }
        }
        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR) {
            AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.inGround) {
            ticksFloating = 0;
            int j = block.getMetaFromState(iblockstate);

            if ((block != this.inTile || j != this.inData) && !this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.05D))) {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200) {
                    this.setDead();
                }
            }

            ++this.timeInGround;
        } else {
            this.timeInGround = 0;
            ++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (raytraceresult != null) {
                vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
            }

            Entity entity = this.findEntityOnPath(vec3d1, vec3d);

            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }

            if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

                if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                    raytraceresult = null;
                }
            }

            if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }
            if (ticksFloating > 0) {
                for (int k = 0; k < 4; ++k) {
                    //this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            //rotations
            float dist = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) dist) * (180D / Math.PI));

            float airResistance = 0.99F;

            if (this.isInWater()) {
                ticksFloating = 0;
                for (int i = 0; i < 4; ++i) {
                    //this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
                }

                airResistance = 0.6F;
            }

            if (this.isWet()) {
                this.extinguish();
            }
            if (ticksFloating < 1) {
                this.motionX *= (double) airResistance;
                this.motionY *= (double) airResistance;
                this.motionZ *= (double) airResistance;

                if (!this.hasNoGravity()) {
                    this.motionY -= 0.05000000074505806D;
                }
            } else {
                ticksFloating--;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    /**
     * Called when the arrow hits a block or an entity
     */
    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity entity = raytraceResultIn.entityHit;
        if (entity instanceof EntityLivingBase) {
        }
        if (entity != null) {
            if (entity.getEntityId() != shootingEntity.getEntityId() || !this.insidePlayer) {
                entity.attackEntityFrom(ModDamageSources.knife(this.shootingEntity), ((ItemKnife) this.item).material.getAttackDamage() + 2);
                if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                    entity.setFire(5);
                }
                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                this.motionX *= -0.10000000149011612D;
                this.motionY *= -0.10000000149011612D;
                this.motionZ *= -0.10000000149011612D;
                this.rotationYaw += 180.0F;
                this.prevRotationYaw += 180.0F;
                this.ticksInAir = 0;

                if (!this.world.isRemote) {
                    if (this.hasDurability()) {
                        this.entityDropItem(this.getKnifeItem(), 0.1F);
                    }else{
                        world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0f);
                    }
                    this.setDead();
                } else {
                    System.out.print("hello\n");
                    if (!this.hasDurability()) {
                        for (int i = 0; i < 4; i++) {
                            world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, Math.random() * 0.2 - 0.1 - this.motionX * 0.3, Math.random() * 0.2 - 0.1 - this.motionY * 0.3, Math.random() * 0.2 - 0.1 - this.motionZ * 0.3, Item.REGISTRY.getIDForObject(this.item));
                        }
                    }
                }
                PacketHandler.INSTANCE.sendToAll(new KnifeLandEntityPacket(this, this.hasDurability()));
            }
        } else {
            if (this.hasDurability()) {
                BlockPos blockpos = raytraceResultIn.getBlockPos();
                this.xTile = blockpos.getX();
                this.yTile = blockpos.getY();
                this.zTile = blockpos.getZ();
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                this.inTile = iblockstate.getBlock();
                this.inData = this.inTile.getMetaFromState(iblockstate);
                this.motionX = (double) ((float) (raytraceResultIn.hitVec.x - this.posX));
                this.motionY = (double) ((float) (raytraceResultIn.hitVec.y - this.posY));
                this.motionZ = (double) ((float) (raytraceResultIn.hitVec.z - this.posZ));
                float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
                this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
                this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                this.inGround = true;
                if (iblockstate.getMaterial() != Material.AIR) {
                    this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
                }
            } else {
                if (!world.isRemote) {
                    world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0f);
                    this.setDead();
                }
            }
            PacketHandler.INSTANCE.sendToAll(new KnifeLandBlockPacket(this, this.hasDurability()));
        }
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    public void move(MoverType type, double x, double y, double z) {
        super.move(type, x, y, z);

        if (this.inGround) {
            this.xTile = MathHelper.floor(this.posX);
            this.yTile = MathHelper.floor(this.posY);
            this.zTile = MathHelper.floor(this.posZ);
        }
    }

    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), ARROW_TARGETS);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = list.get(i);

            if (entity1 != this.shootingEntity || this.ticksInAir >= 5) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null) {
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("xTile", this.xTile);
        compound.setInteger("yTile", this.yTile);
        compound.setInteger("zTile", this.zTile);
        compound.setShort("life", (short) this.ticksInGround);
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
        compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("inData", (byte) this.inData);
        compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        compound.setDouble("damage", this.damage);
        compound.setInteger("floating", this.ticksFloating);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.xTile = compound.getInteger("xTile");
        this.yTile = compound.getInteger("yTile");
        this.zTile = compound.getInteger("zTile");
        this.ticksInGround = compound.getShort("life");

        if (compound.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(compound.getString("inTile"));
        } else {
            this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
        }

        this.inData = compound.getByte("inData") & 255;
        this.inGround = compound.getByte("inGround") == 1;

        if (compound.hasKey("damage", 99)) {
            this.damage = compound.getDouble("damage");
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        if (shootingEntity != null) {
            buf.writeInt(shootingEntity.getEntityId());
            buf.writeInt(Item.REGISTRY.getIDForObject(this.item));
        } else {
            buf.writeInt(-1);
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        this.shootingEntity = world.getEntityByID(buf.readInt());
        this.item = (ItemKnife) Item.REGISTRY.getObjectById(buf.readInt());
        this.ticksFloating = floatTimes.get(this.item);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        //System.out.print("\n" + number + " " + world.isRemote + "\tonCollideWithPlayer");
        if (this.inGround) {
            if (!this.world.isRemote) {
                if (this.hasDurability()) {
                    entityIn.inventory.addItemStackToInventory(this.getKnifeItem());
                    entityIn.onItemPickup(this, 1);
                }
                this.setDead();
            }
        }
    }

    ItemStack getKnifeItem() {
        ItemStack itemstack = new ItemStack(this.item);
        itemstack.setItemDamage(this.durability + 1);
        return itemstack;
    }

    boolean hasDurability() {
        return this.durability < item.material.getMaxUses();
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem() {
        return false;
    }

    public float getEyeHeight() {
        return 0.0F;
    }

}
