package com.nxtdelivery.coolcapybaras.entity;

import com.nxtdelivery.coolcapybaras.ElementsCoolCapybaras;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ElementsCoolCapybaras.ModElement.Tag
public class EntityGort extends ElementsCoolCapybaras.ModElement {
    public static final int EntityID = 3;
    public static final int EntityId_Ranged = 4;
    public static final SoundEvent SPAWN = new SoundEvent(new ResourceLocation("coolcapybaras", "entity.gort.spawn"));
    public static final SoundEvent DEATH = new SoundEvent(new ResourceLocation("coolcapybaras", "entity.gort.death"));
    public static final SoundEvent STEP = new SoundEvent(new ResourceLocation("coolcapybaras", "entity.gort.step"));
    public static final SoundEvent SHOOT = new SoundEvent(new ResourceLocation("coolcapybaras", "entity.gort.shoot"));

    public EntityGort(ElementsCoolCapybaras instance) {
        super(instance, 2);
    }

    public void initElements() {
        SPAWN.setRegistryName("entity.gort.spawn");
        DEATH.setRegistryName("entity.gort.death");
        STEP.setRegistryName("entity.gort.step");
        SHOOT.setRegistryName("entity.gort.shoot");
        ForgeRegistries.SOUND_EVENTS.register(DEATH);
        ForgeRegistries.SOUND_EVENTS.register(SPAWN);
        ForgeRegistries.SOUND_EVENTS.register(STEP);
        ForgeRegistries.SOUND_EVENTS.register(SHOOT);
        //this.elements.entities.add(() -> {
        //    return EntityRegistry.create().entity(EntityCustom.class).id(new ResourceLocation("coolcapybaras", "gort"), 3).name("gort").tracker(64, 3, true).egg(-1, -1).build();
        //});
        //this.elements.entities.add(() -> {
        //    return EntityEntryBuilder.create().entity(EntityArrowCustom.class).id(new ResourceLocation("coolcapybaras", "entitybulletgort"), 4).name("entitybulletgort").tracker(64, 1, true).build();
        //});
    }

    public void init(FMLInitializationEvent event) {
        Biome[] spawnBiomes = new Biome[]{Biome.REGISTRY.getObject(new ResourceLocation("swampland"))};
        EntityRegistry.addSpawn(EntityCustom.class, 1, 1, 1, EnumCreatureType.MONSTER, spawnBiomes);
    }

    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityCustom.class, (renderManager) -> new RenderLiving(renderManager, new EntityCapybara.ModelCapybara(), 0.5F) {
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("coolcapybaras:textures/capybara.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityArrowCustom.class, (renderManager) -> new RenderSnowball<EntityArrowCustom>(renderManager, null, Minecraft.getMinecraft().getRenderItem()) {
            public ItemStack getStackToRender(EntityArrowCustom entity) {
                return new ItemStack(Items.GOLDEN_CARROT, 1);
            }
        });
    }

    public static class EntityArrowCustom extends EntityTippedArrow {
        public EntityArrowCustom(World a) {
            super(a);
        }

        public EntityArrowCustom(World worldIn, double x, double y, double z) {
            super(worldIn, x, y, z);
        }

        public EntityArrowCustom(World worldIn, EntityLivingBase shooter) {
            super(worldIn, shooter);
            this.addEffect(new PotionEffect(MobEffects.SLOWNESS, 1, 2));
        }

        @Override
        public void playSound(SoundEvent soundIn, float volume, float pitch) {
            if (soundIn == SoundEvents.ENTITY_ARROW_SHOOT || soundIn == SoundEvents.ENTITY_ARROW_HIT) {
                playSound(SHOOT, volume, pitch - 0.2f);
            } else {
                super.playSound(soundIn, volume, pitch);
            }
        }
    }

    public static class EntityCustom extends EntityMob implements IRangedAttackMob {
        private final BossInfoServer bossInfo;

        public EntityCustom(World world) {
            super(world);
            this.bossInfo = new BossInfoServer(this.getDisplayName(), Color.PINK, Overlay.PROGRESS);
            this.setSize(0.6F, 1.8F);
            this.experienceValue = 500;
            this.isImmuneToFire = false;
            this.setNoAI(false);
            this.setCustomNameTag("Gort");
            this.setAlwaysRenderNameTag(true);
            this.enablePersistence();
        }

        protected void initEntityAI() {
            super.initEntityAI();
            this.tasks.addTask(1, new EntityAIWander(this, 1.0));
            this.tasks.addTask(2, new EntityAILookIdle(this));
            this.tasks.addTask(3, new EntityAISwimming(this));
            this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.8F));
            this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true, false));
            this.tasks.addTask(6, new EntityAIAttackMelee(this, 1.2, true));
            this.targetTasks.addTask(7, new EntityAIHurtByTarget(this, true));
            this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25, 20, 10.0F));
            this.world.playSound(null, new BlockPos(this), SPAWN, SoundCategory.HOSTILE, 1f, 1f);
        }

        public EnumCreatureAttribute getCreatureAttribute() {
            return EnumCreatureAttribute.UNDEFINED;
        }

        protected boolean canDespawn() {
            return false;
        }

        protected Item getDropItem() {
            return (new ItemStack(Items.DIAMOND, 1)).getItem();
        }

        public SoundEvent getAmbientSound() {
            // capybara already has cool ambient sounds
            return EntityCapybara.AMBIENT;
        }

        @Override
        protected SoundEvent getSwimSound() {
            return STEP;
        }

        @Override
        protected SoundEvent getFallSound(int heightIn) {
            return STEP;
        }

        public SoundEvent getDeathSound() {
            return DEATH;
        }

        public SoundEvent getHurtSound(DamageSource ds) {
            return EntityCapybara.HURT;
        }

        protected float getSoundVolume() {
            return 1.0F;
        }

        public boolean attackEntityFrom(DamageSource source, float amount) {
            if (source.getImmediateSource() instanceof EntityArrow) {
                return false;
            } else {
                return source != DamageSource.FALL && super.attackEntityFrom(source, amount);
            }
        }

        protected void applyEntityAttributes() {
            super.applyEntityAttributes();
            if (this.getEntityAttribute(SharedMonsterAttributes.ARMOR) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0);
            }

            if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(8.0);
            }

            if (this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500.0);
            }

            if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(28.0);
            }

        }

        public void setSwingingArms(boolean swingingArms) {
        }

        public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
            EntityArrowCustom entityArrow = new EntityArrowCustom(this.world, this);
            double d0 = target.posY + (double) target.getEyeHeight() - 1.1;
            double d1 = target.posX - this.posX;
            double d3 = target.posZ - this.posZ;
            entityArrow.setThrowableHeading(d1, d0 - entityArrow.posY + (double) MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224, d3, 1.6F, 12.0F);
            this.world.spawnEntity(entityArrow);
        }

        public boolean isNonBoss() {
            return false;
        }

        public void addTrackingPlayer(EntityPlayerMP player) {
            super.addTrackingPlayer(player);
            this.bossInfo.addPlayer(player);
        }

        public void removeTrackingPlayer(EntityPlayerMP player) {
            super.removeTrackingPlayer(player);
            this.bossInfo.removePlayer(player);
        }

        public void onUpdate() {
            super.onUpdate();
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }
}
