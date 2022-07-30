//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nxtdelivery.coolcapybaras;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClientProxyCoolCapybaras implements IProxyCoolCapybaras {
    public ClientProxyCoolCapybaras() {
    }

    public void init(FMLInitializationEvent event) {
    }

    public void preInit(FMLPreInitializationEvent event) {
        OBJLoader.INSTANCE.addDomain("coolcapybaras");
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverLoad(FMLServerStartingEvent event) {
    }
}
