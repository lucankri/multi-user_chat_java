package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Component("messagesRepository")
public class MessagesRepositoryImpl implements CrudRepository<Message> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MessagesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    private static RowMapper<Message> getMessageRowMapper() {
        return (rs, rowNum) -> {
            try {
                Message message = new Message();
                message.setId(rs.getLong("message_id"));
                message.setText(rs.getString("text"));
                message.setDate(rs.getString("date"));
                User user = new User(rs.getLong("sender_id"), rs.getString("username"), rs.getString("password"));
                message.setSender(user);
                return message;
            } catch (SQLException e) {
                throw new RuntimeException("Error mapping row to Message", e);
            }
        };
    }

    @Override
    public Optional<Message> findById(Long id) {
        String sqlFindByIdQuery = "SELECT m.id as message_id, text, date, s.id as sender_id, s.username, s.password\n" +
                "    FROM message m\n" +
                "    JOIN \"user\" s ON m.user_id = s.id\n" +
                "    WHERE m.id = :id";

        return jdbcTemplate.query(sqlFindByIdQuery,
                new MapSqlParameterSource().addValue("id", id),
                getMessageRowMapper()).stream().findAny();
    }

    @Override
    public List<Message> findAll() {
        String sqlFindAllQuery = "SELECT m.id as message_id, text, date, s.id as sender_id, s.username, s.password\n" +
                "    FROM message m\n" +
                "    JOIN \"user\" s ON m.user_id = s.id";
        return jdbcTemplate.query(sqlFindAllQuery, getMessageRowMapper());
    }

    @Override
    public void save(Message entity) {
        String sqlSaveQuery = "INSERT INTO message (user_id, text, date) VALUES (:sender_id, :text, :date)";
        try {
            jdbcTemplate.update(sqlSaveQuery,
                    new MapSqlParameterSource()
                            .addValue("sender_id", entity.getSender().getId())
                            .addValue("text", entity.getText())
                            .addValue("date", new Timestamp(dateFormat.parse(entity.getDate()).getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Message entity) {
        String sqlSaveQuery = "UPDATE message SET user_id = :sender_id, text = :text, date = :date WHERE id = :id";
        try {
            jdbcTemplate.update(sqlSaveQuery,
                    new MapSqlParameterSource().addValue("sender_id", entity.getSender().getId())
                            .addValue("text", entity.getText())
                            .addValue("id", entity.getId())
                            .addValue("date", new Timestamp(dateFormat.parse(entity.getDate()).getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sqlDeleteQuery = "DELETE FROM message WHERE id = :id";
        jdbcTemplate.update(sqlDeleteQuery,
                new MapSqlParameterSource().addValue("id", id));
    }
}
