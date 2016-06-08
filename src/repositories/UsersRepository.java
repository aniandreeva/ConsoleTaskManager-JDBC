package repositories;

import java.sql.*;

import models.User;

public class UsersRepository extends BaseRepository<User> {

    public UsersRepository() {
        super();
        classT = User.class;
        this.table = "users";
    }

    protected void readItem(ResultSet rs, User user) {
        try {
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void insert(User user) {
        try {
            String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void update(User user) {
        try {
            String sql = "UPDATE users SET username=?, password=? WHERE id =?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getByUsernameAndPassword(String username, String password) {
        return this.getAll().stream().filter((User) -> User.getUsername().equals(username) && User.getPassword().equals(password)).findFirst().orElse(null);
    }
}