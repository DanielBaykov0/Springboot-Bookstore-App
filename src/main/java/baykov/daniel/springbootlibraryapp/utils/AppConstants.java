package baykov.daniel.springbootlibraryapp.utils;

public class AppConstants {

    public static final long ISBN_LOWER_BOUND = 1000_0000_0000_0000L;
    public static final long ISBN_UPPER_BOUND = 9999_9999_9999_9999L;
    public static final int DEFAULT_DAYS_TO_RETURN_A_BOOK = 7;
    public static final int DEFAULT_MAX_POSTPONE_DAYS = 14;
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIR = "asc";
    public static final String AUTHOR_DELETE_MESSAGE = "Author was deleted successfully!";
    public static final String EBOOK_DELETE_MESSAGE = "EBook was deleted successfully!";
    public static final String PAPER_BOOK_DELETE_MESSAGE = "Book was deleted successfully!";
    public static final String PENDING_RETURN_MESSAGE = "You have at least one book with a pending return!";
    public static final String NO_BOOKS_AVAILABLE_MESSAGE = "There are no books available!";
    public static final String NO_VALID_BOOK_USER_MESSAGE = "The borrow history is not from the current user!";
    public static final String BOOK_ALREADY_RETURNED_MESSAGE = "The book is already returned!";
    public static final String INVALID_POSTPONE_DAYS_MESSAGE = "The postpone days must be greater than 0!";
    public static final String LIMIT_POSTPONE_DAYS_MESSAGE = "The postpone date must be up to 14 days from the borrow date!";
    public static final String INVALID_USER_AND_EBOOK_MESSAGE = "User and EBook don't match!";
}
