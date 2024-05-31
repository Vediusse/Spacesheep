package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Coordinates;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;

import java.util.ArrayList;
import java.util.HashMap;

public class GetLoginCoords extends AbstractCommand {
    public GetLoginCoords() {
        super("getlogincoords", "getlogincoords : получить список всех координат каждого пользователя", false, false);
    }


    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        HashMap<String, ArrayList<Coordinates>> loginCoord = new HashMap<>();
        for (SpaceMarine o : collectionSpaceMarine){
            String login = o.getOwner();
            Coordinates coord = o.getCoordinates();
            if (loginCoord.containsKey(login)){
                loginCoord.get(login).add(coord);
            }
            else {
                ArrayList<Coordinates> coordList = new ArrayList<>();
                coordList.add(coord);
                loginCoord.put(login, coordList);
            }
        }
        response.setLoginCoord(loginCoord);
        return response;
    }
}
