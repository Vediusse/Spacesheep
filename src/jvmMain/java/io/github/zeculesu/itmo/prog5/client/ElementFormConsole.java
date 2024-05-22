package io.github.zeculesu.itmo.prog5.client;

import io.github.zeculesu.itmo.prog5.data.*;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.models.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Форма ввода элемента
 */
public class ElementFormConsole {
    public static SpaceMarine getElemFromForm(CommandIO console) throws InputFormException, NamingEnumException, IOException {
        String input;
        console.print("Введите имя: ");
        input = console.readln();
        String name = checkName(input);

        console.print("Введите координату (x и y через пробел): ");
        input = console.readln();
        Coordinates coordinates = checkCoordinates(input);

        console.print("Введите количество здоровья: ");
        input = console.readln();
        int health = checkHealth(input);

        console.print("Введите категорию (SCOUT, SUPPRESSOR, LIBRARIAN, HELIX): ");
        input = console.readln();
        AstartesCategory category = checkCategory(input);

        console.print("Введите тип оружия (BOLTGUN, HEAVY_BOLTGUN, BOLT_RIFLE, FLAMER, MULTI_MELTA): ");
        input = console.readln();
        Weapon weaponType = checkWeaponType(input);

        console.print("Введите оружие ближнего боя (CHAIN_SWORD, POWER_SWORD, CHAIN_AXE, MANREAPER, POWER_BLADE): ");
        input = console.readln();
        MeleeWeapon meleeWeapon = checkMeleeWeapon(input);

        console.print("Введите орден (имя и родительским легион через пробел): ");
        input = console.readln();
        Chapter chapter = checkChapter(input);

        return new SpaceMarine(InMemorySpaceMarineCollection.getNextId(), name, coordinates, health, category,
                weaponType, meleeWeapon, chapter);
    }

    public static int checkId(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод id");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InputFormException("id должно быть числом");
        }
    }

    public static String checkName(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод имени");
        return input;
    }

    public static Coordinates checkCoordinates(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод координат");
        String[] coord = input.split(" ");
        if (coord.length != 2) throw new InputFormException("Неправильный ввод координат");

        try {
            Long x = Long.parseLong(coord[0]);
            float y = Float.parseFloat(coord[1]);
            if (y < -67) throw new InputFormException("y должен быть больше -67");
            return new Coordinates(x, y);
        } catch (NumberFormatException e) {
            throw new InputFormException("Неправильный ввод для координат, ожидается x-long, y-float");
        }
    }

    public static Date checkCreationDate(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод даты");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:ss");
            return formatter.parse(input);
        } catch (ParseException e) {
            throw new InputFormException("Невалидный ввод даты");
        }
    }

    public static int checkHealth(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод количества здоровья");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InputFormException("количество здоровья должно быть числом");
        }
    }

    public static AstartesCategory checkCategory(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод категории");
        try {
            return AstartesCategory.getCategoryByName(input);
        } catch (NamingEnumException e) {
            throw new InputFormException(e.getMessage());
        }
    }

    public static Weapon checkWeaponType(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод оружия");
        try {
            return Weapon.getWeaponByName(input);
        } catch (NamingEnumException e) {
            throw new InputFormException(e.getMessage());
        }
    }

    public static MeleeWeapon checkMeleeWeapon(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод оружия ближнего боя");
        try {
            return MeleeWeapon.getMeleeWeaponByName(input);
        } catch (NamingEnumException e) {
            throw new InputFormException(e.getMessage());
        }
    }

    public static Chapter checkChapter(String input) throws InputFormException {
        if (input == null || input.isBlank() || input.isEmpty()) throw new InputFormException("Неправильный ввод ордена");
        String[] ch = input.split(" ");
        if (ch.length != 2) throw new InputFormException("Неправильный ввод ордена");

        try {
            return new Chapter(ch[0], ch[1]);
        } catch (Exception e) {
            throw new InputFormException("Неправильный ввод ордена");
        }
    }
}

