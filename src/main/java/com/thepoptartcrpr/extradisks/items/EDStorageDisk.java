package com.thepoptartcrpr.extradisks.items;

import com.raoulvdberge.refinedstorage.api.storage.IStorage;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.raoulvdberge.refinedstorage.api.storage.disk.StorageDiskType;
import com.thepoptartcrpr.extradisks.ExtraDisks;
import com.thepoptartcrpr.extradisks.api.RSApiHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EDStorageDisk extends Item implements IStorageDiskProvider {

    public EDStorageDisk() {
        super();

        this.setMaxStackSize(1);
        this.setMaxDamage(1);

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

                this.setId(stack, id);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isValid(ItemStack disk) {
        return disk.hasTagCompound() && disk.getTagCompound().hasUniqueId("Id");
    }

    @Override
    public int getCapacity(ItemStack disk) {
        return 256000;
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

}
