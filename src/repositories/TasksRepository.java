package repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import enums.TaskStatus;
import models.Task;

public class TasksRepository extends BaseRepository<Task> {

    public TasksRepository() {
        super();
        classT = Task.class;
        this.table = "tasks";
    }

    protected void readItem(ResultSet rs, Task task) {
        try {
            task.setId(rs.getInt("id"));
            task.setUserId(rs.getInt("userId"));
            task.setTitle(rs.getString("title"));
            task.setContent(rs.getString("content"));
            task.setTaskStatus(TaskStatus.values()[rs.getInt("status")]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void insert(Task task) {
        try {
            String sql = "INSERT INTO tasks(userId, title, content, status) VALUES (?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getContent());
            stmt.setInt(4, task.getTaskStatus().ordinal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void update(Task task) {
        try {
            String sql = "UPDATE tasks SET userId=?, title=?, content=?, status=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getContent());
            stmt.setInt(4, task.getTaskStatus().ordinal());
            stmt.setInt(5, task.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getTasksByUserId(int id) {
        ArrayList<Task> tasks = getAll().stream().filter(t -> t.getUserId() == id).collect(Collectors.toCollection(ArrayList<Task>::new));
        return tasks;
    }

    public Task getByTitleAndContent(String title, String content) {
        return getAll().stream().filter((Task) -> Task.getTitle().equals(title) && Task.getContent().equals(content)).findFirst().orElse(null);
    }
}