package seedu.address.logic;

import static seedu.address.logic.parser.DiceCoefficient.diceCoefficient;

import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.BookInventoryParser;
import seedu.address.logic.parser.DiceCoefficient;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.book.Book;
import seedu.address.request.Request;
import seedu.address.request.requestcommands.CommandSecondary;
import seedu.address.request.requestcommands.DeleteRequestCommand;
import seedu.address.request.requestcommands.RequestCommand;
import seedu.address.request.requestcommands.UndoRequestCommand;
import seedu.address.request.requestmodel.RequestModel;
import seedu.address.request.requestparser.RequestListParser;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private static DiceCoefficient diceCoefficient;
    private static final double DICE_COEFFICIENT_THRESHOLD = 0.5;

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Model model;
    private final RequestModel requestModel;
    private final CommandHistory history;
    private final BookInventoryParser bookInventoryParser;
    private final RequestListParser requestListParser;

    public LogicManager(Model model, RequestModel requestModel) {
        this.model = model;
        this.requestModel = requestModel;
        history = new CommandHistory();
        bookInventoryParser = new BookInventoryParser();
        requestListParser = new RequestListParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        diceCoefficient = new DiceCoefficient();
        String[] string = commandText.trim().split("\\s+", 8);
        if (diceCoefficient(string[0], RequestCommand.COMMAND_WORD) > DICE_COEFFICIENT_THRESHOLD
            || diceCoefficient(string[0], DeleteRequestCommand.COMMAND_WORD) > DICE_COEFFICIENT_THRESHOLD + 0.2
            || ((diceCoefficient(string[0], UndoRequestCommand.COMMAND_WORD) > DICE_COEFFICIENT_THRESHOLD)
            && (history.getHistory().get(getHistoryList().size() - 1).contains("request")))) {
            CommandSecondary command = requestListParser.parseCommandRequest(commandText);
            history.add(commandText);
            return command.execute(requestModel, history);
        } else {
            try {
                Command command = bookInventoryParser.parseCommand(commandText);
                return command.execute(model, history);
            } finally {
                history.add(commandText);
            }
        }
    }

    @Override
    public Queue<String> getCompleteIsbn(String isbnText) {
        return model.getCompleteIsbn(isbnText);
    }

    @Override
    public ObservableList<Book> getFilteredBookList() {
        return model.getFilteredBookList();
    }

    @Override
    public ObservableList<Request> getFilteredRequestList() {
        return requestModel.getFilteredRequestList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

    public List<String> getHistoryList () {
        return history.getHistory();
    }
}
