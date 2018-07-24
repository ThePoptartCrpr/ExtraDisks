package com.thepoptartcrpr.extradisks.init;

import com.raoulvdberge.refinedstorage.RS;
import com.thepoptartcrpr.extradisks.items.EDStorageDisk;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EDItems {

    public static Item storageDisk = new EDStorageDisk();

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                storageDisk.setCreativeTab(RS.INSTANCE.tab)
        );
    }

    @SubscribeEvent
    public void registerRenders(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(storageDisk, 0, new ModelResourceLocation(storageDisk.getRegistryName(), "inventory"));
    }

}
