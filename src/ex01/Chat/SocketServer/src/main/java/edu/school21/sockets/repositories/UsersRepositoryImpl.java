package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component("usersRepository")
public class UsersRepositoryImpl implements UsersRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(Long id) {
        String sqlFindByIdQuery = "SELECT * FROM \"user\" WHERE id = :id";
        return jdbcTemplate.query(sqlFindByIdQuery,
                new MapSqlParameterSource().addValue("id", id),
                new BeanPropertyRowMapper<>(User.class)).stream().findAny();
    }

    @Override
    public List<User> findAll() {
        String sqlFindAllQuery = "SELECT * FROM \"user\"";
        return jdbcTemplate.query(sqlFindAllQuery, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void save(User entity) throws UsernameExistsException {
        if (findByUsername(entity.getUsername()).isPresent()) {
            throw new UsernameExistsException(1);
        }
        String sqlSaveQuery = "INSERT INTO \"user\" (username, password) VALUES (:username, :password)";
        try {
            jdbcTemplate.update(sqlSaveQuery,
                    new MapSqlParameterSource()
                            .addValue("username", entity.getUsername())
                            .addValue("password", entity.getPassword()));
        } catch (DataAccessException e) {
            throw new UsernameExistsException(2);
        }
    }

    @Override
    public void update(User entity) {
        String sqlSaveQuery = "UPDATE \"user\" SET username = :username, password = :password WHERE id = :id";
        jdbcTemplate.update(sqlSaveQuery,
                new MapSqlParameterSource().addValue("username", entity.getUsername())
                        .addValue("id", entity.getId())
                        .addValue("password", entity.getPassword()));
    }

    @Override
    public void delete(Long id) {
        String sqlDeleteMessagesUser = "DELETE FROM message WHERE user_id = :id";
        jdbcTemplate.update(sqlDeleteMessagesUser, new MapSqlParameterSource().addValue("user_id", id));
        String sqlDeleteQuery = "DELETE FROM \"user\" WHERE id = :id";
        jdbcTemplate.update(sqlDeleteQuery,
                new MapSqlParameterSource().addValue("id", id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sqlFindByEmail = "SELECT * FROM \"user\" WHERE username = :username";
        return jdbcTemplate.query(sqlFindByEmail,
                new MapSqlParameterSource().addValue("username", username),
                new BeanPropertyRowMapper<>(User.class)).stream().findAny();
    }
}
