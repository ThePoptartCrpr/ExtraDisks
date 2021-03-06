package com.thepoptartcrpr.extradisks.items;

import com.raoulvdberge.refinedstorage.RSItems;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDiskSyncData;
import com.raoulvdberge.refinedstorage.api.storage.disk.StorageDiskType;
import com.thepoptartcrpr.extradisks.api.RSApiHelper;
import com.thepoptartcrpr.extradisks.init.EDItems;
import com.thepoptartcrpr.extradisks.types.StorageTypes;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EDStorageDisk extends EDItem implements IStorageDiskProvider {

    public EDStorageDisk() {
        super("storage_disk");

        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
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

            if (data != null) tooltip.add(I18n.format("misc.refinedstorage:storage.stored_capacity", RSApiHelper.api.getQuantityFormatter().format(data.getStored()), RSApiHelper.api.getQuantityFormatter().format(data.getCapacity())));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack heldItem = player.getHeldItem(hand);

        if (!world.isRemote) {
            if (player.isSneaking()) {
                IStorageDisk disk = RSApiHelper.api.getStorageDiskManager(world).getByStack(heldItem);

                if (disk != null && disk.getStored() == 0) {
                    ItemStack part = new ItemStack(EDItems.storagePart, heldItem.getCount(), heldItem.getMetadata());

                    if (!player.inventory.addItemStackToInventory(part.copy())) InventoryHelper.spawnItemStack(world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), part);
                }

                RSApiHelper.api.getStorageDiskManager(world).remove(this.getId(heldItem));
                RSApiHelper.api.getStorageDiskManager(world).markForSaving();

                return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(RSItems.STORAGE_HOUSING));
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, heldItem);
    }

    @Override
    public int getEntityLifespan(ItemStack stack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isValid(ItemStack disk) {
        return disk.hasTagCompound() && disk.getTagCompound().hasUniqueId("Id");
    }

    @Override
    public int getCapacity(ItemStack disk) {
        return StorageTypes.getById(disk.getItemDamage()).getCapacity();
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
