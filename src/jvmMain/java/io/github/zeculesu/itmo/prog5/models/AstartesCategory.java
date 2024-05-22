package io.github.zeculesu.itmo.prog5.models;

import io.github.zeculesu.itmo.prog5.error.NamingEnumException;

import java.util.Map;

import static kotlin.collections.ArraysKt.associateBy;

/**
 * Описывает возможные категории для SpaceMarine
 */
public enum AstartesCategory {
    SCOUT("SCOUT"),
    SUPPRESSOR("SUPPRESSOR"),
    LIBRARIAN("LIBRARIAN"),
    HELIX("HELIX");

    private final String astartesCategoryName;
    private static final Map<String, AstartesCategory> name2instance = associateBy(AstartesCategory.values(), v -> v.astartesCategoryName);

    AstartesCategory(String astartesCategoryName) {
        this.astartesCategoryName = astartesCategoryName;
    }

    public static AstartesCategory getCategoryByName(String astartesCategoryName) throws NamingEnumException{
        if (name2instance.get(astartesCategoryName) == null){
            throw new NamingEnumException("Неправильное имя для категория");
        }
        return name2instance.get(astartesCategoryName);
    }
}
