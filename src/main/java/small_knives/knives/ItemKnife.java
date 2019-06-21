package small_knives.knives;

import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import small_knives.SmallKnives;

public class ItemKnife extends Item {
    Item.ToolMaterial material;
    private final float attackDamage;
    String name;
    public ItemKnife(Item.ToolMaterial material, String name) {
        this.material = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.attackDamage = 2.0F + material.getAttackDamage();
        setRegistryName(name);
        setUnlocalizedName(SmallKnives.MODID + "." + name);
        this.name = name;
    }
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public float getAttackDamage() {
        return attackDamage;
    }
}
