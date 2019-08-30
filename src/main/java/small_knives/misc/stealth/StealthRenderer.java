package small_knives.misc.stealth;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import small_knives.items.ItemKnife;

@Mod.EventBusSubscriber
public class StealthRenderer {
    private static Boolean firstTime = true;

    @SubscribeEvent
    public static void render(RenderPlayerEvent.Pre event) {
        if (!(((AbstractClientPlayer) event.getEntity()).getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemKnife)) {
            return;
        }
        Stealth.activate = true;
        if (firstTime) {
            firstTime = false;
            Stealth.dummy = (AbstractClientPlayer) event.getEntity();
            Stealth.itemBuffer = Stealth.dummy.getHeldItem(EnumHand.OFF_HAND);
            Stealth.dummy.inventory.offHandInventory.set(0, ItemStack.EMPTY);

            event.getRenderer().doRender(Stealth.dummy, event.getX(), event.getY(), event.getZ(), event.getEntity().rotationYaw, event.getPartialRenderTick());
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public static void renderPost(RenderPlayerEvent.Post event) {
        if (!Stealth.activate) {
            return;
        }
        Stealth.activate = false;
        firstTime = true;
        Stealth.dummy.inventory.offHandInventory.set(0, Stealth.itemBuffer);
    }
}
