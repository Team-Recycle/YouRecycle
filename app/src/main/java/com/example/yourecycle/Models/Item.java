package com.example.yourecycle.Models;

public class Item {
    String id, name, address, desc, imageUri, ownerId, ownerName, ownerPhoneNumber, timeStamp;

    public Item(String name, String address, String desc, String imageUri, String ownerId) {
        this.name = name;
        this.ownerId = ownerId;
        this.address = address;
        this.desc = desc;
        this.imageUri = imageUri;
    }

    public Item() { }

    public String getID() {
        return id;
    }

    public void setID(String itemID) {
        this.id = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
