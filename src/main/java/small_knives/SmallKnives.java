package small_knives;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = SmallKnives.MODID, name = SmallKnives.NAME, version = SmallKnives.VERSION, dependencies = "required-after:forge@[14.23.5.2768,)")
public class SmallKnives {
    public static final String MODID = "small_knives";
    public static final String NAME = "Small Knives";
    public static final String VERSION = "1.0.1";

    private static Logger logger;
    @SidedProxy(clientSide = "small_knives.ClientProxy", serverSide = "small_knives.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
