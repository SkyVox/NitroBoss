package com.skydhs.boss.utils.nbt;

import org.bukkit.inventory.ItemStack;

abstract class NBT {
    private Object nmsItem /* This represents the NMS_ItemStack. */, compound /* This will represent NBTCompound. */;

    protected NBT(ItemStack item) {
        if (item == null) throw new NullPointerException("ItemStack cannot be null.");
        this.nmsItem = NBTUtil.ITEM_NMS_COPY.run(NBTUtil.CRAFT_ITEM_STACK.getDeclaredClass(), item);
    }

    /**
     * Get the current nmsItem compound.
     * *This can be null if this item
     * hasn't key yet*
     *
     * @return current nmsItem compound.
     */
    public Object getCompound() {
        if (this.compound != null) return compound;
        return NBTUtil.ITEM_GET_TAG.run(nmsItem);
    }

    /**
     * Change the nmsItem compound.
     *
     * @param compound compound to be set.
     */
    public void setCompound(Object compound) {
        this.compound = compound;
    }

    /**
     * Check if the current compound
     * contains the given key.
     *
     * @param key key to search.
     * @return if has key.
     */
    public Boolean hasKey(final String key) {
        Boolean ret = (Boolean) getData(this, NBTUtil.COMPOUND_HAS_KEY, key);
        return ret == null ? Boolean.FALSE : ret;
    }

    /**
     * Get stored float.
     *
     * @param key key to search.
     * @return stored float.
     */
    public Float getFloat(final String key) {
        Float ret = (Float) getData(this, NBTUtil.COMPOUND_GET_FLOAT, key);
        return ret == null ? 0F : ret;
    }

    /**
     * Insert float into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setFloat(final String key, Float value) {
        setData(this, NBTUtil.COMPOUND_SET_FLOAT, key, value);
    }

    /**
     * Get stored string.
     *
     * @param key key to search.
     * @return stored string.
     */
    public String getString(final String key) {
        String ret = (String) getData(this, NBTUtil.COMPOUND_GET_STRING, key);
        return ret == null ? "" : ret;
    }

    /**
     * Insert string into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setString(final String key, String value) {
        setData(this, NBTUtil.COMPOUND_SET_STRING, key, value);
    }

    /**
     * Get stored integer.
     *
     * @param key key to search.
     * @return stored integer.
     */
    public Integer getInt(final String key) {
        Integer ret = (Integer) getData(this, NBTUtil.COMPOUND_GET_INT, key);
        return ret == null ? 0 : ret;
    }

    /**
     * Insert integer into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setInt(final String key, Integer value) {
        setData(this, NBTUtil.COMPOUND_SET_INT, key, value);
    }

    /**
     * Get stored byte[].
     *
     * @param key key to search.
     * @return stored byte[]
     */
    public byte[] getByteArray(final String key) {
        byte[] ret = (byte[]) getData(this, NBTUtil.COMPOUND_GET_BYTE_ARRAY, key);
        return ret == null ? new byte[0] : ret;
    }

    /**
     * Insert byte[] into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setByteArray(final String key, byte[] value) {
        setData(this, NBTUtil.COMPOUND_SET_BYTE_ARRAY, key, value);
    }

    /**
     * Get stored int[].
     *
     * @param key key to search.
     * @return stored int[].
     */
    public int[] getIntArray(final String key) {
        int[] ret = (int[]) getData(this, NBTUtil.COMPOUND_GET_INT_ARRAY, key);
        return ret == null ? new int[0] : ret;
    }

    /**
     * Insert int[] into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setIntArray(final String key, int[] value) {
        setData(this, NBTUtil.COMPOUND_SET_INT_ARRAY, key, value);
    }

    /**
     * Get stored Long.
     *
     * @param key key to search.
     * @return stored long.
     */
    public Long getLong(final String key) {
        Long ret = (Long) getData(this, NBTUtil.COMPOUND_GET_LONG, key);
        return ret == null ? 0L : ret;
    }

    /**
     * Insert Long into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setLong(final String key, Long value) {
        setData(this, NBTUtil.COMPOUND_SET_LONG, key, value);
    }

    /**
     * Get stored Short.
     *
     * @param key key to search.
     * @return stored short.
     */
    public Short getShort(final String key) {
        Short ret = (Short) getData(this, NBTUtil.COMPOUND_GET_SHORT, key);
        return ret == null ? 0 : ret;
    }

