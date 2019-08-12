package small_knives.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import small_knives.entities.EntityKnife;

public class KnifeLandEntityPacket implements IMessage {
    private int entityID = 0;
    private double positionX=0;
    private double positionY=0;
    private double positionZ=0;
    private double motionX=0;
    private double motionY=0;
    private double motionZ=0;
    private boolean hasDurability=true;
    public KnifeLandEntityPacket(){

    }
    public KnifeLandEntityPacket(EntityKnife entity, boolean hasDurability) {
        this.entityID = entity.getEntityId();
        this.positionX = entity.posX;
        this.positionY = entity.posY;
        this.positionZ = entity.posZ;
        this.motionX = entity.motionX;
        this.motionY = entity.motionY;
        this.motionZ = entity.motionZ;
        this.hasDurability = hasDurability;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeDouble(positionX);
        buf.writeDouble(positionY);
        buf.writeDouble(positionZ);
        buf.writeDouble(motionX);
        buf.writeDouble(motionY);
        buf.writeDouble(motionZ);
        buf.writeBoolean(hasDurability);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
        positionX = buf.readDouble();
        positionY = buf.readDouble();
        positionZ = buf.readDouble();
        motionX = buf.readDouble();
        motionY = buf.readDouble();
        motionZ = buf.readDouble();
        hasDurability = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<KnifeLandEntityPacket, IMessage> {
        // Do note that the default constructor is required, but implicitly defined in this case
        @Override
        public IMessage onMessage(KnifeLandEntityPacket message, MessageContext ctx) {
            // This is the player the packet was sent to the server from
            Minecraft minecraft = Minecraft.getMinecraft();
            final WorldClient worldClient = minecraft.world;
            EntityKnife knife = (EntityKnife) worldClient.getEntityByID(message.entityID);
            minecraft.addScheduledTask(() -> {
                knife.landEntity(message.positionX,message.positionY,message.positionZ,message.motionX,message.motionY,message.motionZ,message.hasDurability);
                    }
            );
            return null;
        }
    }
}
