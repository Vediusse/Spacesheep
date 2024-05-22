package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.error.OwnershipException;
import io.github.zeculesu.itmo.prog5.models.MeleeWeapon;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Удаление элемента из коллекции по его оружию ближнего боя
 */
public class RemoveByWeaponCommand extends AbstractCommand {
    public RemoveByWeaponCommand() {
        super("remove_all_by_melee_weapon", "remove_all_by_melee_weapon meleeWeapon : удалить из коллекции все элементы, значение поля meleeWeapon которого эквивалентно заданному", false, true);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        if (args.length == 0) {
            response.setError("Не введен аргумент - оружие ближнего боя");
            return response;
        }
        try {
            MeleeWeapon meleeWeapon = MeleeWeapon.getMeleeWeaponByName(args[0]);
            int start = collectionSpaceMarine.size();
            collectionSpaceMarine.removeAllByMeleeWeapon(meleeWeapon);
            int end = collectionSpaceMarine.size();
            if (start == end) {
                response.setError("Элементов с таким оружием ближнего боя в коллекции не найдено");
            } else {
                response.setMessage("Удаление произошло успешно");
            }
        } catch (NamingEnumException | OwnershipException e) {
            response.setError(e.getMessage());
        }
        return response;
    }
}
