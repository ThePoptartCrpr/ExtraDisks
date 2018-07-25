package com.thepoptartcrpr.extradisks.items;

import com.thepoptartcrpr.extradisks.types.StorageTypes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class EDStoragePart extends EDItem {

    public EDStoragePart() {
        super("storage_part");

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!this.isInCreativeTab(tab)) return;

        for (int i = 0; i < StorageTypes.values().length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        for (int i = 0; i < StorageTypes.values().length; i++) {
            if (stack.getItemDamage() == i) return this.getUnlocalizedName() + "." + StorageTypes.values()[i].getId();
        }
        return null;
    }

}
