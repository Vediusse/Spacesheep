package io.github.zeculesu.itmo.prog5.models;

import io.github.zeculesu.itmo.prog5.error.NamingEnumException;

import java.io.Serializable;
import java.util.Map;

import static kotlin.collections.ArraysKt.associateBy;

/**
 * Варианты оружия ближнего боя для SpaceMarine
 */
public enum MeleeWeapon implements Serializable {
    CHAIN_SWORD("CHAIN_SWORD"),
    POWER_SWORD("POWER_SWORD"),
    CHAIN_AXE("CHAIN_AXE"),
    MANREAPER("MANREAPER"),
    POWER_BLADE("POWER_BLADE");
    private final String meleeWeaponName;
    private static final Map<String, MeleeWeapon> name2instance = associateBy(MeleeWeapon.values(), v -> v.meleeWeaponName);

    MeleeWeapon(String meleeWeaponName) {
        this.meleeWeaponName = meleeWeaponName;
    }

    public static MeleeWeapon getMeleeWeaponByName(String meleeWeaponName) throws NamingEnumException {
        if (name2instance.get(meleeWeaponName) == null){
            throw new NamingEnumException("Неправильное имя для оружия ближнего боя");
        }
        return name2instance.get(meleeWeaponName);
    }
}