package small_knives.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import small_knives.entities.EntityKnife;

public class KnifeLandMessage implements IMessage {
    private int entityID = 0;
    private double posX = 0;
    private double posY = 0;
    private double posZ = 0;
    private int tileX = 0;
    private int tileY = 0;
    private int tileZ = 0;
    private float rotationYaw = 0;
    private float rotationPitch = 0;

    public KnifeLandMessage() {
    }

    public KnifeLandMessage(EntityKnife entity) {
        this.entityID = entity.getEntityId();
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.posZ = entity.posZ;
        this.tileX = entity.xTile;
        this.tileY = entity.yTile;
        this.tileZ = entity.zTile;
        this.rotationYaw = entity.rotationYaw;
        this.rotationPitch = entity.rotationPitch;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Writes the int into the buf
        buf.writeInt(entityID);
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeInt(tileX);
        buf.writeInt(tileY);
        buf.writeInt(tileZ);
        buf.writeFloat(rotationYaw);
        buf.writeFloat(rotationPitch);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        // Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
        entityID = buf.readInt();
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        tileX = buf.readInt();
        tileY = buf.readInt();
        tileZ = buf.readInt();
        rotationYaw = buf.readFloat();
        rotationPitch = buf.readFloat();
    }

    public static class Handler implements IMessageHandler<KnifeLandMessage, IMessage> {
        // Do note that the default constructor is required, but implicitly defined in this case
        @Override
        public IMessage onMessage(KnifeLandMessage message, MessageContext ctx) {
            // This is the player the packet was sent to the server from
            System.out.print(System.currentTimeMillis()+" hey look at me " + ctx.side + "\n");
            Minecraft minecraft = Minecraft.getMinecraft();
            final WorldClient worldClient = minecraft.world;
            EntityKnife knife = (EntityKnife)worldClient.getEntityByID(message.entityID);
            minecraft.addScheduledTask(()->
                knife.land(message.posX,message.posY,message.posZ,message.tileX,message.tileY,message.tileZ,message.rotationYaw,message.rotationPitch)
            );
            return null;
        }
    }
}
