package edu.school21.sockets.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(Long id);
    List<T> findAll();

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
    void save(T entity) throws UsernameExistsException;
    void update(T entity);
    void delete(Long id);
}
