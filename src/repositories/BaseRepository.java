package repositories;

import models.BaseModel;

import java.sql.*;
import java.util.ArrayList;

public abstract class BaseRepository <T extends BaseModel> {
    Connection connection;
    protected String table;
    protected Class<T> classT;

    public BaseRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/TaskManager" + "?verifyServerCertificate=false" + "&useSSL=true" + "&requireSSL=false", "root", "pass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract void readItem(ResultSet resultSet, T item);

    public T getById(int id) {
        return getAll().stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public ArrayList<T> getAll() {
        ArrayList<T> items = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM " + table;
            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                T item = classT.newInstance();
                readItem(rs, item);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return items;
    }

    protected abstract void insert(T item);

    protected abstract void update(T item);

    public void save(T item) {
        if (item.getId() > 0) {
            update(item);
        } else {
            insert(item);
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM " + table + " WHERE id=" + id;
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}