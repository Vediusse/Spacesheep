package io.github.zeculesu.itmo.prog5.data;

import io.github.zeculesu.itmo.prog5.models.MeleeWeapon;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CachedSpaceMarineCollection implements SpaceMarineCollection {
    InMemorySpaceMarineCollection cache;
    SpaceMarineCollection origin;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public CachedSpaceMarineCollection(SpaceMarineCollection origin, InMemorySpaceMarineCollection cache) {
        this.origin = origin;
        this.cache = cache;
    }

    public CachedSpaceMarineCollection(SpaceMarineCollection origin) {
        this.origin = origin;
        this.cache = new InMemorySpaceMarineCollection();
    }

    @Override
    public List<String> info() {
        readLock.lock();
        try {
            return this.origin.info();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<SpaceMarine> show() {
        readLock.lock();
        try {
            return this.cache.show();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int add(SpaceMarine o) {
        writeLock.lock();
        try {
            int id = this.origin.add(o);
            this.cache.add(id, o);
            return id;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void update(int id, SpaceMarine o) {
        writeLock.lock();
        try {
            this.origin.update(id, o);
            this.cache.update(id, o);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean removeById(int id) {
        writeLock.lock();
        try {
            if (this.origin.removeById(id)) {
                this.cache.removeById(id);
                return true;
            }
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            this.origin.clear();
            this.cache.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public SpaceMarine removeHead() {
        writeLock.lock();
        try {
            SpaceMarine o = this.origin.removeHead();
            this.cache.removeById(o.getId());
            return o;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeLower(SpaceMarine o) {
        writeLock.lock();
        try {
            this.origin.removeLower(o);
            this.cache.removeLower(o);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeAllByMeleeWeapon(MeleeWeapon meleeWeapon) {
        writeLock.lock();
        try {
            this.origin.removeAllByMeleeWeapon(meleeWeapon);
            this.cache.removeAllByMeleeWeapon(meleeWeapon);
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public List<SpaceMarine> filterStartsWithName(String name) {
        readLock.lock();
        try {
            return this.cache.filterStartsWithName(name);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String printFieldDescendingHealth() {
        readLock.lock();
        try {
            return this.cache.printFieldDescendingHealth();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return this.cache.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public SpaceMarine findById(int id) {
        readLock.lock();
        try {
            return this.cache.findById(id);
        } finally {
            readLock.unlock();
        }
    }

    @NotNull
    @Override
    public Iterator<SpaceMarine> iterator() {
        return this.cache.iterator();
    }
}
