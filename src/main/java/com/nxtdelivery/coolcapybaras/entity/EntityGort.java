package com.nxtdelivery.coolcapybaras.entity;

import com.nxtdelivery.coolcapybaras.ElementsCoolCapybaras;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
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
        EntityRegistry.registerModEntity(new ResourceLocation("coolcapybaras", "gort"), EntityCustom.class, "gort", EntityID, "coolcapybaras", 64, 3, true, -9680102, -10531559);
        EntityRegistry.registerModEntity(new ResourceLocation("coolcapybaras", "entitybulletgort"), EntityArrowCustom.class, "entitybulletgort", EntityId_Ranged, "coolcapybaras", 64, 1, true);
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
        RenderingRegistry.registerEntityRenderingHandler(EntityCustom.class, (renderManager) -> new RenderLiving(renderManager, new ModelCapybara(), 0.5F) {
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

    public static class ModelCapybara extends ModelBase {
        private final ModelRenderer Head;
        private final ModelRenderer cube_r1;
        private final ModelRenderer cube_r2;
        private final ModelRenderer cube_r3;
        private final ModelRenderer Torso;
        private final ModelRenderer RightFrontLeg;
        private final ModelRenderer LeftFrontLeg;
        private final ModelRenderer LeftBackLeg;
        private final ModelRenderer RightBackLeg;

        public ModelCapybara() {
            this.textureWidth = 64;
            this.textureHeight = 64;
            this.Head = new ModelRenderer(this);
            this.Head.setRotationPoint(0.0F, 13.0F, -6.0F);
            this.cube_r1 = new ModelRenderer(this);
            this.cube_r1.setRotationPoint(0.0F, 11.0F, 6.0F);
            this.Head.addChild(this.cube_r1);
            this.setRotationAngle(this.cube_r1, -1.5272F, 0.0F, 0.0F);
            this.cube_r1.cubeList.add(new ModelBox(this.cube_r1, 9, 9, -4.0F, 3.0F, -17.0F, 0, 3, 3, 0.0F, false));
            this.cube_r2 = new ModelRenderer(this);
            this.cube_r2.setRotationPoint(0.0F, 11.0F, 6.0F);
            this.Head.addChild(this.cube_r2);
            this.setRotationAngle(this.cube_r2, -0.6109F, 0.0F, 0.0F);
            this.cube_r2.cubeList.add(new ModelBox(this.cube_r2, 0, 26, -4.0F, -9.0F, -16.0F, 8, 10, 5, 0.0F, false));
            this.cube_r3 = new ModelRenderer(this);
            this.cube_r3.setRotationPoint(0.0F, 11.0F, 6.0F);
            this.Head.addChild(this.cube_r3);
            this.setRotationAngle(this.cube_r3, -1.4835F, 0.0F, 0.0F);
            this.cube_r3.cubeList.add(new ModelBox(this.cube_r3, 9, 3, 4.1F, 3.0F, -17.2F, 0, 3, 3, 0.0F, false));
            this.Torso = new ModelRenderer(this);
            this.Torso.setRotationPoint(0.0F, 17.0F, 0.0F);
            this.Torso.cubeList.add(new ModelBox(this.Torso, 0, 0, -5.0F, -4.0F, -9.0F, 10, 8, 18, 0.0F, false));
            this.RightFrontLeg = new ModelRenderer(this);
            this.RightFrontLeg.setRotationPoint(-2.0F, 21.0F, -7.0F);
            this.RightFrontLeg.cubeList.add(new ModelBox(this.RightFrontLeg, 26, 26, -3.0F, 0.0F, -2.0F, 3, 3, 3, 0.0F, false));
            this.LeftFrontLeg = new ModelRenderer(this);
            this.LeftFrontLeg.setRotationPoint(3.0F, 22.0F, -6.0F);
            this.LeftFrontLeg.cubeList.add(new ModelBox(this.LeftFrontLeg, 0, 0, -1.0F, -1.0F, -3.0F, 3, 3, 3, 0.0F, false));
            this.LeftBackLeg = new ModelRenderer(this);
            this.LeftBackLeg.setRotationPoint(2.0F, 22.0F, 6.0F);
            this.LeftBackLeg.cubeList.add(new ModelBox(this.LeftBackLeg, 0, 6, 0.0F, -1.0F, -1.0F, 3, 3, 3, 0.0F, false));
            this.RightBackLeg = new ModelRenderer(this);
            this.RightBackLeg.setRotationPoint(-2.0F, 21.0F, 7.0F);
            this.RightBackLeg.cubeList.add(new ModelBox(this.RightBackLeg, 0, 12, -3.0F, 0.0F, -2.0F, 3, 3, 3, 0.0F, false));
        }

        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            this.Head.render(f5);
            this.Torso.render(f5);
            this.RightFrontLeg.render(f5);
            this.LeftFrontLeg.render(f5);
            this.LeftBackLeg.render(f5);
            this.RightBackLeg.render(f5);
        }

        public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }

        public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
            super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
            this.LeftBackLeg.rotateAngleX = MathHelper.cos(f) * -1.0F * f1;
            this.Head.rotateAngleY = f3 / 57.295776F;
            this.Head.rotateAngleX = f4 / 57.295776F;
            this.RightFrontLeg.rotateAngleX = MathHelper.cos(f) * 1.0F * f1;
            this.RightBackLeg.rotateAngleX = MathHelper.cos(f) * 1.0F * f1;
            this.LeftFrontLeg.rotateAngleX = MathHelper.cos(f) * -1.0F * f1;
        }
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
