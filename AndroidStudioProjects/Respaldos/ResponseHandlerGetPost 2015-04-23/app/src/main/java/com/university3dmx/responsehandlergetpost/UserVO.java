package com.university3dmx.responsehandlergetpost;

/**
 * Created by Admin on 23/04/2015.
 */
public class UserVO {

    private String idUser;
    private String user;
    private String password;
    private String email;
    private String idPerson;

    public UserVO() {
    }

    public UserVO(String idUser, String user, String password, String email, String idPerson) {
        this.idUser = idUser;
        this.user = user;
        this.password = password;
        this.email = email;
        this.idPerson = idPerson;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }
}
