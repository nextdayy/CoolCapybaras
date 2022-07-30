package com.nxtdelivery.coolcapybaras.entity;

import com.nxtdelivery.coolcapybaras.ElementsCoolCapybaras;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
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
public class EntityCapybara extends ElementsCoolCapybaras.ModElement {
    public static final int EntityId = 1;
    public static final int EntityIdRanged = 2;
    public static final SoundEvent HURT = new SoundEvent(new ResourceLocation("coolcapybaras", "entity.capybara.hurt"));
    public static final SoundEvent DEATH = new SoundEvent(new ResourceLocation("coolcapybaras", "entity.capybara.death"));
    public static final SoundEvent AMBIENT = new SoundEvent(new ResourceLocation("coolcapybaras", "entity.capybara.ambient"));

    public EntityCapybara(ElementsCoolCapybaras instance) {
        super(instance, 1);
    }

    public void initElements() {
        //this.elements.entities.add(() -> {
        //return EntityRegistry.registerModEntity((EntityCustom.class).id(new ResourceLocation("coolcapybaras", "capybara"), 1).name("capybara").tracker(64, 3, true).egg(-9680102, -10531559).build();
        //return
        //});
        DEATH.setRegistryName("entity.capybara.death");
        HURT.setRegistryName("entity.capybara.hurt");
        AMBIENT.setRegistryName("entity.capybara.ambient");
        ForgeRegistries.SOUND_EVENTS.register(HURT);
        ForgeRegistries.SOUND_EVENTS.register(DEATH);
        ForgeRegistries.SOUND_EVENTS.register(AMBIENT);
    }

    public void init(FMLInitializationEvent event) {
        Biome[] spawnBiomes = new Biome[]{Biome.REGISTRY.getObject(new ResourceLocation("plains"))};
        EntityRegistry.addSpawn(EntityCustom.class, 20, 3, 25, EnumCreatureType.CREATURE, spawnBiomes);
    }

    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityCustom.class, (renderManager) -> new RenderLiving(renderManager, new ModelCapybara(), 0.5F) {
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("coolcapybaras:textures/capybara.png");
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

    public static class EntityCustom extends EntityAnimal {
        public EntityCustom(World world) {
            super(world);
            this.setSize(0.6F, 1.8F);
            this.experienceValue = 5;
            this.isImmuneToFire = false;
            this.setNoAI(false);
        }

        protected void initEntityAI() {
            super.initEntityAI();
            this.tasks.addTask(1, new EntityAIWander(this, 1.0));
            this.tasks.addTask(2, new EntityAILookIdle(this));
            this.tasks.addTask(3, new EntityAIEatGrass(this));
            this.tasks.addTask(4, new EntityAISwimming(this));
            this.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.8F));
            this.tasks.addTask(6, new EntityAIPanic(this, 1.2));
            this.tasks.addTask(7, new EntityAIHurtByTarget(this, false));
            this.tasks.addTask(3, new EntityAIMate(this, 1.0));
        }

        public EnumCreatureAttribute getCreatureAttribute() {
            return EnumCreatureAttribute.UNDEFINED;
        }

        protected Item getDropItem() {
            return null;
        }

        public SoundEvent getAmbientSound() {
            return AMBIENT;
        }

        protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
            return HURT;
        }

        public SoundEvent getDeathSound() {
            return DEATH;
        }

        protected float getSoundVolume() {
            return 1.0F;
        }

        protected void applyEntityAttributes() {
            super.applyEntityAttributes();
            if (this.getEntityAttribute(SharedMonsterAttributes.ARMOR) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0);
            }

            if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
            }

            if (this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
            }

            if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
                this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0);
            }

        }

        public EntityCustom createChild(EntityAgeable ageable) {
            return new EntityCustom(this.world);
        }

        public float getEyeHeight() {
            return this.isChild() ? this.height : 1.3F;
        }

        public boolean isBreedingItem(ItemStack stack) {
            if (stack == null) {
                return false;
            } else {
                return (new ItemStack(Blocks.TALLGRASS, 1, 1)).getItem() == stack.getItem() && (new ItemStack(Blocks.TALLGRASS, 1, 1)).getMetadata() == stack.getMetadata();
            }
        }
    }
}
