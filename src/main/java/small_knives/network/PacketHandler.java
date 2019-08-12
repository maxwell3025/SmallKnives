package small_knives.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("small_knives");
    private static byte packetId = 0;
    public static void register(){
        INSTANCE.registerMessage(KnifeLandBlockPacket.Handler.class, KnifeLandBlockPacket.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(KnifeLandEntityPacket.Handler.class, KnifeLandEntityPacket.class, packetId++, Side.CLIENT);
    }
}
