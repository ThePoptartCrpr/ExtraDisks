package com.thepoptartcrpr.extradisks.types;

import net.minecraft.util.IStringSerializable;

public enum StorageTypes implements IStringSerializable {

    TYPE_256K(0, 256_000, "256k");

    private int id;
    private int capacity;
    private String name;

    StorageTypes(int id, int capacity, String name) {
        this.id = id;
        this.capacity = capacity;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static StorageTypes getById(int id) {
        for (StorageTypes type : StorageTypes.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return null;
    }

}
