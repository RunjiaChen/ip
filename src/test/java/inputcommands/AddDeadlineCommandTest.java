package inputcommands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import snomexceptions.InvalidCommandDateFormatException;
import snomexceptions.InvalidCommandTaskDescException;
import snomtasklist.TaskList;

public class AddDeadlineCommandTest {

    @Test
    public void addDeadlineCommand_blankTaskDescription_exceptionThrown() {
        TaskList lst = TaskList.makeTaskList();
        Throwable t = Assertions.assertThrows(InvalidCommandTaskDescException.class, ()
                 -> Command.makeCommand("deadline ").execute(lst));
        Assertions.assertEquals("Please do not leave your task description or date blank",
                t.getMessage());

    }

    @Test
    public void addDeadlineCommand_blankTaskDescriptionWithSlash_exceptionThrown() {
        TaskList lst = TaskList.makeTaskList();
        Throwable t = Assertions.assertThrows(InvalidCommandTaskDescException.class, ()
                 -> Command.makeCommand("deadline / ").execute(lst));
        Assertions.assertEquals("Please do not leave your task description or date blank",
                t.getMessage());

    }

    @Test
    public void addDeadlineCommand_blankDate_exceptionThrown() {
        TaskList lst = TaskList.makeTaskList();
        Throwable t = Assertions.assertThrows(InvalidCommandTaskDescException.class, ()
                 -> Command.makeCommand("deadline abc/ ").execute(lst));
        Assertions.assertEquals("Please do not leave your task description or date blank",
                t.getMessage());

    }


    @Test
    public void addDeadlineCommand_invalidDateFormat_exceptionThrown() {
        TaskList lst = TaskList.makeTaskList();
        Throwable t = Assertions.assertThrows(InvalidCommandDateFormatException.class, ()
                 -> Command.makeCommand("deadline abc/efg ").execute(lst));
        Assertions.assertEquals("Please ensure that your date(s) is of the format yyyy-mm-dd",
                t.getMessage());

    }
}
