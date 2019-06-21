package small_knives.knives;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import small_knives.SmallKnives;

import java.util.Set;

public class ItemKnife extends ItemTool {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.LEAVES,Blocks.LEAVES2, Blocks.WHEAT, Blocks.GRASS);

    Item.ToolMaterial material;
    String name;
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
}
