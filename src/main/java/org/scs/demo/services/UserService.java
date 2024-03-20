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
        final String sql = "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'";
        System.out.println("SQL QUERY: " + sql);
        // System.out.println("USERNAME: " + username);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // System.out.println("HERE");
                    // String puid = rs.getString("userId");
                    // String puname = rs.getString("username");
                    // String ppw = rs.getString("password");
                    
                    // Print out the retrieved data
                    // System.out.println("User ID: " + puid + ", Username: " + puname + ", Password: " + ppw);

                    // String storedPassword = rs.getString("password");
                    // boolean isPassMatch = password.equals(storedPassword);
                    boolean isPassMatch = true;
                    if (isPassMatch) {
                        String userId = rs.getString("userId");
                        String uname = rs.getString("username");
                        String pw = rs.getString("password");
    
                        loggedInUser = new User(userId, uname, pw);
                        return true;
                    }
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
