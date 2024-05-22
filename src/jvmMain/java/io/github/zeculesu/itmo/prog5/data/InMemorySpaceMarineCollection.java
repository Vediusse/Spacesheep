package io.github.zeculesu.itmo.prog5.data;

import io.github.zeculesu.itmo.prog5.error.ElementNotFound;
import io.github.zeculesu.itmo.prog5.error.EmptyCollectionException;
import io.github.zeculesu.itmo.prog5.error.IdException;

import io.github.zeculesu.itmo.prog5.models.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Класс, описывающий коллекцию из элементов SpaceMarine
 */
public class InMemorySpaceMarineCollection implements SpaceMarineCollection {

    /**
     * Сама коллекция
     */
    private static int nextId = 1;
    private final HashMap<Integer, SpaceMarine> collectionSpaceMarine = new HashMap<Integer, SpaceMarine>();
    /**
     * Дата создания коллекции
     */
    Date dateInitialization;

    /**
     * Изначально создается пустая коллекция и определяется время создания
     */
    public InMemorySpaceMarineCollection() {
        this.dateInitialization = new Date();
    }

    @Override
    public ArrayList<String> info() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Тип коллекции: " + this.collectionSpaceMarine.getClass().getName());
        info.add("Дата инициализации: " + this.dateInitialization.toString());
        info.add("Количество элементов: " + this.collectionSpaceMarine.size());
        return info;
    }

    @Override
    public ArrayList<SpaceMarine> show() {
        return new ArrayList<>(this.collectionSpaceMarine.values());
    }

    public int add(SpaceMarine o) {
        this.collectionSpaceMarine.put(nextId, o);
        o.setId(nextId);
        return nextId++;
    }

    public void add(int id, SpaceMarine o) throws IdException {
        try {
            findById(o.getId());
        } catch (ElementNotFound e) {
            throw new IdException("id не уникальные, элемент с повторяющимся id загружен не будет");
        }
        o.setId(id);
        this.collectionSpaceMarine.put(id, o);
        if (nextId < id) {
            nextId = id + 1;
        }
    }

    @Override
    public void update(int id, SpaceMarine o) throws ElementNotFound {
        SpaceMarine elem = findById(id);
        elem.setName(o.getName());
        elem.setCoordinates(o.getCoordinates());
        elem.setHealth(o.getHealth());
        elem.setCategory(o.getCategory());
        elem.setWeaponType(o.getWeaponType());
        elem.setMeleeWeapon(o.getMeleeWeapon());
        elem.setChapter(o.getChapter());
    }

    @Override
    public boolean removeById(int id) {
        SpaceMarine o = findById(id);
        if (o == null) {
            return false;
        }
        this.collectionSpaceMarine.remove(id);
        return true;
    }

    @Override
    public void clear() {
        this.collectionSpaceMarine.clear();
    }


    public void setNewMaxId() {
        for (SpaceMarine elem : this.collectionSpaceMarine.values()) {
            if (elem.getId() >= nextId) nextId = elem.getId() + 1;
        }
    }

    @Override
    public SpaceMarine removeHead() throws EmptyCollectionException {
        Iterator<Integer> idIter = this.collectionSpaceMarine.keySet().iterator();
        if (!idIter.hasNext()) {
            throw new EmptyCollectionException();
        }
        int id = idIter.next();
        SpaceMarine head = this.collectionSpaceMarine.get(id);
        this.collectionSpaceMarine.remove(id);
        return head;
    }

    @Override
    public void removeLower(SpaceMarine other) {
        for (SpaceMarine spaceMarine : this.collectionSpaceMarine.values()) {
            if (spaceMarine.compareTo(other) < 0) this.collectionSpaceMarine.remove(spaceMarine.getId());
        }
    }

    @Override
    public void removeAllByMeleeWeapon(MeleeWeapon meleeWeapon) {
        for (SpaceMarine spaceMarine : this.collectionSpaceMarine.values()) {
            if (spaceMarine.getMeleeWeapon() == meleeWeapon) this.collectionSpaceMarine.remove(spaceMarine.getId());
        }
    }

    @Override
    public ArrayList<SpaceMarine> filterStartsWithName(String name) {
        return (ArrayList<SpaceMarine>) this.collectionSpaceMarine.values().stream().filter(x
                -> x.getName().startsWith(name)).collect(Collectors.toList());
    }

    @Override
    public String printFieldDescendingHealth() throws EmptyCollectionException {
        if (this.size() == 0) {
            throw new EmptyCollectionException();
        }
        return this.collectionSpaceMarine.values().stream().map(SpaceMarine::getHealth).sorted(Comparator.reverseOrder()).
                map(Object::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public int size() {
        return this.collectionSpaceMarine.size();
    }

    @Override
    public SpaceMarine findById(int id) {
        return this.collectionSpaceMarine.get(id);
    }

    public static int getNextId() {
        return nextId;
    }

    /**
     * Делает коллекцию итерируемой
     *
     * @return итератор
     */
    @NotNull
    @Override
    public Iterator<SpaceMarine> iterator() {
        return this.collectionSpaceMarine.values().iterator();
    }

}
