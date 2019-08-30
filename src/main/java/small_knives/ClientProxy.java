package small_knives;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import small_knives.blocks.ModBlocks;
import small_knives.entities.EntityKnife;
import small_knives.items.ModItems;
import small_knives.misc.stealth.Stealth;
import small_knives.misc.stealth.StealthRenderer;
import small_knives.render.RenderKnife;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        RenderingRegistry.registerEntityRenderingHandler(EntityKnife.class, manager->new RenderKnife(manager));
        super.preInit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e) {
        ModBlocks.initModels();
        ModItems.initModels();
    }
}
