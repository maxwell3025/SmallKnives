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


public class EntityKnife extends Entity implements IProjectile{

    public EntityKnife(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

    }
}
