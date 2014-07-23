package pstanks.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import pstanks.system.DataManager;

public class EntityChinu extends Entity {
    /**
     * true if no player in boat
     */

    private boolean isTankEmpty;
    private double speed = 0;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;

    public EntityChinu(World p_i1704_1_) {
        super(p_i1704_1_);
        this.isTankEmpty = true;
        this.preventEntitySpawning = true;
        this.setSize(2.5F, 0.01F);
        this.yOffset = this.height / 2.0F;
        this.ignoreFrustumCheck = true;
        this.stepHeight = 2.0F;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        this.dataWatcher.addObject(17, 0);
        this.dataWatcher.addObject(18, 1);
        this.dataWatcher.addObject(19, 0.0F);
    }

    public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
        return p_70114_1_.boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public boolean canBePushed() {
        return true;
    }

    public EntityChinu(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
        this(p_i1705_1_);
        this.setPosition(p_i1705_2_, p_i1705_4_ + (double) this.yOffset, p_i1705_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }

    public double getMountedYOffset() {
        return 2.8;
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else if (!this.worldObj.isRemote && !this.isDead) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + p_70097_2_ * 10.0F);
            this.setBeenAttacked();
            boolean flag = p_70097_1_.getEntity() instanceof EntityPlayer && ((EntityPlayer) p_70097_1_.getEntity()).capabilities.isCreativeMode;

            if (flag || this.getDamageTaken() > 400.0F) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mountEntity(this);
                }

                if (!flag) {
                    this.func_145778_a(Items.boat, 1, 0.0F);
                }

                this.setDead();
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0F);
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_) {
        if (this.isTankEmpty) {
            this.boatPosRotationIncrements = p_70056_9_ + 5;
        } else {
            double d3 = p_70056_1_ - this.posX;
            double d4 = p_70056_3_ - this.posY;
            double d5 = p_70056_5_ - this.posZ;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;

            if (d6 <= 1.0D) {
                return;
            }

            this.boatPosRotationIncrements = 3;
        }

        this.boatX = p_70056_1_;
        this.boatY = p_70056_3_;
        this.boatZ = p_70056_5_;
        this.boatYaw = (double) p_70056_7_;
        this.boatPitch = (double) p_70056_8_;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
        this.velocityX = this.motionX = p_70016_1_;
        this.velocityY = this.motionY = p_70016_3_;
        this.velocityZ = this.motionZ = p_70016_5_;
    }

    public void onUpdate() {
        super.onUpdate();

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.worldObj.isRemote) {
            this.setRotation((float) boatYaw, this.rotationPitch);
            if (this.boatPosRotationIncrements > 0) {
                double d2 = this.posX + (this.boatX - this.posX) / (double) this.boatPosRotationIncrements;
                double d4 = this.posY + (this.boatY - this.posY) / (double) this.boatPosRotationIncrements;
                double d11 = this.posZ + (this.boatZ - this.posZ) / (double) this.boatPosRotationIncrements;
                this.rotationPitch = (float) ((double) this.rotationPitch + (this.boatPitch - (double) this.rotationPitch) / (double) this.boatPosRotationIncrements);
                this.setRotation(this.rotationYaw, this.rotationPitch);
                --this.boatPosRotationIncrements;
                this.setPosition(d2, d4, d11);
            } else {
                double d2 = this.posX + this.motionX;
                double d4 = this.posY + this.motionY;
                double d11 = this.posZ + this.motionZ;
                this.setPosition(d2, d4, d11);

                if (this.onGround) {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
        } else {
            {
                this.motionY -= 0.01;
            }

            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
                if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyBack)) {
                    if (speed < 5)
                        this.speed += 1;
                } else if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyForward)) {
                    if (speed > -5)
                        this.speed -= 1;
                }

                if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyLeft)) {
                    this.rotationYaw -= 8;
                    if(this.rotationYaw < -360)
                        rotationYaw += 360;
                } else if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyRight)) {
                    this.rotationYaw += 8;
                    if(this.rotationYaw > 360)
                        rotationYaw -= 360;
                }

                if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyShot)) {
                    double d0 = Math.cos(((double) riddenByEntity.rotationYaw+90) * Math.PI / 180.0D) * 1.6D;
                    double d1 = Math.sin(((double) riddenByEntity.rotationYaw+90) * Math.PI / 180.0D) * 1.6D;
                    double d2 = Math.sin(((double) -riddenByEntity.rotationPitch) * Math.PI / 180.0D) * 1.6D;
                    EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed(worldObj,posX+d0,posY+2.4,posZ+d1, (EntityLivingBase) riddenByEntity);
                    entityTNTPrimed.motionX = d0;
                    entityTNTPrimed.motionY = d2;
                    entityTNTPrimed.motionZ = d1;
                    worldObj.spawnEntityInWorld(entityTNTPrimed);
                }
            }

            float f = this.rotationYaw;
            this.motionX = -Math.sin((double) (f * (float) Math.PI / 180.0F)) * this.speed*0.2;
            this.motionZ = Math.cos((double) (f * (float) Math.PI / 180.0F)) * this.speed*0.2;

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            this.motionX *= 0.98;
            this.motionZ *= 0.98;

            this.rotationPitch = 0.0F;
            double d11 = this.prevPosX - this.posX;
            double d12 = this.prevPosZ - this.posZ;

            double d4 = (double) ((float) (Math.atan2(d12, d11) * 180.0D / Math.PI));

            //this.rotationYaw = (float) ((double) d4);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            double m = Math.sqrt(motionX*motionX+motionY*motionY+motionX*motionZ);

            if (m > 0&&this.riddenByEntity != null) {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(2,1,2));

                if (list != null && !list.isEmpty()) {
                    for (Object aList : list) {
                        Entity entity = (Entity) aList;

                        if (entity != this.riddenByEntity && entity.canBePushed()) {
                            entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this.riddenByEntity),10);
                        }
                    }
                }

                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }

    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double d0 = Math.cos(((double) riddenByEntity.rotationYaw-50-90) * Math.PI / 180.0D) * 0.7D;
            double d1 = Math.sin(((double) riddenByEntity.rotationYaw-50-90) * Math.PI / 180.0D) * 0.7D;
            this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
        }
    }

    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }

    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    public boolean interactFirst(EntityPlayer p_130002_1_) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != p_130002_1_) {
            return true;
        } else {
            if (!this.worldObj.isRemote) {
                p_130002_1_.mountEntity(this);
            }

            return true;
        }
    }

    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
    }

    public void setDamageTaken(float p_70266_1_) {
        this.dataWatcher.updateObject(19, p_70266_1_);
    }

    public float getDamageTaken() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setTimeSinceHit(int p_70265_1_) {
        this.dataWatcher.updateObject(17, p_70265_1_);
    }

    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setForwardDirection(int p_70269_1_) {
        this.dataWatcher.updateObject(18, p_70269_1_);
    }

    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
}