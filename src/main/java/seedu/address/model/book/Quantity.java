package seedu.address.model.book;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.commands.SellCommand.MESSAGE_INVALID_QUANTITY;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Represents a Book's quantity in the inventory book.
 * Guarantees: immutable; is valid as declared in {@link #isValidQuantity(String)}
 */
public class Quantity {

    public static final String MESSAGE_QUANTITY_CONSTRAINTS =
            "Quantity can only take positive values up to 999, and it should not be blank";

    /**
     * Quantity only accepts from 0 to 999
     */
    public static final String QUANTITY_VALIDATION_REGEX = "(\\d{1,3})";

    private String value;
    /**
     * Constructs an {@code Quantity}.
     *
     * @param quantity A valid quantity.
     */
    public Quantity(String quantity) {
        requireNonNull(quantity);
        checkArgument(isValidQuantity(quantity), MESSAGE_QUANTITY_CONSTRAINTS);
        value = quantity;
    }

    public String getValue() {
        return value;
    }

    public void increase(int amount) {
        this.value = Integer.toString(Integer.parseInt(value) + amount);
    }

    /**
     * Ensures quantity remains above 0 and stores the remaining quantity of books
     *
     * @param selling quantity of books sold
     */
    public void decrease(int selling) throws CommandException {
        Integer after = Integer.parseInt(value) - selling;
        if (after >= 0) {
            this.value = Integer.toString(after);
        } else {
            throw new CommandException(MESSAGE_INVALID_QUANTITY);
        }
    }

    public int toInteger() {
        return Integer.parseInt(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid quantity.
     */
    public static boolean isValidQuantity(String test) {
        return test.matches(QUANTITY_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Quantity // instanceof handles nulls
                && value.equals(((Quantity) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
