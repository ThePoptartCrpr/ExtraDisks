package com.thepoptartcrpr.extradisks.items;

import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDiskSyncData;
import com.raoulvdberge.refinedstorage.api.storage.disk.StorageDiskType;
import com.thepoptartcrpr.extradisks.ExtraDisks;
import com.thepoptartcrpr.extradisks.api.RSApiHelper;
import com.thepoptartcrpr.extradisks.types.StorageType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EDStorageDisk extends Item implements IStorageDiskProvider {

    public EDStorageDisk() {
        super();

        this.setMaxStackSize(1);
        this.setMaxDamage(1);
        this.setHasSubtypes(true);

        this.setUnlocalizedName("storage_disk");
        this.setRegistryName(new ResourceLocation(ExtraDisks.Reference.MODID, "storage_disk"));
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.onUpdate(stack, world, entity, slot, selected);

        if (!world.isRemote) {
            if (!stack.hasTagCompound()) {
                UUID id = UUID.randomUUID();

                IStorageDisk disk = RSApiHelper.api.createDefaultItemDisk(world, getCapacity(stack));
                RSApiHelper.api.getStorageDiskManager(world).set(id, disk);
                RSApiHelper.api.getStorageDiskManager(world).markForSaving();

                this.setId(stack, id);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (this.isValid(stack)) {
            UUID id = this.getId(stack);

            RSApiHelper.api.getStorageDiskSync().sendRequest(id);
            IStorageDiskSyncData data = RSApiHelper.api.getStorageDiskSync().getData(id);

            if (data == null) return;

            if (data.getCapacity() == -1) tooltip.add(I18n.format("misc.refinedstorage:storage.stored", RSApiHelper.api.getQuantityFormatter().format(data.getStored())));
            else tooltip.add(I18n.format("misc.refinedstorage:storage.stored_capacity", RSApiHelper.api.getQuantityFormatter().format(data.getStored()), RSApiHelper.api.getQuantityFormatter().format(data.getCapacity())));
        }
    }

    @Override
    public boolean isValid(ItemStack disk) {
        return disk.hasTagCompound() && disk.getTagCompound().hasUniqueId("Id");
    }

    @Override
    public int getCapacity(ItemStack disk) {
        return StorageType.getById(disk.getItemDamage()).getCapacity();
    }

    @Override
    public UUID getId(ItemStack disk) {
        return disk.getTagCompound().getUniqueId("Id");
    }

    @Override
    public void setId(ItemStack disk, UUID id) {
        disk.setTagCompound(new NBTTagCompound());
        disk.getTagCompound().setUniqueId("Id", id);
    }

    @Override
    public StorageDiskType getType() {
        return StorageDiskType.ITEM;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < StorageType.values().length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        for (int i = 0; i < StorageType.values().length; i++) {
            if (stack.getItemDamage() == i) return this.getUnlocalizedName() + "." + StorageType.values()[i].getName();
        }
        return null;
    }

}
