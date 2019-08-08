package small_knives.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        stone_knife.initModel();
        iron_knife.initModel();
        golden_knife.initModel();
        diamond_knife.initModel();
    }
}
