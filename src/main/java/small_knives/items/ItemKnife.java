package small_knives.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import small_knives.SmallKnives;
import small_knives.entities.EntityKnife;
import small_knives.network.KnifeLandMessage;
import small_knives.network.PacketHandler;

import java.util.Set;

public class ItemKnife extends ItemTool {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.LEAVES,Blocks.LEAVES2, Blocks.WHEAT, Blocks.GRASS);

    public Item.ToolMaterial material;
    public String name;
    public ItemKnife(Item.ToolMaterial material, String name) {
        super(material, EFFECTIVE_ON);
        this.material = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.attackDamage = 2.0F + material.getAttackDamage();
        this.attackSpeed = -1.0f;
        setRegistryName(name);
        setUnlocalizedName(SmallKnives.MODID + "." + name);
        this.name = name;
    }
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){

        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            EntityKnife entityknife = new EntityKnife(worldIn, playerIn,this);
            worldIn.spawnEntity(entityknife);
            entityknife.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.5F, 0.0F);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }


}
