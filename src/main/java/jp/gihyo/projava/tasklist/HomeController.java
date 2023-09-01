package jp.gihyo.projava.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    record  TaskItem(String id, String task, String deadline, boolean done) {}
    private List<TaskItem> taskItems = new ArrayList<>();
    private final TaskListDao dao;
    @Autowired
    HomeController(TaskListDao dao) {
        this.dao = dao;
    }
    @RequestMapping(value="/hello")
    String hello(Model model) {
        model.addAttribute("time",LocalDateTime.now());
        return "hello";
    }
    @GetMapping("/add")
    String addItem(@RequestParam("task")String task,
                   @RequestParam("deadline") String deadline) {
        String id = UUID.randomUUID().toString().substring(0,8);
        TaskItem item = new TaskItem(id, task, deadline, false);
        dao.add(item);
        return "redirect:/list";
    }

    @GetMapping("/list")
    String listItems(Model model) {
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("tasklist", taskItems);
        return "home";
    }
    @GetMapping("/delete")
    String deleteItem(@RequestParam("id") String id) {
        dao.delete(id);
        return "redirect:/list";
    }
    @GetMapping("/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("deadline") String deadline,
                      @RequestParam("done") boolean done) {
        TaskItem taskItem = new TaskItem(id, task, deadline, done);
        dao.update(taskItem);
        return "redirect:/list";
    }
    @GetMapping("/search")
    String search(Model model,
                  @RequestParam(name = "month") String month,
                  @RequestParam(name = "chk1", required = false, defaultValue = "1" ) String chk1) {
        List<TaskItem> taskItems = dao.search(month,chk1);
        model.addAttribute("tasklist", taskItems);
        return "home";
    }
}
