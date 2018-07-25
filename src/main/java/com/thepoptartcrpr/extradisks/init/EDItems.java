package com.thepoptartcrpr.extradisks.init;

import com.raoulvdberge.refinedstorage.RS;
import com.thepoptartcrpr.extradisks.items.EDStorageDisk;
import com.thepoptartcrpr.extradisks.items.EDStoragePart;
import com.thepoptartcrpr.extradisks.types.StorageTypes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EDItems {

    public static Item storageDisk = new EDStorageDisk();
    public static Item storagePart = new EDStoragePart();

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                storageDisk.setCreativeTab(RS.INSTANCE.tab),
                storagePart.setCreativeTab(RS.INSTANCE.tab)
        );
    }

    @SubscribeEvent
    public void registerRenders(ModelRegistryEvent event) {
        for (int i = 0; i < StorageTypes.values().length; i++ ) {
            registerRender(storageDisk, i, storageDisk.getRegistryName() + "_" + i);
        }
        for (int i = 0; i < StorageTypes.values().length; i++) {
            registerRender(storagePart, i, storagePart.getRegistryName() + "_" + i);
        }
    }

    private void registerRender(Item item) {
        this.registerRender(item, 0, item.getRegistryName());
    }

    private void registerRender(Item item, int meta, String location) {
        this.registerRender(item, meta, new ResourceLocation(location));
    }

    private void registerRender(Item item, int meta, ResourceLocation location) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
    }

}
