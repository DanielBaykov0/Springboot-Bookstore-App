package baykov.daniel.springbootlibraryapp.constant;

public class ErrorMessages {

    public static final String INCORRECT_CREDENTIALS = "Incorrect email or password. Please try again.";

    public static final String REFRESH_TOKEN_EXPIRED = "Refresh Token has expired!";
    public static final String PREVIOUS_TOKEN_NOT_EXPIRED = "Previous Token has not expired yet!";
    public static final String PREVIOUS_TOKEN_EXPIRED = "Previous Token has expired!";

    public static final String NOT_AUTHENTICATED = "User not authenticated!";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with email: ";
    public static final String EMAIL_NOT_VERIFIED = "User email not verified!";

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists!";
    public static final String TOKEN_NOT_FOUND = "Token not found!";
    public static final String EMAIL_ALREADY_CONFIRMED = "Email already confirmed!";

    public static final String TOKEN_EXPIRED = "Token expired!";
    public static final String INVALID_TOKEN_TYPE = "Invalid Token Type!";

    public static final String EMAIL_SEND_FAILURE = "Failed to send email!";
    public static final String EMAIL_NOT_CONFIRMED = "Email has not been confirmed!";
    public static final String USER_NOT_VERIFIED_REVIEW = "Please verify your email before posting a review!";
    public static final String USER_NO_REVIEW = "User has not yet posted a review!";

    public static final String AVAILABLE_BOOKS_BIGGER_THAN_TOTAL = "Total Number of Books should be equal or greater than number of Available Books!";
    public static final String QUANTITY_EXCEEDS_PRODUCTS_AVAILABLE = "Requested quantity exceeds available copies!";
    public static final String NEGATIVE_QUANTITY = "Please enter a valid quantity!";
    public static final String QUANTITY_EXCEEDS_ADDED_PRODUCTS = "Requested quantity exceeds added copies!";
    public static final String CANT_BUY_MORE_THAN_ONE = "Item is already in your Shopping Cart. You can buy only one copy!";
    public static final String CAN_BUY_ONLY_ONE = "You can buy only one copy!";
    public static final String EBOOK_BOUGHT_ALREADY = "You have already purchased this ebook!";
    public static final String AUDIOBOOK_BOUGHT_ALREADY = "You have already purchased this audiobook!";
    public static final String UNKNOWN_PRODUCT_TYPE = "Unknown product type!";
}
