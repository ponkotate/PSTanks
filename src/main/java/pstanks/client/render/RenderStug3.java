package pstanks.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.ObjModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderStug3 extends Render {
    private static final ResourceLocation boatTextures = new ResourceLocation("pstanks:textures/tank.png");
    protected WavefrontObject model;

    public RenderStug3() {
        this.shadowSize = 0.5F;
        this.model = (WavefrontObject) (new ObjModelLoader()).loadInstance(new ResourceLocation("pstanks:tanks/tank.obj"));
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
        GL11.glRotatef(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float) ((p_76986_1_.posY-p_76986_1_.prevPosY)*-90.0F), 1.0F, 0.0F, 0.0F);
        this.bindTexture(boatTextures);
        GL11.glScalef(1.6F, 1.6F, 1.6F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.model.renderAllExcept();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return null;
    }
}