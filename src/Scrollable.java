import java.sql.*;

public class Scrollable {
    public static void main(String[] args) throws ClassNotFoundException {
        String username = "root";
        String password = "root";
        String connectionUrl = "jdbc:mysql://localhost:3307/traindb?verifyServerCertificate=false&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(connectionUrl, username, password);
             Statement statement = connection.createStatement()) {
            createTable(statement);
            Statement statement1 = readOnlyStatement(connection);
            ResultSet resultSet = statement1.executeQuery("select * from books");
            if (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
            if (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
            if (resultSet.previous()) {
                System.out.println(resultSet.getString("name"));
            }
            System.out.println("====================================");
            // вывод относительно текущей строки на +2 строки
            if (resultSet.relative(2)) {
                System.out.println(resultSet.getString("name"));
            }
            // вывод относительно текущей строки на -2 строки
            if (resultSet.relative(-2)) {
                System.out.println(resultSet.getString("name"));
            }
            // вывод относительно начальной строки
            if (resultSet.absolute(2)) {
                System.out.println(resultSet.getString("name"));
            }
            System.out.println("====================================");
            // вывод первой записи
            if (resultSet.first()) {
                System.out.println(resultSet.getString("name"));
            }
            // вывод последней записи
            if (resultSet.last()) {
                System.out.println(resultSet.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void createTable(Statement statement) throws SQLException {
        statement.execute("drop table if exists books");
        statement.executeUpdate("create table if not exists books (id mediumint primary key auto_increment, name varchar(30))");
        statement.executeUpdate("insert into books (name) values ('Inferno')");
        statement.executeUpdate("insert into books (name) values ('Davincho Code')", Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate("insert into books (name) values ('Solomons Keys')");
    }

    private static Statement readOnlyStatement(Connection connection) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return statement;
    }

    private static PreparedStatement readOnlyPrepareStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return preparedStatement;
    }

    private static Statement writeOnlyStatement(Connection connection) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.FETCH_FORWARD);
        return statement;
    }

    private static PreparedStatement writeOnlyPrepareStatement(Connection connection) throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.FETCH_FORWARD);
        return prepareStatement;
    }
}
