package com.nxtdelivery.coolcapybaras;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
public class CoolCapybarasVariables {
    public CoolCapybarasVariables() {
    }

    public static class WorldSavedDataSyncMessage implements IMessage {
        public int type;
        public WorldSavedData data;

        public WorldSavedDataSyncMessage() {
        }

        public WorldSavedDataSyncMessage(int type, WorldSavedData data) {
            this.type = type;
            this.data = data;
        }

        public void toBytes(ByteBuf buf) {
            buf.writeInt(this.type);
            ByteBufUtils.writeTag(buf, this.data.writeToNBT(new NBTTagCompound()));
        }

        public void fromBytes(ByteBuf buf) {
            this.type = buf.readInt();
            if (this.type == 0) {
                this.data = new MapVariables();
            } else {
                this.data = new WorldVariables();
            }

            this.data.readFromNBT(ByteBufUtils.readTag(buf));
        }
    }

    public static class WorldSavedDataSyncMessageHandler implements IMessageHandler<WorldSavedDataSyncMessage, IMessage> {
        public WorldSavedDataSyncMessageHandler() {
        }

        public IMessage onMessage(WorldSavedDataSyncMessage message, MessageContext context) {
            if (context.side == Side.SERVER) {
                context.getServerHandler().player.getServerWorld().addScheduledTask(() -> this.syncData(message, context, context.getServerHandler().player.world));
            } else {
                Minecraft.getMinecraft().addScheduledTask(() -> this.syncData(message, context, Minecraft.getMinecraft().player.world));
            }

            return null;
        }

        private void syncData(WorldSavedDataSyncMessage message, MessageContext context, World world) {
            if (context.side == Side.SERVER) {
                message.data.markDirty();
                if (message.type == 0) {
                    CoolCapybaras.PACKET_HANDLER.sendToAll(message);
                } else {
                    CoolCapybaras.PACKET_HANDLER.sendToDimension(message, world.provider.getDimension());
                }
            }

            if (message.type == 0) {
                world.getMapStorage().setData("coolcapybaras_mapvars", message.data);
            } else {
                world.getPerWorldStorage().setData("coolcapybaras_worldvars", message.data);
            }

        }
    }

    public static class WorldVariables extends WorldSavedData {
        public static final String DATA_NAME = "coolcapybaras_worldvars";

        public WorldVariables() {
            super("coolcapybaras_worldvars");
        }

        public WorldVariables(String s) {
            super(s);
        }

        public void readFromNBT(NBTTagCompound nbt) {
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            return nbt;
        }

        public void syncData(World world) {
            this.markDirty();
            if (world.isRemote) {
                CoolCapybaras.PACKET_HANDLER.sendToServer(new WorldSavedDataSyncMessage(1, this));
            } else {
                CoolCapybaras.PACKET_HANDLER.sendToDimension(new WorldSavedDataSyncMessage(1, this), world.provider.getDimension());
            }

        }

        public static WorldVariables get(World world) {
            WorldVariables instance = (WorldVariables)world.getPerWorldStorage().getOrLoadData(WorldVariables.class, "coolcapybaras_worldvars");
            if (instance == null) {
                instance = new WorldVariables();
                world.getPerWorldStorage().setData("coolcapybaras_worldvars", instance);
            }

            return instance;
        }
    }

    public static class MapVariables extends WorldSavedData {
        public static final String DATA_NAME = "coolcapybaras_mapvars";

        public MapVariables() {
            super("coolcapybaras_mapvars");
        }

        public MapVariables(String s) {
            super(s);
        }

        public void readFromNBT(NBTTagCompound nbt) {
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            return nbt;
        }

        public void syncData(World world) {
            this.markDirty();
            if (world.isRemote) {
                CoolCapybaras.PACKET_HANDLER.sendToServer(new WorldSavedDataSyncMessage(0, this));
            } else {
                CoolCapybaras.PACKET_HANDLER.sendToAll(new WorldSavedDataSyncMessage(0, this));
            }

        }

        public static MapVariables get(World world) {
            MapVariables instance = (MapVariables)world.getPerWorldStorage().getOrLoadData(MapVariables.class, "coolcapybaras_mapvars");
            if (instance == null) {
                instance = new MapVariables();
                world.getPerWorldStorage().setData("coolcapybaras_mapvars", instance);
            }

            return instance;
        }
    }
}
