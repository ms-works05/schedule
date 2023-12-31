package jp.gihyo.projava.tasklist;

import jp.gihyo.projava.tasklist.HomeController.TaskItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskListDao {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(TaskItem taskItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasklist");
        insert.execute(param);
    }

    public List<TaskItem> findAll() {
        String query = "SELECT * FROM tasklist";
        List<Map<String, Object>> resurt = jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = resurt.stream()
                .map((Map<String, Object> row) -> new TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("memo").toString(),
                        row.get("deadline").toString(),
                        (Boolean) row.get("done")))
                .toList();
        return taskItems;
    }

    public int update(TaskItem taskItem) {
        int number = jdbcTemplate.update(
                "UPDATE tasklist SET task = ?, memo = ?, deadline = ?, done = ? WHERE id = ?",
                taskItem.task(),
                taskItem.memo(),
                taskItem.deadline(),
                taskItem.done(),
                taskItem.id());
        return number;
    }

    public int delete(String id) {
        int number = jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?", id);
        return number;
    }

    public List<TaskItem>search(String month, String chk1) {
        String query;
        if (chk1.equals("0")) {
            query = "SELECT * FROM tasklist where deadline like '" + month + "%' and done=false ";
        } else {
            query = "SELECT * FROM tasklist where deadline like '" + month + "%'";
        }
        List<Map<String, Object>> resurt = jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = resurt.stream()
                .map((Map<String, Object> row) -> new TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("memo").toString(),
                        row.get("deadline").toString(),
                        (Boolean) row.get("done")))
                .toList();
        return taskItems;
    }
}
