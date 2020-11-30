package com.YEF.yefApp;

public class signup {
    String email,number,name,password,confirmpasswrod;

    public signup()
    {}

    public signup(String email, String number, String name, String password, String confirmpasswrod) {
        this.email = email;
        this.number = number;
        this.name = name;
        this.password = password;
        this.confirmpasswrod = confirmpasswrod;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmpasswrod() {
        return confirmpasswrod;
    }
}
