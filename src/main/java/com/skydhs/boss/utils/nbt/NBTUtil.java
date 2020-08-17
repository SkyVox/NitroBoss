package com.skydhs.boss.utils.nbt;

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public enum NBTUtil {
    // Compound *GET* Methods.
    COMPOUND_GET_FLOAT(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getFloat"),
    COMPOUND_GET_STRING(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getString"),
    COMPOUND_GET_INT(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getInt"),
    COMPOUND_GET_BYTE_ARRAY(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getByteArray"),
    COMPOUND_GET_INT_ARRAY(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getIntArray"),
    COMPOUND_GET_LONG(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getLong"),
    COMPOUND_GET_SHORT(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getShort"),
    COMPOUND_GET_BYTE(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getByte"),
    COMPOUND_GET_DOUBLE(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getDouble"),
    COMPOUND_GET_BOOLEAN(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getBoolean"),
    COMPOUND_GET_COMPOUND(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "getCompound"),

    // Compound *SET* Methods.
    COMPOUND_SET_FLOAT(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, float.class }, "setFloat"),
    COMPOUND_SET_STRING(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, String.class }, "setString"),
    COMPOUND_SET_INT(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, int.class }, "setInt"),
    COMPOUND_SET_BYTE_ARRAY(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, byte[].class }, "setByteArray"),
    COMPOUND_SET_INT_ARRAY(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, int[].class }, "setIntArray"),
    COMPOUND_SET_LONG(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, long.class }, "setLong"),
    COMPOUND_SET_SHORT(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, short.class }, "setShort"),
    COMPOUND_SET_BYTE(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, byte.class }, "setByte"),
    COMPOUND_SET_DOUBLE(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, double.class }, "setDouble"),
    COMPOUND_SET_BOOLEAN(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, boolean.class }, "setBoolean"),
    COMPOUND_MERGE(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { ClassWrapper.NBTTAGCOMPOUND.getClazz() }, "a"),
    COMPOUND_GET(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "get"),
    COMPOUND_SET(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class, ClassWrapper.NBTBASE.getClazz() }, "set"),

    // Compound *CONTAINS* Methods.
    COMPOUND_HAS_KEY(ClassWrapper.NBTTAGCOMPOUND.getClazz(), new Class[] { String.class }, "hasKey"),

    // Compound Managements.
    COMPOUND(ClassWrapper.NBTTAGCOMPOUND.getClazz(), null, null),
    CRAFT_ITEM_STACK(ClassWrapper.CRAFT_ITEMSTACK.getClazz(), null, null),

    // ItemStack Managements.
    ITEM_GET_TAG(ClassWrapper.ITEMSTACK.getClazz(), new Class[] {}, "getTag"),
    ITEM_SET_TAG(ClassWrapper.ITEMSTACK.getClazz(), new Class[] { ClassWrapper.NBTTAGCOMPOUND.getClazz() }, "setTag"),
    ITEM_NMS_COPY(ClassWrapper.CRAFT_ITEMSTACK.getClazz(), new Class[] { org.bukkit.inventory.ItemStack.class }, "asNMSCopy"),
    ITEM_BUKKIT_COPY(ClassWrapper.CRAFT_ITEMSTACK.getClazz(), new Class[] { ClassWrapper.ITEMSTACK.getClazz() }, "asBukkitCopy");

    private final Class<?> clazz;
    private String path;
    protected Method method;

    NBTUtil(Class<?> clazz, Class<?>[] methodArguments, String path) {
        this.clazz = Objects.requireNonNull(clazz, "Class cannot be null.");
        this.path = path;

        if (path == null) return;

        try {
            this.method = Objects.requireNonNull(clazz.getMethod(path, methodArguments));
            this.method.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return Class given from enum.
     */
    public Class<?> getDeclaredClass() {
        return clazz;
    }

    /**
     * Creates a new instance of the
     * given enum class.
     *
     * @return new instance of @clazz.
     */
    public Object getNewInstance() {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {}
        return null;
    }

    /**
     * Method name.
     *
     * @return method name.
     */
    public String getPath() {
        return path;
    }

    /**
     * Method.
     * @return The static method.
     */
    protected Method getMethod() {
        return method;
    }

    /**
     * Invoke the @method using the
     * given arguments.
     *
     * @param args given arguments to set on the
     *             method args.
     * @return method return.
     */
    public Object run(Object[] args) {
        return run(clazz, args);
    }

    /**
     * Invoke the @method using the
     * given arguments.
     *
     * @param clazz class to invoke.
     * @param args given arguments to set on the
     *             method args.
     * @return method return.
     */
    public Object run(Object clazz, Object... args) {
        try {
            return this.method.invoke(clazz, args);
        } catch (IllegalAccessException | InvocationTargetException ex) {}
        return null;
    }

    private enum PackageWrapper {
        CRAFTBUKKIT("org.bukkit.craftbukkit"),
        NMS("net.minecraft.server");

        private String uri;

        PackageWrapper(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }
    }

    private enum ClassWrapper {
        NBTTAGCOMPOUND(PackageWrapper.NMS, "NBTTagCompound"),
        NBTBASE(PackageWrapper.NMS, "NBTBase"),
        ITEMSTACK(PackageWrapper.NMS, "ItemStack"),
        CRAFT_ITEMSTACK(PackageWrapper.CRAFTBUKKIT, "inventory.CraftItemStack");

        private PackageWrapper packageWrapper;
        private String method;

        ClassWrapper(PackageWrapper packageWrapper, String method) {
            this.packageWrapper = packageWrapper;
            this.method = method;
        }

        public PackageWrapper getPackageWrapper() {
            return packageWrapper;
        }

        public String getMethod() {
            return method;
        }

        public final Class<?> getClazz() {
            try {
                final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                return Class.forName(packageWrapper.getUri() + '.' + version + '.' + getMethod());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}