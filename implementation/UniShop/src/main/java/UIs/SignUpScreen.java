package UIs;

/**
 * Interface to create sign up screens for a buyer and a seller. Classes implementing this interface must define
 * getCredentialsAndSignUp().
 */
public interface SignUpScreen {
    /**
     * Asks the user's necessary information in order to sign them up on UniShop.
     */
    void getCredentialsAndSignUp();
}
