package pstanks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.ObjModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderChinu extends Render {
    private static final ResourceLocation boatTextures = new ResourceLocation("pstanks:textures/tex.png");
    protected WavefrontObject model;

    public RenderChinu() {
        this.shadowSize = 0.5F;
        this.model = (WavefrontObject) (new ObjModelLoader()).loadInstance(new ResourceLocation("pstanks:tanks/chi_nu.obj"));
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_ + 1.6F, (float) p_76986_6_);
        GL11.glRotatef(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float) ((p_76986_1_.posY-p_76986_1_.prevPosY)*-90.0F), 1.0F, 0.0F, 0.0F);
        this.bindTexture(boatTextures);
        GL11.glScalef(0.6F, 0.6F, 0.6F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.model.renderAllExcept("houtou", "hatti1", "hatti2", "hou");
        {
            GL11.glTranslated(0, 0, -0.2);
            GL11.glRotatef(180.0F - p_76986_8_, 0.0F, -1.0F, 0.0F);
            if (p_76986_1_.riddenByEntity != null)
                GL11.glRotated(-p_76986_1_.riddenByEntity.rotationYaw, 0, 1, 0);
            else GL11.glRotated(180, 0, 1, 0);
            GL11.glTranslated(0, 0, 0.2);
            model.renderOnly("houtou", "hatti1", "hatti2");

            GL11.glTranslated(0, 0, 0.8);
            double p = 0;

            if (p_76986_1_.riddenByEntity != null)
                p = p_76986_1_.riddenByEntity.rotationPitch;

            if(p > 25)p = 25;
            if(p < -30)p=-30;

            GL11.glRotated(p, 1, 0, 0);
            GL11.glTranslated(0, 0, -0.8);
            model.renderOnly("hou");
        }
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return null;
    }
}