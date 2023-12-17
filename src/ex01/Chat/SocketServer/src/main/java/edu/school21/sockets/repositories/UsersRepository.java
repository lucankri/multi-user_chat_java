package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User> {
    /**
     * EmailExistsException
     * error: 1 - such a user already exists
     * error: 2 - unknown error
     */
    class UsernameExistsException extends RuntimeException {
        private final int error;

        public UsernameExistsException(int error) {
            this.error = error;
        }

        public int getError() {
            return error;
        }
    }
    Optional<User> findByUsername(String username);
}
