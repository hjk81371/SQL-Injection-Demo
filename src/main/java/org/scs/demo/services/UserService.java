package org.scs.demo.services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.scs.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;


@Service
@SessionScope
public class UserService {

    private final DataSource dataSource;

    private User loggedInUser = null;

    @Autowired
    public UserService(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public boolean authenticate(String username, String password) throws SQLException {
        final String sql = "SELECT * FROM user WHERE username = '" + username + "'";
        System.out.println("USERNAME: " + username);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String storedPassword = rs.getString("password");
                    boolean isPassMatch = password.equals(storedPassword);
                    if (isPassMatch) {
                        String userId = rs.getString("userId");
                        String uname = rs.getString("username");
                        String pw = rs.getString("password");
    
                        loggedInUser = new User(userId, uname, pw);
                    }
                    return isPassMatch;
                }
            }
        }
        return false;
    }


    public void unAuthenticate() {
        loggedInUser = null;
    }

    public boolean isAuthenticated() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

}
