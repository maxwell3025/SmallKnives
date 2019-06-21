package small_knives;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import small_knives.knives.*;

public class ModItems {
    @GameRegistry.ObjectHolder("small_knives:wooden_knife")
    public static ItemKnife wooden_knife;
    @GameRegistry.ObjectHolder("small_knives:stone_knife")
    public static ItemKnife stone_knife;
    @GameRegistry.ObjectHolder("small_knives:iron_knife")
    public static ItemKnife iron_knife;
    @GameRegistry.ObjectHolder("small_knives:golden_knife")
    public static ItemKnife golden_knife;
    @GameRegistry.ObjectHolder("small_knives:diamond_knife")
    public static ItemKnife diamond_knife;
    @SideOnly(Side.CLIENT)
    public static void initModels() {
        wooden_knife.initModel();
    }
}
