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

    public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
        GL11.glRotatef(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);

        double xy = Math.toDegrees(Math.atan2(entity.posZ - entity.prevPosZ, entity.posX - entity.prevPosX)) - entity.rotationYaw;

        double r = 50;

        if (xy > 90 - r && xy < 90 + r) {
            GL11.glRotatef((float) ((entity.posY - entity.prevPosY) * -90.0F), -1.0F, 0.0F, 0.0F);
        } else if (xy > -90 - r && xy < -90 + r) {
            GL11.glRotatef((float) ((entity.posY - entity.prevPosY) * -90.0F), 1.0F, 0.0F, 0.0F);
        }

        this.bindTexture(boatTextures);
        GL11.glScalef(1.6F, 1.6F, 1.6F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.model.renderAllExcept("Cannon", "Gun","Hatch1","Hatch2");

        if(entity.getDataWatcher().getWatchableObjectInt(20) == 0){
            model.renderOnly("Hatch1");
        }

        if(entity.getDataWatcher().getWatchableObjectInt(21) == 0){
            model.renderOnly("Hatch2");
        }

        {
            GL11.glPushMatrix();
            GL11.glTranslated(0, 1.5, 0.7);

            double y = 0;

            if (entity.riddenByEntity != null)
                y = entity.riddenByEntity.rotationYaw + 180.0F - p_76986_8_;

            while (y <= -300) y += 360;
            while (y > 300) y -= 360;

            if (y > 25) y = 25;
            if (y < -25) y = -25;

            GL11.glRotated(-y, 0, 1, 0);

            double p = 0;

            if (entity.riddenByEntity != null)
                p = entity.riddenByEntity.rotationPitch;

            if (p > 25) p = 25;
            if (p < -30) p = -30;

            GL11.glRotated(p, 1, 0, 0);
            GL11.glTranslated(0, -1.5, -0.7);

            model.renderOnly("Cannon");
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(-0.79, 1.76, 0.68);
            GL11.glRotated(-entity.getDataWatcher().getWatchableObjectFloat(22), 0, 1, 0);
            GL11.glRotated(entity.getDataWatcher().getWatchableObjectFloat(23), 1, 0, 0);
            GL11.glTranslated(0.79, -1.76, -0.68);
            model.renderOnly("Gun");
            GL11.glPopMatrix();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return null;
    }
}