import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Duke {
    private ArrayList<Task> tasks;
    private int counter;

    private Parser p;
    
    public Duke() {
        this.tasks = new ArrayList<Task>();
        this.counter = 0;
        this.p = new Parser();
    }
    public static void main(String[] args) {
        Duke d = new Duke();

        d.runBot();
    }

    /**
     * Greets the user when the bot is started.
     */
    private void greet() {
        System.out.println("Hello, I'm Baymin");
        System.out.println("What can I do for you?");
    }

    /**
     * Says goodbye to the user when the bot has finished running.
     */
    private void bye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints out a confirmation message when a task is added successfully.
     * @param task is the name of the task.
     */
    private void echo(String task) {
        System.out.println("added: " + task);

    }

    /**
     * Adds a new task to the list of tasks, provided that the task details pass
     * the checks.
     *
     * @param name is details of the task.
     */
    private void addTask(String name) {
        String[] details = name.split(" ");
        String type = details[0];
        if (!checkType(type)) {
            System.out.println("please enter a valid task type");
            return;
        } else {
            switch(type) {
                case "todo" :
                    addToDo(name);
                    break;
                case "deadline":
                    addDeadline(name);
                    break;
                default:
                    addEvent(name);
                    break;
            };

        }



    }

    /**
     * Checks if the string for the name or date is blank.
     * @param s is the string that represents the dates or name
     *          of a task.
     * @return a boolean value to indicate if the string is blank.
     */
    private boolean checkBlankString(String s) {
        return s.trim().isEmpty();
    }

    /**
     * Adds a todo task to the list of tasks after verifying its details.
     * @param name is the details of a todo task.
     */
    private void addToDo(String name) {
        String[] lst = name.split("todo");
        if (lst.length == 0 || checkBlankString(lst[1])) {
            System.out.println("Don't leave the task description blank");
        } else {
            Task t = new ToDo(lst[1]);
            this.tasks.add(t);
            this.counter += 1;
            this.echo(lst[1]);
        }

    }


    /**
     * Adds a deadline task to the list of tasks after verifying its details.
     * @param name is the details of a Deadline task.
     */
    private void addDeadline(String name) {
        //todo, use parser to take care of blank inputs
        String[] lst = name.split("deadline");
        System.out.println("Please enter due date");
        String date = this.p.getDate();
        Task t = new Deadline(name, date);
        this.tasks.add(t);
        this.counter += 1;
        this.echo(name);
    }

    /**
     * Adds a Event task to the list of tasks after verifying its details.
     * @param name is the details of a Event task.
     */

    private void addEvent(String name) {
        //todo, use parser to take care of blank inputs
        String[] lst = name.split("event");
        System.out.println("Please enter a start date");
        String start = this.p.getDate();
        System.out.println("Please enter a end date");
        String end = this.p.getDate();
        Task t = new Event(name, start + " " + end);
        this.tasks.add(t);
        this.counter += 1;
        this.echo(name);
    }


    /**
     * Prints out all the tasks
     */
    private void listTask() {
        for (int i = 0; i < counter; i++) {
            System.out.println(i + 1 + "." + this.tasks.get(i));
        }
        this.saveTask();
        return;
    }

    /**
     * Saves the tasks to a .txt file
     */
    private void saveTask() {
        File f = new File("data/tasks.txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            f.getParentFile().mkdirs();
            saveTask();

        }
        System.out.println(f.getAbsoluteFile());
        updateFile("data/tasks.txt");
    }

    private static void writeToFile(String filePath, String textToAdd) throws IOException {

        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }

    public void updateFile (String filename){
        try {
            for (int i = 0; i < counter; i++) {
                writeToFile(filename, this.tasks.get(i) + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }






    /**
     * Checks if the type of task entered matches the available types.
     * @param cmd is the String representing the task type.
     * @return a boolean value depending on whehter the type is valid.
     */
    private boolean checkType(String cmd) {
        String[] cmds = {"todo", "event", "deadline"};
        return Arrays.stream(cmds).anyMatch(cmd::equals);
    }

    /**
     * Removes a task from the list.
     * @param pos is the index position of the task in the list, note that the index entered
     *            is based on 1-based index rather than 0-based index.
     */
    private void delete(int pos) {
        pos -= 1;
        if (this.tasks.get(pos) == null) {
            System.out.println("Please enter a valid index");
        } else {
            this.tasks.remove(pos);
            this.counter -= 1;
        }

    }

    /**
     * Starts the execution of the bot.
     */
    public void runBot() {
        Scanner s = new Scanner(System.in);
        this.greet();
        boolean active = true;

        while (active) {
            String cmd = s.nextLine();
            if (cmd.equals("bye")) {
                active = false;
                this.bye();
            } else if (cmd.equals("list")) {

                this.listTask();
            } else if (cmd.contains("unmark")) {
                    String[] lst = cmd.split(" ");
                    int pos = Integer.parseInt(lst[1]);
                    this.tasks.get(pos - 1).undoTask();
            } else if (cmd.contains("mark")) {
                String[] lst = cmd.split(" ");
                int pos = Integer.parseInt(lst[1]);
                this.tasks.get(pos - 1).doTask();
            } else {
                this.addTask(cmd);
            }

        }

    }

}


abstract class Task {

    private String name;

    private boolean done;

    public Task(String name) {
        this.name = name;
        this.done = false;
    }

    @Override
    public String toString() {
        if (done) {
            return "[X]" + this.name;
        } else {
            return "[ ]" + this.name;
        }

    }

    /**
     * Changes the satus of a task from not done to done
     */
    public void doTask() {
        if (!done) {
            this.done = true;
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(this.toString());
        } else {
            System.out.println("This task is already done");
        }

    }

    /**
     * Changes the satus of a task from done to not done
     */
    public void undoTask() {
        if (done) {
            this.done = false;
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println(this.toString());
        } else {
            System.out.println("This task has not been done");

        }

    }


}

class ToDo extends Task{

    public ToDo(String name) {
        super(name);
    }


    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}


class Deadline extends Task{

    private String deadline;

    public Deadline(String name, String deadline) {
        super(name);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + "(by: " + this.deadline + ")";
    }
}

class Event extends Task{

    private String time;

    public Event(String name, String time) {
        super(name);
        this.time = time;
    }


    @Override
    public String toString() {
        return "[E]" + super.toString() + this.time;
    }
}

class Parser {
    Scanner input;

    public Parser() {
        this.input = new Scanner(System.in);
    }

    public String getDate() {
        System.out.println("Please enter the date in the format of yyyy--mm--dd");
        while (true) {
            try {
                LocalDate date = LocalDate.parse(input.next());
                return date.toString();
            } catch (DateTimeException e) {
                System.out.println("Please enter the date of the correct format");
            }

        }
    }

    public String getTaskDescription() {
        System.out.println("Please enter a task description");
        while (true) {
            String taskDescription = input.nextLine();
            if (taskDescription.isEmpty()) {
                System.out.println("Task description cannot be blank");
            } else {
                return taskDescription;
            }
        }

    }

    

    public String getTaskType(String taskDescription) {
        System.out.println("Please enter a task type");
        ArrayList<String> lst = new ArrayList<>();
        lst.add("event");
        lst.add("todo");
        lst.add("deadline");
        while (true) {
            String taskType = taskDescription;
            if (!lst.contains(taskType)) {
                System.out.println("Please enter a valid command");
                taskDescription = input.nextLine();
            } else {
                return taskDescription;
            }
        }
    }


}




