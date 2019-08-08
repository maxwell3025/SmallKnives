package small_knives;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import small_knives.entities.EntityKnife;
import small_knives.items.*;
import small_knives.network.PacketHandler;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
        EntityRegistry.registerModEntity(new ResourceLocation(SmallKnives.MODID, "knife"), EntityKnife.class, SmallKnives.MODID + ".entity_knife", 0, SmallKnives.INSTANCE, 64, 1, true);
        PacketHandler.register();
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
