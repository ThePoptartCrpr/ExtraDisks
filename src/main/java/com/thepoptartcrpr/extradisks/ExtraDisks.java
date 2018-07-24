package com.thepoptartcrpr.extradisks;

import com.thepoptartcrpr.extradisks.init.EDItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExtraDisks.Reference.MODID, name = ExtraDisks.Reference.NAME, version = ExtraDisks.Reference.VERSION, dependencies = "required-after:refinedstorage")
public class ExtraDisks {

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EDItems());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    public class Reference {
        public static final String MODID = "extradisks";
        public static final String NAME = "Extra Disks";
        public static final String VERSION = "0.1";
    }

}
