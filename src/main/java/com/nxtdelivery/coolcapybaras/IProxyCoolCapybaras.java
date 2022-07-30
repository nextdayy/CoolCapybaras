package com.nxtdelivery.coolcapybaras;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public interface IProxyCoolCapybaras {
    void preInit(FMLPreInitializationEvent var1);

    void init(FMLInitializationEvent var1);

    void postInit(FMLPostInitializationEvent var1);

    void serverLoad(FMLServerStartingEvent var1);
}
