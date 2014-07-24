package pstanks.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by yu on 2014/07/24.
 */
public class EntityChair extends Entity {
    private Entity main;

    public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
        return null;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public boolean canBePushed() {
        return false;
    }

    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.setPosition(this.posX, this.posY - 2 + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public EntityChair(World p_i1582_1_) {
        super(p_i1582_1_);
        setSize(1.0F,0.2F);
    }

    public EntityChair(Entity main) {
        this(main.worldObj);
        this.main = main;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!worldObj.isRemote&&main == null){
            setDead();
            return;
        }

        if(!worldObj.isRemote&&main.isDead){
            setDead();
            return;
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer entityPlayer) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != entityPlayer) {
            return true;
        } else {
            if (!this.worldObj.isRemote) {
                entityPlayer.mountEntity(this);
            }

            return true;
        }
    }

    @Override
    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {

    }
}
