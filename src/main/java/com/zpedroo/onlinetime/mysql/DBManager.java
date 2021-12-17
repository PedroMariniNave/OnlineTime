package com.zpedroo.onlinetime.mysql;

import com.zpedroo.onlinetime.objects.PlayerData;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class DBManager {

    public void saveData(PlayerData data) {
        if (contains(data.getUUID().toString(), "uuid")) {
            String query = "UPDATE `" + DBConnection.TABLE + "` SET" +
                    "`uuid`='" + data.getUUID().toString() + "', " +
                    "`played_time`='" + data.getTotalOnlineTime() + "', " +
                    "`first_join`='" + data.getFirstJoinTime() + "', " +
                    "`spent_points`='" + data.getSpentPoints() + "' " +
                    "WHERE `uuid`='" + data.getUUID().toString() + "';";
            executeUpdate(query);
            return;
        }

        String query = "INSERT INTO `" + DBConnection.TABLE + "` (`uuid`, `played_time`, `first_join`, `spent_points`) VALUES " +
                "('" + data.getUUID().toString() + "', " +
                "'" + data.getTotalOnlineTime() + "', " +
                "'" + data.getFirstJoinTime() + "', " +
                "'" + data.getSpentPoints() + "');";
        executeUpdate(query);
    }

    public PlayerData loadData(Player player) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.TABLE + "` WHERE `uuid`='" + player.getUniqueId().toString() + "';";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                UUID uuid = UUID.fromString(result.getString(1));
                Long playedTime = result.getLong(2);
                Long firstJoinTime = result.getLong(3);
                Long spentPoints = result.getLong(4);

                return new PlayerData(uuid, playedTime, firstJoinTime, spentPoints);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return new PlayerData(player.getUniqueId(), 0L, System.currentTimeMillis(), 0L);
    }

    public List<PlayerData> getTop() {
        List<PlayerData> top = new ArrayList<>(10);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.TABLE + "` ORDER BY `played_time` DESC LIMIT 10;";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString(1));
                Long playedTime = result.getLong(2);
                Long firstJoinTime = result.getLong(3);
                Long spentPoints = result.getLong(4);

                top.add(new PlayerData(uuid, playedTime, firstJoinTime, spentPoints));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return top;
    }

    private Boolean contains(String value, String column) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT `" + column + "` FROM `" + DBConnection.TABLE + "` WHERE `" + column + "`='" + value + "';";
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return false;
    }

    private void executeUpdate(String query) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, null, null, statement);
        }
    }

    private void closeConnection(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS `" + DBConnection.TABLE + "` (`uuid` VARCHAR(255), `played_time` DECIMAL(40,0), `first_join` LONG, `spent_points` LONG, PRIMARY KEY(`uuid`));";
        executeUpdate(query);
    }

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}