package com.skydhs.boss.utils;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.RegistryMaterials;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomEntityRegistry extends RegistryMaterials {

    public static void registerEntity(String name, int id, Class<? extends Entity> customClass) {
        try {
            List<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field field : EntityTypes.class.getDeclaredFields()) {
                if (field.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    field.setAccessible(true);
                    dataMap.add((Map<?, ?>) field.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}