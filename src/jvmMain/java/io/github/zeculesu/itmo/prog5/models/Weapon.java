package io.github.zeculesu.itmo.prog5.models;

import io.github.zeculesu.itmo.prog5.error.NamingEnumException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import static kotlin.collections.ArraysKt.associateBy;

/**
 * Возможные типы оружия для SpaceMarine
 */
public enum Weapon implements Serializable {
    BOLTGUN("BOLTGUN"),

    HEAVY_BOLTGUN("HEAVY_BOLTGUN"),

    BOLT_RIFLE("BOLT_RIFLE"),

    FLAMER("FLAMER"),

    MULTI_MELTA("MULTI_MELTA");

    private final String weaponName;
    private static final Map<String, Weapon> name2instance = associateBy(Weapon.values(), v -> v.weaponName);

    Weapon(String weaponName) {
        this.weaponName = weaponName;
    }

    public static Weapon getWeaponByName(String weaponName) throws NamingEnumException{
        if (name2instance.get(weaponName) == null){
            throw new NamingEnumException("Неправильное имя для оружия");
        }
        return name2instance.get(weaponName);

    }
}