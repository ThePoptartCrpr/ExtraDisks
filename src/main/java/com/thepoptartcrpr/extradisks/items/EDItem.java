package com.thepoptartcrpr.extradisks.items;

import com.thepoptartcrpr.extradisks.ExtraDisks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class EDItem extends Item {

    public EDItem(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(ExtraDisks.Reference.MODID, name));
    }

}
