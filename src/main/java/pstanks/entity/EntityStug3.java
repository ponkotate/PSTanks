package pstanks.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import pstanks.system.DataManager;

import java.util.List;

public class EntityStug3 extends Entity {
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

    private int gd = 0;
    private int sd = 0;
    private int sh = 0;
    private int shd = 0;

    public Entity edc = null;
    public Entity erc = null;

    public EntityStug3(World p_i1704_1_) {
        super(p_i1704_1_);
        this.isTankEmpty = true;
        this.preventEntitySpawning = true;
        this.setSize(6.0F, 0.01F);
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
        this.dataWatcher.addObject(20, 0);
        this.dataWatcher.addObject(21, 0);
        this.dataWatcher.addObject(22, 0.0F);
        this.dataWatcher.addObject(23, 0.0F);
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

    public EntityStug3(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
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
        return 2;
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
            if (edc == null) {
                edc = new EntityChair(this);
                double d00 = Math.cos(((double) rotationYaw + 55 - 180) * Math.PI / 180.0D) * 1.8D;
                double d10 = Math.sin(((double) rotationYaw + 55 - 180) * Math.PI / 180.0D) * 1.8D;
                edc.setPosition(this.posX + d00, this.posY + 2.8, this.posZ + d10);
                worldObj.spawnEntityInWorld(edc);
                erc = new EntityChair(this);
                d00 = Math.cos(((double) rotationYaw + 35) * Math.PI / 180.0D) * 1.3D;
                d10 = Math.sin(((double) rotationYaw + 35) * Math.PI / 180.0D) * 1.3D;
                erc.setPosition(this.posX + d00, this.posY + dataWatcher.getWatchableObjectInt(21) * 0.4 + 3, this.posZ + d10);
                worldObj.spawnEntityInWorld(erc);
            }

            double d00 = Math.cos(((double) rotationYaw + 55 - 180) * Math.PI / 180.0D) * 1.8D;
            double d10 = Math.sin(((double) rotationYaw + 55 - 180) * Math.PI / 180.0D) * 1.8D;
            edc.setPosition(this.posX + d00, this.posY + 2.8, this.posZ + d10);
            d00 = Math.cos(((double) rotationYaw + 35) * Math.PI / 180.0D) * 1.3D;
            d10 = Math.sin(((double) rotationYaw + 35) * Math.PI / 180.0D) * 1.3D;
            erc.setPosition(this.posX + d00, this.posY + dataWatcher.getWatchableObjectInt(21) * 0.5 + 3, this.posZ + d10);

            if (gd > 0)
                gd--;
            if (shd > 0)
                shd--;
            if (sd > 0)
                sd--;

            {
                this.motionY -= 0.1;
            }


            if (this.erc.riddenByEntity != null && this.erc.riddenByEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) this.erc.riddenByEntity;
                double r = player.rotationYaw - rotationYaw - 180;
                double rp = player.rotationPitch;
                double n = 25;
                if (r > -n && r < n&&rp > -n && rp < n) {
                    dataWatcher.updateObject(22,(float)r);
                    dataWatcher.updateObject(23,(float)rp);

                    if (DataManager.isKeyPress(player, DataManager.keyShot)) {
                        worldObj.spawnEntityInWorld(new EntityArrow(worldObj, player, 2));
                    }
                }

                if (DataManager.isKeyPress(player, DataManager.keyJump) && shd == 0) {
                    dataWatcher.updateObject(21, dataWatcher.getWatchableObjectInt(21) + 1);
                    if (dataWatcher.getWatchableObjectInt(21) > 2) {
                        dataWatcher.updateObject(21, 0);
                    }
                    shd = 16;
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer) {
                if (gd == 0) {
                    if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyBack)) {
                        if (speed < 5) {
                            this.speed += 1;
                            gd = 16;
                        }
                    } else if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyForward)) {
                        if (speed > -5) {
                            this.speed -= 1;
                            gd = 16;
                        }
                    }
                }

                if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyLeft)) {
                    if (speed > 0)
                        this.rotationYaw += 3;
                    else this.rotationYaw -= 3;
                    if (this.rotationYaw < -360)
                        rotationYaw += 360;
                } else if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyRight)) {
                    if (speed > 0)
                        this.rotationYaw -= 3;
                    else this.rotationYaw += 3;
                    if (this.rotationYaw > 360)
                        rotationYaw -= 360;
                }

                if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyJump)) {
                    speed = 0;
                    gd = 0;
                }

                if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyJump) && shd == 0) {
                    sh += 1;
                    if (sh > 2) sh = 0;
                    dataWatcher.updateObject(20, sh);
                    shd = 16;
                }

                if (DataManager.isKeyPress((EntityPlayer) this.riddenByEntity, DataManager.keyShot) && sd == 0) {
                    double p = 0;

                    p = riddenByEntity.rotationPitch;

                    if (p > 25) p = 25;
                    if (p < -30) p = -30;

                    double y = 0;

                    y = riddenByEntity.rotationYaw + 180.0F - rotationYaw;

                    while (y <= -300) y += 360;
                    while (y > 300) y -= 360;

                    if (y > 25) y = 25;
                    if (y < -25) y = -25;

                    double d0 = Math.cos((y + rotationYaw - 90) * Math.PI / 180.0D) * 2.4D;
                    double d1 = Math.sin((y + rotationYaw - 90) * Math.PI / 180.0D) * 2.4D;
                    double d2 = Math.sin(-p * Math.PI / 180.0D) * 1.6D;
                    EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed(worldObj, posX + d0, posY + 2.4, posZ + d1, (EntityLivingBase) riddenByEntity);
                    entityTNTPrimed.motionX = d0;
                    entityTNTPrimed.motionY = d2;
                    entityTNTPrimed.motionZ = d1;
                    worldObj.spawnEntityInWorld(entityTNTPrimed);
                    worldObj.createExplosion(riddenByEntity, posX + d0, posY + 2.4, posZ + d1, 1.5F, false);
                    sd = 15;
                }
            }

            float f = this.rotationYaw;
            this.motionX = -Math.sin((double) (f * (float) Math.PI / 180.0F)) * this.speed * 0.08;
            this.motionZ = Math.cos((double) (f * (float) Math.PI / 180.0F)) * this.speed * 0.08;

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            this.motionX *= 0.98;
            this.motionZ *= 0.98;

            this.rotationPitch = 0.0F;
            double d11 = this.prevPosX - this.posX;
            double d12 = this.prevPosZ - this.posZ;

            double d4 = (double) ((float) (Math.atan2(d12, d11) * 180.0D / Math.PI));

            //this.rotationYaw = (float) ((double) d4);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            double m = Math.sqrt(motionX * motionX + motionY * motionY + motionX * motionZ);

            if (m > 0 && this.riddenByEntity != null) {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(2, 1, 2));

                if (list != null && !list.isEmpty()) {
                    for (Object aList : list) {
                        Entity entity = (Entity) aList;

                        if (entity != this.riddenByEntity && entity.canBePushed()) {
                            entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) this.riddenByEntity), 10);
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
            double d0 = Math.cos(((double) rotationYaw + 60 - 270) * Math.PI / 180.0D) * 1.3D;
            double d1 = Math.sin(((double) rotationYaw + 60 - 270) * Math.PI / 180.0D) * 1.3D;
            this.riddenByEntity.setPosition(this.posX + d0, this.posY + dataWatcher.getWatchableObjectInt(20) * 0.4 + this.riddenByEntity.getYOffset() + 1.5, this.posZ + d1);
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