package bd.edu.seu.chat.seuquest.user;


import bd.edu.seu.chat.seuquest.HelloApplication;
import bd.edu.seu.chat.seuquest.modules.DeviceInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDetails{
    private boolean isAuthenticated = false;
    private Integer id = null;
    private String username;
    private String password;
    private String fullName;
    private boolean hasRole;
    private Role role;
    private String secretKey;
    private String profilePath;
    private boolean userForgotPass = false;

    private DatabaseManager db = HelloApplication.dbManager;
    private static String deviceMac = DeviceInfo.getMacAddress();
    private static String deviceIp = DeviceInfo.getLocalIPAddress();

    public UserDetails() {}

    public UserDetails(boolean isAuthenticated, Integer id, String username, String fullName, boolean hasRole, Role role) {
        this.isAuthenticated = isAuthenticated;
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.hasRole = hasRole;
        this.role = role;

    }

    public UserDetails(String username, String password) throws SQLException {
        this.username = username;
        this.password = encyptPassword(password);

    }

    public UserDetails(String username, String password, String secretKey, String fullName,String profile) throws SQLException {
        this.username = username;
        this.password = encyptPassword(password);
        this.secretKey = encyptPassword(secretKey);
        this.fullName = fullName;
        this.profilePath = profile;

    }

    public UserDetails(String username, String password, Role role, String fullName,String profile) throws SQLException {
        this.username = username;
        this.password = encyptPassword(password);
        this.role = role;
        this.fullName = fullName;
        this.profilePath = profile;

    }


    private boolean setDetails(boolean isAuthenticated, String username,Integer id, String fullName,boolean hasRole, Role role, String profilePath){
        this.isAuthenticated = isAuthenticated;
        this.id = id;
        this.username = username;
        this.role = role;
        this.hasRole = hasRole;
        this.fullName = fullName;
        this.profilePath = profilePath;

        return true;
    }

    public String encyptPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Unable to apply SHA-256 hashing algorithm.", e);
        }
    }

    public boolean login() throws SQLException {
        StringBuilder condition = new StringBuilder();
        StringBuilder command = new StringBuilder(); // command to update database user mac and loggedin status

        condition.append("username='"+username+"' && password='"+password+"'");

        ResultSet result = db.getByCustomCondition("users",condition.toString());
//        System.out.println(result.next());

        if(result.next()){
//            result.next();
            hasRole = result.getBoolean("has_role");
            String getRole = result.getString("role");
            Role UserRole = getRoleFromString(getRole);

            setDetails(true, username,result.getInt("id"),
                    result.getString("full_name"), true,
                    UserRole,result.getString("profile"));

            command.append(
                    "UPDATE users set loogedin=true, mac="
                    + deviceMac
                    +" where id="+id
            );
            db.customCommand(command.toString());
            return true;
        }

        return false;
    }

    public boolean loginStudent(String email, String name, String profile) throws SQLException {
        StringBuilder query = new StringBuilder();
        StringBuilder createUserCommand = new StringBuilder();
        StringBuilder loggedInCommand = new StringBuilder(); // command to update database user mac and loggedin status

        query.append(
                "SELECT * FROM users WHERE username = '"
                        + email + "'"
        );
        createUserCommand.append(
                "INSERT INTO users (username, full_name, role, has_role, profile) "
                + "VALUES('"
                + email +"','"
                + name +"','"
                + Role.STUDENT.name() +"',"
                + true +",'"
                + profile +"')"
        );
        ResultSet result = db.customQuery(query.toString());

        int id = 765;
        if (result.next()) {
            id = result.getInt("id");
        }else {
            db.customCommand(createUserCommand.toString());
        }
        
        setDetails(true,email,id,name,true,Role.STUDENT,profile);
        loggedInCommand.append(
                "UPDATE users set loogedin=true, mac="
                        + deviceMac
                        +" where id="+id
        );
        db.customCommand(loggedInCommand.toString());
        return true;
    }

    public boolean register() throws SQLException {
        StringBuilder addUserCommand = new StringBuilder();
        StringBuilder secretKeyCommand = new StringBuilder();

        if(role != null){
            addUserCommand.append("insert into users(username,password,full_name,has_role,role,profile,is_guest) " +
                    "values('" + username +"','"+password+"','"+
                    fullName +"',true,'"+role.name()+"','"+profilePath+"',false)"
            );

            db.customCommand(addUserCommand.toString());

            return true;
        }
        // else
        secretKeyCommand.append("select * from secret_key where skey = '"+secretKey+"'");
        ResultSet secretKey = db.customQuery(secretKeyCommand.toString());
        if(secretKey.next()){
            Role userRole = getRoleFromString(secretKey.getString("role"));
            boolean is_guest = (userRole == Role.GUEST || userRole == Role.NONE)? true:false;

            addUserCommand.append("insert into users(username,password,full_name,has_role,role,profile,is_guest) " +
                    "values('" + username +"','"+password+"','"+
                    fullName +"',true,'"+userRole+"','"+profilePath+"',"+is_guest+")"
            );

            db.customCommand(addUserCommand.toString());
            return true;
        }

        return false;

    }

    public boolean logout() throws SQLException {
        StringBuilder command = new StringBuilder();
        command.append(
                "UPDATE users set loogedin=false where id= "
                +getId()

        );
        if(deviceMac != null){
            command.append(
                    " && mac="
                    +deviceMac
            );
        }

        db.customCommand(command.toString());
        setDetails(false, null,null,null, false, Role.NONE, null);
        return true;
    }

    public void forgotPassword(String username,int id) throws SQLException {
        this.username = username;
        this.id = id;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean hasRole() {
        return hasRole;
    }

    public Role getRole() {
        return role;
    }

    public String getFullName(){
        return fullName;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public Role setRole(Role role) {
        this.role = role;
        return role;
    }

    public boolean isUserForgotPass() {
        return userForgotPass;
    }

    public void setUserForgotPass(boolean userForgotPass) {
        this.userForgotPass = userForgotPass;
    }


    public Map<String, Object> getDetails(){
        Map<String, Object> details = new HashMap<>();
        details.put("isAuthenticated", isAuthenticated);
        details.put("id", id);
        details.put("username", username);
        details.put("hasRole", hasRole);
        details.put("role", role);

        return details;
    }

    public void checkPreviousLogin() throws SQLException, IOException {

        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT * FROM users WHERE loogedin=true && mac="
                + deviceMac
        );

        if(deviceMac == null){
            HelloApplication.changeScene("hello-view","SeuQuest- Welcome Dashboard", 640, 744);
            return;
        }
        db = HelloApplication.dbManager;
        ResultSet result = db.customQuery(query.toString());
        if(result.next()){
            setDetails(
                    true, result.getString("username"),
                    result.getInt("id"),result.getString("full_name"),
                    result.getBoolean("has_role"),getRoleFromString(result.getString("role")),
                    result.getString("profile")
            );

            if(getRole() == Role.ADMIN || getRole() == Role.SUPERUSER){
                HelloApplication.changeScene("admin-view","SeuQuest- Admin Dashboard", 1300, 744);
            }else if(getRole() == Role.TRAINER){
                HelloApplication.changeScene("trainer-view","SeuQuest- Training Dashboard", 1300, 744);
            } else if (getRole() == Role.STUDENT) {
                HelloApplication.changeScene("student-view","SeuQuest- Student Dashboard",1300,744);
            } else{
                HelloApplication.changeScene("chat-view","SeuQuest- Chat Dashboard", 1300, 744);
            }
            return;
        }else {
            HelloApplication.changeScene("hello-view","SeuQuest- Welcome Dashboard", 640, 744);
        }
    }
    public Role getRoleFromString(String StrRole){
        Role userRole;
        if(StrRole.equals("SUPERUSER")){
            userRole = Role.SUPERUSER;
        }else if(StrRole.equals("ADMIN")){
            userRole = Role.ADMIN;
        }else if(StrRole.equals("TRAINER")){
            userRole = Role.TRAINER;
        }else if(StrRole.equals("GUEST")){
            userRole = Role.GUEST;
        }else if(StrRole.equals("GENERAL")){
            userRole = Role.GENERAL;
        } else if (StrRole.equals("STUDENT")) {
            userRole = Role.STUDENT;
        } else {
            userRole = Role.NONE;
        }
        return userRole;
    }

    public Image getProfileImage() {
        Image profileImage;

        if(getRole() == Role.STUDENT){
            return new Image(profilePath);
        }

        String path = "values/images/profiles/";
        if(profilePath != null){
            path = path.concat(profilePath);
        }else {
            path = path.concat("guest-user.png");
        }

        profileImage = new Image(String.valueOf(HelloApplication.class.getResource(path)));
        return profileImage;
    }


}