    /**
     * Insert Short into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setShort(final String key, Short value) {
        setData(this, NBTUtil.COMPOUND_SET_SHORT, key, value);
    }

    /**
     * Get stored Byte.
     *
     * @param key key to search.
     * @return stored byte
     */
    public Byte getByte(final String key) {
        Byte ret = (Byte) getData(this, NBTUtil.COMPOUND_GET_BYTE, key);
        return ret == null ? 0 : ret;
    }

    /**
     * Insert Byte into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setByte(final String key, Byte value) {
        setData(this, NBTUtil.COMPOUND_SET_BYTE, key, value);
    }

    /**
     * Get stored Double.
     *
     * @param key key to search.
     * @return stored double.
     */
    public Double getDouble(final String key) {
        Double ret = (Double) getData(this, NBTUtil.COMPOUND_GET_DOUBLE, key);
        return ret == null ? 0D : ret;
    }

    /**
     * Insert Double into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setDouble(final String key, Byte value) {
        setData(this, NBTUtil.COMPOUND_SET_DOUBLE, key, value);
    }

    /**
     * Get stored Boolean.
     *
     * @param key key to search.
     * @return stored boolean.
     */
    public Boolean getBoolean(final String key) {
        Boolean ret = (Boolean) getData(this, NBTUtil.COMPOUND_GET_BOOLEAN, key);
        return ret == null ? Boolean.FALSE : ret;
    }

    /**
     * Insert Boolean into the @nmsItem.
     *
     * @param key key/finder to set.
     * @param value value to set.
     */
    public void setBoolean(final String key, Boolean value) {
        setData(this, NBTUtil.COMPOUND_SET_BOOLEAN, key, value);
    }

    /**
     * Get Compound for the current nmsItem.
     *
     * @param str finder.
     * @return nmsItem compound.
     */
    public Object getCompound(final String str) {
        return getData(this, NBTUtil.COMPOUND_GET_COMPOUND, str);
    }

    public void merge(Object newCompound) {
        Object compound = getCompound();

        if (compound == null && newCompound != null) {
            NBTUtil.ITEM_SET_TAG.run(nmsItem, newCompound);
        } else {
            if (newCompound == null) return;
            NBTUtil.COMPOUND_MERGE.run(compound, newCompound);
        }
    }

    /**
     * Get the NBTBase based on the given
     * search key.
     *
     * @param str String to search.
     * @return NBTBase class.
     */
    public Object getNBTBase(final String str) {
        return getData(this, NBTUtil.COMPOUND_GET, str);
    }

    /**
     * Set new NBTBase for the current nmsItem.
     *
     * @param str String to search.
     * @param nbtBase new NBTBase.
     */
    public void setNBTBase(final String str, Object nbtBase) {
        setData(this, NBTUtil.COMPOUND_SET, str, nbtBase);
    }

    /**
     * Converts the modified nmsItem to
     * {@link org.bukkit.inventory.ItemStack}
     * and return it.
     *
     * @return modified itemStack.
     */
    public ItemStack getItem() {
        return (ItemStack) NBTUtil.ITEM_BUKKIT_COPY.run(nmsItem, nmsItem);
    }

    /**
     * Get raw itemStack.
     * @return Non modified itemStack.
     */
    public abstract ItemStack getRawItem();

    /**
     * Set a specific data on the nmsItem.
     *
     * @param nbt Current NBT (item).
     * @param type NMS Class and method to
     *             run.
     * @param key key/finder to set.
     * @param object object to insert into
     *               the nmsItem.
     */
    private static void setData(NBT nbt, NBTUtil type, final String key, Object object) {
        Object compound = nbt.getCompound();
        if (compound == null) compound = NBTUtil.COMPOUND.getNewInstance();
        type.run(compound, key, object);

        // Set tag for the current nmsItem and update compound.
        NBTUtil.ITEM_SET_TAG.run(nbt.nmsItem, compound);
        nbt.setCompound(compound);
    }

    /**
     * This will run through NMS classes and
     * search for the given key using the
     * {@link NBTUtil >type<} to specify the
     * NMS class and method.
     *
     * @param nbt Current NBT (item).
     * @param type NMS Class and method to
     *             run.
     * @param key key to be searched.
     * @return the stored data.
     */
    private static Object getData(NBT nbt, NBTUtil type, final String key) {
        Object compound = nbt.getCompound();
        if (compound == null) return null;
        return type.run(compound, key);
    }
}