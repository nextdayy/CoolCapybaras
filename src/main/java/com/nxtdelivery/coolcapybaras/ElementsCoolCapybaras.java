package com.nxtdelivery.coolcapybaras;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
public class ElementsCoolCapybaras implements IFuelHandler, IWorldGenerator {
    public final List<ModElement> elements = new ArrayList<>();
    public final List<Supplier<Block>> blocks = new ArrayList<>();
    public final List<Supplier<Item>> items = new ArrayList<>();
    public final List<Supplier<Biome>> biomes = new ArrayList<>();
    public final List<Supplier<EntityEntry>> entities = new ArrayList<>();
    public final List<Supplier<Potion>> potions = new ArrayList<>();
    public static Map<ResourceLocation, SoundEvent> sounds = new HashMap<>();
    private int messageID = 0;

    public ElementsCoolCapybaras() {
    }

    public void preInit(FMLPreInitializationEvent event) {
        try {
            for (ASMDataTable.ASMData asmData : event.getAsmData().getAll(ModElement.Tag.class.getName())) {
                Class<?> clazz = Class.forName(asmData.getClassName());
                if (clazz.getSuperclass() == ModElement.class) {
                    this.elements.add((ModElement) clazz.getConstructor(this.getClass()).newInstance(this));
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        Collections.sort(this.elements);
        this.elements.forEach(ModElement::initElements);
        this.addNetworkMessage(CoolCapybarasVariables.WorldSavedDataSyncMessageHandler.class, CoolCapybarasVariables.WorldSavedDataSyncMessage.class, Side.SERVER, Side.CLIENT);
    }

    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {

        for (Map.Entry<ResourceLocation, SoundEvent> resourceLocationSoundEventEntry : sounds.entrySet()) {
            event.getRegistry().register(resourceLocationSoundEventEntry.getValue().setRegistryName(resourceLocationSoundEventEntry.getKey()));
        }

    }

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator cg, IChunkProvider cp) {
        this.elements.forEach((element) -> element.generateWorld(random, chunkX * 16, chunkZ * 16, world, world.provider.getDimension(), cg, cp));
    }

    public int getBurnTime(ItemStack fuel) {
        Iterator<ModElement> var2 = this.elements.iterator();

        int ret;
        do {
            if (!var2.hasNext()) {
                return 0;
            }

            ModElement element = var2.next();
            ret = element.addFuel(fuel);
        } while(ret == 0);

        return ret;
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.world.isRemote) {
            WorldSavedData mapdata = CoolCapybarasVariables.MapVariables.get(event.player.world);
            WorldSavedData worlddata = CoolCapybarasVariables.WorldVariables.get(event.player.world);
            if (mapdata != null) {
                CoolCapybaras.PACKET_HANDLER.sendTo(new CoolCapybarasVariables.WorldSavedDataSyncMessage(0, mapdata), (EntityPlayerMP)event.player);
            }

            if (worlddata != null) {
                CoolCapybaras.PACKET_HANDLER.sendTo(new CoolCapybarasVariables.WorldSavedDataSyncMessage(1, worlddata), (EntityPlayerMP)event.player);
            }
        }

    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.player.world.isRemote) {
            WorldSavedData worlddata = CoolCapybarasVariables.WorldVariables.get(event.player.world);
            if (worlddata != null) {
                CoolCapybaras.PACKET_HANDLER.sendTo(new CoolCapybarasVariables.WorldSavedDataSyncMessage(1, worlddata), (EntityPlayerMP)event.player);
            }
        }

    }

    public <T extends IMessage, V extends IMessage> void addNetworkMessage(Class<? extends IMessageHandler<T, V>> handler, Class<T> messageClass, Side... sides) {
        for (Side side : sides) {
            CoolCapybaras.PACKET_HANDLER.registerMessage(handler, messageClass, this.messageID, side);
        }
        ++this.messageID;
    }

    public List<ModElement> getElements() {
        return this.elements;
    }

    public List<Supplier<Block>> getBlocks() {
        return this.blocks;
    }

    public List<Supplier<Item>> getItems() {
        return this.items;
    }

    public List<Supplier<Biome>> getBiomes() {
        return this.biomes;
    }

    public List<Supplier<EntityEntry>> getEntities() {
        return this.entities;
    }

    public List<Supplier<Potion>> getPotions() {
        return this.potions;
    }

    public static class ModElement implements Comparable<ModElement> {
        protected final ElementsCoolCapybaras elements;
        protected final int sortid;

        public ModElement(ElementsCoolCapybaras elements, int sortid) {
            this.elements = elements;
            this.sortid = sortid;
        }

        public void initElements() {
        }

        public void init(FMLInitializationEvent event) {
        }

        public void preInit(FMLPreInitializationEvent event) {
        }

        public void generateWorld(Random random, int posX, int posZ, World world, int dimID, IChunkGenerator cg, IChunkProvider cp) {
        }

        public void serverLoad(FMLServerStartingEvent event) {
        }

        public void registerModels(ModelRegistryEvent event) {
        }

        public int addFuel(ItemStack fuel) {
            return 0;
        }

        public int compareTo(ModElement other) {
            return this.sortid - other.sortid;
        }

        @Retention(RetentionPolicy.RUNTIME)
        public @interface Tag {
        }
    }

    public static class GuiHandler implements IGuiHandler {
        public GuiHandler() {
        }

        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }

        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }
    }
}
