package io.github.zeculesu.itmo.prog5.sql;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.error.ElementNotFound;
import io.github.zeculesu.itmo.prog5.models.*;
import org.jetbrains.annotations.NotNull;
import ru.landgrafhomyak.utility.JavaResourceLoader;
import sun.misc.Unsafe;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class JDBCSpaceMarineCollection implements SpaceMarineCollection {
    private final ConnectingDB connection;

    public JDBCSpaceMarineCollection(ConnectingDB connection) {
        this.connection = connection;
    }

    @Override
    public List<String> info() {
        List<String> output = new ArrayList<>();
        output.add("Тип коллекции: база данных PostgreSQl");
        output.add("Количество элементов: " + size());
        return output;
    }

    private final static String SHOW = loadQuery("show.sql");

    @Override
    public List<SpaceMarine> show() {
        List<SpaceMarine> spaceMarines = new ArrayList<>();
        try (Connection con = this.connection.connect()) {
            PreparedStatement ps = con.prepareStatement(SHOW);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                spaceMarines.add(createElem(resultSet));
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
        return spaceMarines;
    }

    private final static String ADD_QUERY = loadQuery("add.sql");

    @Override
    public int add(SpaceMarine o) {
        try (Connection con = this.connection.connect()) {
            PreparedStatement ps = con.prepareStatement(ADD_QUERY);
            fullQueryElem(ps, o);
            ps.setString(11, o.getOwner());

            boolean hasResultSet = ps.execute();
            if (hasResultSet) {
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
            return 0;
        }
        return 0;
    }

    private final static String UPDATE_QUERY = loadQuery("update.sql");

    @Override
    public void update(int id, SpaceMarine o) {
        try (Connection con = this.connection.connect()) {
            try (PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)) {
                fullQueryElem(ps, o);
                ps.setInt(11, id);
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated <= 0) throw new ElementNotFound("Элемент с id=" + id + " не был найден");
            } catch (SQLException e) {
                con.rollback();
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
    }

    private final static String REMOVE_BY_ID_QUERY = loadQuery("removeById.sql");

    @Override
    public boolean removeById(int id) {
        try (Connection con = this.connection.connect()) {
            PreparedStatement ps = con.prepareStatement(REMOVE_BY_ID_QUERY);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
            return false;
        }
    }

    private final static String CLEAR_QUERY = loadQuery("clear.sql");

    @Override
    public void clear() {
        try (PreparedStatement ps = this.connection.connect().prepareStatement(CLEAR_QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
    }

    private final static String REMOVE_HEAD_QUERY = loadQuery("removeHead.sql");

    @Override
    public SpaceMarine removeHead() {
        try (PreparedStatement ps = this.connection.connect().prepareStatement(REMOVE_HEAD_QUERY)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    long coordinatesX = rs.getLong("coordinatesX");
                    float coordinatesY = rs.getFloat("coordinatesY");
                    Date creationDate = new Date(rs.getDate("creationDate").getTime());
                    int health = rs.getInt("health");
                    AstartesCategory category = AstartesCategory.getCategoryByName(rs.getString("category"));
                    Weapon weaponType = Weapon.getWeaponByName(rs.getString("weaponType"));
                    MeleeWeapon meleeWeapon = MeleeWeapon.getMeleeWeaponByName(rs.getString("meleeWeapon"));
                    String chapterName = rs.getString("chapterName");
                    String chapterParentLegion = rs.getString("chapterParentLegion");
                    return new SpaceMarine(id, name, new Coordinates(coordinatesX, coordinatesY),
                            creationDate, health, category, weaponType, meleeWeapon,
                            new Chapter(chapterName, chapterParentLegion));
                }
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
        return null;
    }

    private final static String REMOVE_LOWER_QUERY = loadQuery("removeLower.sql");

    @Override
    public void removeLower(SpaceMarine o) {
        try (PreparedStatement ps = this.connection.connect().prepareStatement(REMOVE_LOWER_QUERY)) {
            ps.setString(1, o.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
    }

    private final static String REMOVE_BY_MELEEWEAPON_QUERY = loadQuery("removeAllByMeleeWeapon.sql");

    @Override
    public void removeAllByMeleeWeapon(MeleeWeapon meleeWeapon) {
        try (PreparedStatement ps = this.connection.connect().prepareStatement(REMOVE_BY_MELEEWEAPON_QUERY)) {
            ps.setString(1, meleeWeapon.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            Unsafe.getUnsafe().throwException(e);
        }
    }

    private final static String FILTER_NAME_QUERY = loadQuery("filterStartsWithName.sql");

    @Override
    public List<SpaceMarine> filterStartsWithName(String name) {
        List<SpaceMarine> spaceMarines = new ArrayList<>();
        try (PreparedStatement ps = this.connection.connect().prepareStatement(FILTER_NAME_QUERY)) {
            ps.setString(1, name + "%");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                SpaceMarine spaceMarine = createElem(resultSet);
                spaceMarines.add(spaceMarine);
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
        return spaceMarines;
    }

    private final static String HEALTH_QUERY = loadQuery("printFieldDescendingHealth.sql");

    @Override
    public String printFieldDescendingHealth() {
        StringJoiner result = new StringJoiner("\n");
        try (PreparedStatement ps = this.connection.connect().prepareStatement(HEALTH_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int value = resultSet.getInt("health");
                result.add(Integer.toString(value));
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
        return result.toString();
    }

    private final static String SIZE_QUERY = loadQuery("size.sql");

    @Override
    public int size() {
        try (PreparedStatement ps = this.connection.connect().prepareStatement(SIZE_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
        return 0;
    }

    private final static String FIND_BY_ID_QUERY = loadQuery("findById.sql");

    @Override
    public SpaceMarine findById(int id) {
        try (PreparedStatement ps = this.connection.connect().prepareStatement(FIND_BY_ID_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return createElem(resultSet);
            }
        } catch (SQLException e) {
            Unsafe.getUnsafe().throwException(e);
        }
        return null;
    }

    @NotNull
    @Override
    public Iterator<SpaceMarine> iterator() {
        return show().iterator();
    }

    private static String loadQuery(String filename) {
        return JavaResourceLoader.loadTextRelativeExitOnFail(JDBCSpaceMarineCollection.class, filename);
    }

    private SpaceMarine createElem(ResultSet resultSet) throws SQLException {
        return new SpaceMarine(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                new Coordinates(
                        resultSet.getLong("coordinatesX"),
                        resultSet.getFloat("coordinatesY")),
                new Date(resultSet.getTimestamp("creationDate").getTime()),
                resultSet.getInt("health"),
                AstartesCategory.getCategoryByName(resultSet.getString("category")),
                Weapon.getWeaponByName(resultSet.getString("weaponType")),
                MeleeWeapon.getMeleeWeaponByName(resultSet.getString("meleeWeapon")),
                new Chapter(
                        resultSet.getString("chapterName"),
                        resultSet.getString("chapterParentLegion")),
                resultSet.getString("owner")
        );
    }

    private void fullQueryElem(PreparedStatement ps, SpaceMarine o) throws SQLException {
        ps.setString(1, o.getName());
        ps.setLong(2, o.getCoordinates().getX());
        ps.setFloat(3, o.getCoordinates().getY());
        ps.setTimestamp(4, new Timestamp(o.getCreationDate().getTime()));
        ps.setInt(5, o.getHealth());
        ps.setString(6, o.getCategory().name());
        ps.setString(7, o.getWeaponType().name());
        ps.setString(8, o.getMeleeWeapon().name());
        ps.setString(9, o.getChapter().getName());
        ps.setString(10, o.getChapter().getParentLegion());
    }

    private final static String INIT_DB_QUERY = loadQuery("initDB.sql");

    public void initDB() {
        try (PreparedStatement ps = this.connection.connect().prepareStatement(INIT_DB_QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            Unsafe.getUnsafe().throwException(e);
        }
    }
}
