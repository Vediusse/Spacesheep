package io.github.zeculesu.itmo.prog5.data;

import io.github.zeculesu.itmo.prog5.error.ElementNotFound;
import io.github.zeculesu.itmo.prog5.error.EmptyCollectionException;
import io.github.zeculesu.itmo.prog5.error.OwnershipException;
import io.github.zeculesu.itmo.prog5.models.MeleeWeapon;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AuthCheckSpaceMarineCollection implements SpaceMarineCollection {
    private SpaceMarineCollection origin;
    private String owner;

    public AuthCheckSpaceMarineCollection(SpaceMarineCollection origin, String owner) {
        this.origin = origin;
        this.owner = owner;
    }

    @Override
    public List<String> info() {
        return this.origin.info();
    }

    @Override
    public List<SpaceMarine> show() {
        return this.origin.show();
    }

    @Override
    public int add(SpaceMarine o) {
        o.setOwner(owner);
        return this.origin.add(o);
    }

    @Override
    public void update(int id, SpaceMarine o) throws ElementNotFound {
        if (findById(id) == null) {
            throw new ElementNotFound("Элемент с id=" + id + " не найден");
        }
        if (this.origin.findById(id).getOwner().equals(owner)) {
            this.origin.update(id, o);
        } else {
            throw new OwnershipException();
        }

        //todo починить дату создания
    }

    @Override
    public boolean removeById(int id) {
        SpaceMarine o = this.origin.findById(id);
        if (o == null) {
            return false;
        }
        if (o.getOwner().equals(owner)) {
            return this.origin.removeById(id);
        }
        throw new OwnershipException();
    }

    @Override
    public void clear() {
        Iterator<SpaceMarine> iterator = this.origin.iterator();
        while (iterator.hasNext()) {
            SpaceMarine element = iterator.next();
            if (element.getOwner().equals(owner)) {
                iterator.remove();
                this.origin.removeById(element.getId());
            }
        }
    }

    @Override
    public SpaceMarine removeHead() throws EmptyCollectionException {
        throw new OwnershipException();
    }

    @Override
    public void removeLower(SpaceMarine other) {
        Iterator<SpaceMarine> iterator = this.origin.iterator();
        while (iterator.hasNext()) {
            SpaceMarine o = iterator.next();
            if (o.compareTo(other) < 0 && o.getOwner().equals(owner)) {
                iterator.remove();
                this.origin.removeById(o.getId());
            }
        }
    }

    @Override
    public void removeAllByMeleeWeapon(MeleeWeapon meleeWeapon) {
        Iterator<SpaceMarine> iterator = this.origin.iterator();
        while (iterator.hasNext()) {
            SpaceMarine o = iterator.next();
            if (o.getMeleeWeapon() == meleeWeapon && o.getOwner().equals(owner)) {
                iterator.remove();
                this.origin.removeById(o.getId());
            }
        }
//        for (SpaceMarine o : this.origin) {
//            if (o.getMeleeWeapon() == meleeWeapon && o.getOwner().equals(owner)) {
//                this.origin.removeById(o.getId());
//            }
//        }
    }

    @Override
    public List<SpaceMarine> filterStartsWithName(String name) {
        return this.origin.filterStartsWithName(name);
    }

    @Override
    public String printFieldDescendingHealth() throws EmptyCollectionException {
        return this.origin.printFieldDescendingHealth();
    }

    @Override
    public int size() {
        return this.origin.size();
    }

    @Override
    public SpaceMarine findById(int id) throws ElementNotFound {
        return this.origin.findById(id);
    }

    @NotNull
    @Override
    public Iterator<SpaceMarine> iterator() {
        return this.origin.iterator();
    }
}
