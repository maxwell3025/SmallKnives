package small_knives.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import small_knives.entities.EntityKnife;
import small_knives.items.ModItems;

public class RenderKnife extends Render<EntityKnife> {
    RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
    public RenderKnife(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }
    public void doRender(EntityKnife entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (!this.renderOutlines)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y, (float)z);
            float partialYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
            float partialPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            float spin = 0f;
            if(entity.ticksFloating>0){
                spin = (entity.ticksFloating-partialTicks)*36*0;
            }
            GlStateManager.rotate(partialYaw+90,0,1,0);
            GlStateManager.rotate(partialPitch-spin-135,0,0,-1);
            //this.bindEntityTexture(entity);
            this.bindTexture(KNIFE_TEXTURE);
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem( new ItemStack(entity.inGround? Items.IRON_SHOVEL:entity.type), ItemCameraTransforms.TransformType.NONE);
//            Tessellator tessellator = Tessellator.getInstance();
//            BufferBuilder bufferbuilder = tessellator.getBuffer();
//            GlStateManager.glNormal3f(0.05625F, 0.0F, 0.0F);
//            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
//            bufferbuilder.pos( -0.5D, -0.5D,0.0D).tex(0.0D, 0.0D).endVertex();
//            bufferbuilder.pos( -0.5D, 0.5D,0.0D).tex(0.0D, 1.0D).endVertex();
//            bufferbuilder.pos( 0.5D, 0.5D,0.0D).tex(1.0D, 1.0D).endVertex();
//            bufferbuilder.pos( 0.5D, -0.5D,0.0D).tex(1.0D, 0.0D).endVertex();
//            bufferbuilder.pos( 0.5D, -0.5D,0.0D).tex(1.0D, 0.0D).endVertex();
//            bufferbuilder.pos( 0.5D, 0.5D,0.0D).tex(1.0D, 1.0D).endVertex();
//            bufferbuilder.pos( -0.5D, 0.5D,0.0D).tex(0.0D, 1.0D).endVertex();
//            bufferbuilder.pos( -0.5D, -0.5D,0.0D).tex(0.0D, 0.0D).endVertex();
//            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    private static final ResourceLocation KNIFE_TEXTURE = new ResourceLocation("small_knives:textures/entity/iron_knife.png");

    protected ResourceLocation getEntityTexture(EntityKnife entity) {
        return KNIFE_TEXTURE;
    }
}
