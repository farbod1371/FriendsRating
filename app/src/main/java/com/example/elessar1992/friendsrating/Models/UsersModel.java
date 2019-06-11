package com.example.elessar1992.friendsrating.Models;

/**
 * Created by elessar1992 on 6/6/19.
 */

public class UsersModel
{
   String name;
   String email;
   String search;
   String phone;
   String image;
   String cover;
   String uid;

   public UsersModel(){}

   public UsersModel(String name, String email, String search, String phone, String image, String cover) {
        this.name = name;
        this.email = email;
        this.search = search;
        this.phone = phone;
        this.image = image;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSearch() {
        return search;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public String getCover() {
        return cover;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
