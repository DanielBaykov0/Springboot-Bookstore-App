package baykov.daniel.springbootlibraryapp.utils;

public class Messages {
    public static final String AUTHOR_DELETE_MESSAGE = "Author was deleted successfully!";
    public static final String BOOK_DELETE_MESSAGE = "Book was deleted successfully!";
    public static final String PENDING_RETURN_MESSAGE = "You have at least one book with a pending return!";
    public static final String NO_BOOKS_AVAILABLE_MESSAGE = "There are no books available!";
    public static final String NO_VALID_BOOK_USER_MESSAGE = "The borrow history is not from the current user!";
    public static final String BOOK_ALREADY_RETURNED_MESSAGE = "The book is already returned!";
    public static final String INVALID_POSTPONE_DAYS_MESSAGE = "The postpone days must be greater than 0!";
    public static final String LIMIT_POSTPONE_DAYS_MESSAGE = "The postpone date must be up to 14 days from the borrow date!";
    public static final String INVALID_USER_AND_BOOK_MESSAGE = "User and Book don't match!";
    public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists!";
    public static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email already exists!";
    public static final String USER_REGISTERED_SUCCESSFULLY_MESSAGE = "User registered successfully!";
    public static final String AVAILABLE_BOOKS_BIGGER_THAN_TOTAL_MESSAGE = "Total Number of Books should be equal or greater than number of Available Books!";
}
