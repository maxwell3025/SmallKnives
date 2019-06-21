package small_knives;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import small_knives.knives.*;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new ItemKnife(Item.ToolMaterial.WOOD, "wooden_knife"));
        e.getRegistry().register(new ItemKnife(Item.ToolMaterial.STONE, "stone_knife"));
        e.getRegistry().register(new ItemKnife(Item.ToolMaterial.IRON, "iron_knife"));
        e.getRegistry().register(new ItemKnife(Item.ToolMaterial.GOLD, "golden_knife"));
        e.getRegistry().register(new ItemKnife(Item.ToolMaterial.DIAMOND, "diamond_knife"));
    }
}
