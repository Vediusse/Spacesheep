package io.github.zeculesu.itmo.prog5.data;

import io.github.zeculesu.itmo.prog5.error.ElementNotFound;
import io.github.zeculesu.itmo.prog5.error.EmptyCollectionException;
import io.github.zeculesu.itmo.prog5.models.*;

import java.util.List;

/**
 * Действия, реализуемые с коллекцией
 */
public interface SpaceMarineCollection extends Iterable<SpaceMarine> {
    /**
     * Создается список строк, содержащих главную информацию
     *
     * @return список со строками информации о коллекции
     */
    List<String> info();

    /**
     * Формируется список из элементов коллекции
     *
     * @return список SpaceMarin'ов
     */
    List<SpaceMarine> show();

    int add(SpaceMarine o);

    /**
     * Обновление значений полей имеющегося элемента через его id
     *
     * @param id id элемента для изменения
     * @param o  элемент с новыми полями
     * @throws ElementNotFound в коллекции нет элемента с таким id
     */
    void update(int id, SpaceMarine o) throws ElementNotFound;

    /**
     * Удаление элемента из коллекции по его id, возвращает true при успешном удалении и false при неудаче
     *
     * @param id id элемента
     */
    boolean removeById(int id);

    /**
     * Очищает коллекцию
     */
    void clear();

    /**
     * Удаляет первый элемент коллекции
     *
     * @return возвращает удаленный элемент
     * @throws EmptyCollectionException коллекция пустая
     */
    SpaceMarine removeHead() throws EmptyCollectionException;

    /**
     * Удаляет элементы коллекции, которые меньше указанного
     *
     * @param o элемент для сравнения
     */
    void removeLower(SpaceMarine o);

    /**
     * Удаляет элементы, у которых поле ближнего боя равно указанному
     *
     * @param meleeWeapon параметр ближнего боя
     */
    void removeAllByMeleeWeapon(MeleeWeapon meleeWeapon);

    /**
     * Выбирает элементы, у которых имя начинается с заданной подстроки
     *
     * @param name подстрока для поиска
     * @return элементы, у которых имя начинается с заданной подстроки
     */

    List<SpaceMarine> filterStartsWithName(String name);

    /**
     * Выбирает у всех элементов колллекции поле здоровья
     *
     * @return строка со всеми количествами здоровья в коллекции, идущих по убыванию
     * @throws EmptyCollectionException коллекция пустая
     */

    String printFieldDescendingHealth() throws EmptyCollectionException;

    /**
     * Количество элементов в коллекции
     *
     * @return размер коллекции
     */
    int size();

    /**
     * Получить элемент из коллекции по id
     *
     * @param id id элемента
     * @return элемент SpaceMarine
     * @throws ElementNotFound элемента с таким id нет в коллекции
     */
    SpaceMarine findById(int id) throws ElementNotFound;
}


