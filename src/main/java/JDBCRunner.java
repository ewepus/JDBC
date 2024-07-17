import java.sql.*;

public class JDBCRunner {

    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL_LOCALE_NAME = "localhost:5432/";

    private static final String DATABASE_NAME = "noita";

    public static final String DATABASE_URL = PROTOCOL + URL_LOCALE_NAME + DATABASE_NAME;
    public static final String USER_NAME = "postgres";
    public static final String DATABASE_PASS = "1111";

    public static void main(String[] args) {
        checkDriver();
        checkDB();
        System.out.println("Подключение к базе данных | " + DATABASE_URL + "\n");

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {
            getTopFiveSpells(connection);
            System.out.println();

            getPoorAlchemist(connection);
            System.out.println();

            getInterval(connection);
            System.out.println();

            updateMaxMana(connection, 1, 150);
            System.out.println();

            addSpell(connection, "Волшебная ракета", "Снаряд", 180, 70, 1);
            System.out.println();

            deleteSpellByID(connection, 20);
            System.out.println();

            getAlchemistWands(connection);
            System.out.println();

            getAlchemistSpells(connection);
            System.out.println();

            countSpellType(connection);
            System.out.println();

            totalManaWands(connection);
            System.out.println();

            moreThanAVGMana(connection);

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                System.out.println("Произошло дублирование данных");
            } else throw new RuntimeException(e);
        }
    }

    public static void checkDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }

    public static void checkDB() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
        } catch (SQLException e) {
            System.out.println("Нет базы данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            throw new RuntimeException(e);
        }
    }

    static void getTopFiveSpells(Connection connection) throws SQLException {
        int param0, param3, param4, param5;
        String param1, param2;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM spells\n" +
                "ORDER BY mana_cost DESC\n" +
                "LIMIT 5;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getString(2);
            param2 = rs.getString(3);
            param3 = rs.getInt(4);
            param4 = rs.getInt(5);
            param5 = rs.getInt(6);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4 + " | " + param5);
        }
    }

    static void getPoorAlchemist(Connection connection) throws SQLException {
        int param0;
        String param1;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM alchemists\n" +
                "WHERE id NOT IN (SELECT id_alchemist FROM wands);");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getString(2);
            System.out.println(param0 + " | " + param1);
        }
    }

    static void getInterval(Connection connection) throws SQLException {
        int param0, param1, param2, param3;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM wands\n" +
                "WHERE mana_charge_speed BETWEEN 15 AND 100\n" +
                "ORDER BY mana_charge_speed;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getInt(2);
            param2 = rs.getInt(3);
            param3 = rs.getInt(4);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }
    }

    private static void updateMaxMana(Connection connection, int idAlchemist, int amount) throws SQLException {
        if (amount <= 0) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE wands\n" +
                "SET mana_max = mana_max + ?\n" +
                "WHERE id_alchemist = ?;");
        statement.setInt(1, amount);
        statement.setInt(2, idAlchemist);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " wands");
        getWands(connection);
    }

    static void getWands(Connection connection) throws SQLException {
        int param0, param1, param2, param3;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM wands ORDER BY id;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getInt(2);
            param2 = rs.getInt(3);
            param3 = rs.getInt(4);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }
    }

    private static void addSpell(Connection connection, String name, String type, int damage, int manaCost, int idWand) throws SQLException {
        if (name == null || name.isBlank() || type == null || type.isBlank() || manaCost < 0) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO spells(name, type, damage, mana_cost, id_wand)\n" +
                        "VALUES(?, ?, ?, ?, ?)\n" +
                        "returning id;", Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, name);
        statement.setString(2, type);
        statement.setInt(3, damage);
        statement.setInt(4, manaCost);
        statement.setInt(5, idWand);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор жезла " + rs.getInt(1));
        }

        System.out.println("INSERTed " + count + " wands");
        getSpells(connection);
    }

    static void getSpells(Connection connection) throws SQLException {
        int param0, param3, param4, param5;
        String param1, param2;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM spells ORDER BY id;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getString(2);
            param2 = rs.getString(3);
            param3 = rs.getInt(4);
            param4 = rs.getInt(5);
            param5 = rs.getInt(6);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4 + " | " + param5);
        }
    }

    private static void deleteSpellByID(Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM spells WHERE id = ?;");
        statement.setInt(1, id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " spells");
        getSpells(connection);
    }

    static void getAlchemistWands(Connection connection) throws SQLException {
        int param0, param1, param2;
        String param3;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT wands.id, wands.mana_max, wands.mana_charge_speed," +
                " alchemists.name AS alchemist_name\n" +
                "FROM wands\n" +
                "JOIN alchemists\n" +
                "ON wands.id_alchemist = alchemists.id\n" +
                "ORDER BY wands.id;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getInt(2);
            param2 = rs.getInt(3);
            param3 = rs.getString(4);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }
    }

    static void getAlchemistSpells(Connection connection) throws SQLException {
        String param0, param1;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT alchemists.name, spells.name \n" +
                "FROM alchemists \n" +
                "LEFT JOIN wands ON alchemists.id = wands.id_alchemist \n" +
                "LEFT JOIN spells ON wands.id = spells.id_wand\n" +
                "ORDER BY alchemists.id;");

        while (rs.next()) {
            param0 = rs.getString(1);
            param1 = rs.getString(2);
            System.out.println(param0 + " | " + param1);
        }
    }

    static void countSpellType(Connection connection) throws SQLException {
        String param0;
        int param1;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT type, COUNT(spells.id) \n" +
                "FROM spells\n" +
                "GROUP BY type\n" +
                "ORDER BY count DESC;");

        while (rs.next()) {
            param0 = rs.getString(1);
            param1 = rs.getInt(2);
            System.out.println(param0 + " | " + param1);
        }
    }

    static void totalManaWands(Connection connection) throws SQLException {
        int param0, param1;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT id_wand, sum(mana_cost) as total_mana_cost \n" +
                "FROM spells \n" +
                "GROUP BY id_wand\n" +
                "ORDER BY id_wand;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getInt(2);
            System.out.println(param0 + " | " + param1);
        }
    }

    static void moreThanAVGMana(Connection connection) throws SQLException {
        int param0, param1, param2, param3;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from wands \n" +
                "WHERE mana_max > (SELECT AVG(mana_max) FROM wands)\n" +
                "ORDER BY mana_max;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getInt(2);
            param2 = rs.getInt(3);
            param3 = rs.getInt(4);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }
    }
}
