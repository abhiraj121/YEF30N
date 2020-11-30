package com.YEF.yefApp;

public class Member {

    String title;
    String image;
    String city;
    String story;

    public Member(){

    }

    public Member(String title, String image, String city, String story){
        if (title.trim().equals("")) {
            title = "No Name";
        }
        if (city.trim().equals("")) {
            city = "Not available";
        }
        if (story.trim().equals("")) {
            story = "Not available";
        }
        this.title=title;
        this.image=image;
        this.city=city;
        this.story=story;

    }
    public Member(String image){
        this.image=image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}