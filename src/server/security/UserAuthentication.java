package server.security;

public class UserAuthentication {

    /*
    static public UserDTO login(String username, String plaintextPassword) throws CredentialException, NotFoundException {
        UserDTO ret = null;
        // First, go to database to get stored hash via query executor
        ret = QueryExecutor.getUserGivenUsername(username);
        if (ret == null) {
            throw new NotFoundException(username + " was not found");
        }
        // Then, use Password.correctPassword(....) to verify that it's the correct one
        if (Password.isCorrectPassword(plaintextPassword, ret.passwordHash) && !SharedObjects.userIsLoggedIn(username)) {
            SharedObjects.loginUser(username);
            return ret;
        }
        throw new CredentialException();
    }

    static public UserDTO logout(String username) throws NotFoundException {
        UserDTO ret = null;

        ret = QueryExecutor.getUserGivenUsername(username);
        if (ret == null) {
            throw new NotFoundException(username + " was not found");
        }
        if (SharedObjects.logoutUser(username)) {
            System.out.println("Successfully logged out user: " + username);
        } else {
            System.out.println("Could not log out user: " + username + ". Perhaps the user is not logged in?");
        }
        return ret;
    }
    */
}
