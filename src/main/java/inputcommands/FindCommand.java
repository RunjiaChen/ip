package InputCommands;

import SnomExceptions.InvalidCommandException;
import SnomExceptions.InvalidCommandTaskDescException;
import SnomTaskList.TaskList;

class FindCommand extends Command{

    @Override
    public CmdType getType() {
        return CmdType.FIND;
    }


    private FindCommand(String desc) {
        super(desc);
    }

    @Override
    public String execute(TaskList lst) throws InvalidCommandException {
        try {
            String detail = this.desc.toLowerCase().split("find ", 2)[1].trim();
            if (detail.isEmpty()) {
                throw new InvalidCommandTaskDescException();
            }
            return detail;
        } catch (ArrayIndexOutOfBoundsException e) {

            throw new InvalidCommandTaskDescException();
        }

    }
}