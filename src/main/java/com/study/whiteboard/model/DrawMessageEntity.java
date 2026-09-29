package com.study.whiteboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "draw_messages")
public class DrawMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String strokeId;
    private double prevX;
    private double prevY;
    private double currX;
    private double currY;
    private String color;
    private double lineWidth;
    private String senderId;
    private String type;
    private String roomId;

    public DrawMessageEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrokeId() {
        return strokeId;
    }

    public void setStrokeId(String strokeId) {
        this.strokeId = strokeId;
    }

    public double getPrevX() {
        return prevX;
    }

    public void setPrevX(double prevX) {
        this.prevX = prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPrevY(double prevY) {
        this.prevY = prevY;
    }

    public double getCurrX() {
        return currX;
    }

    public void setCurrX(double currX) {
        this.currX = currX;
    }

    public double getCurrY() {
        return currY;
    }

    public void setCurrY(double currY) {
        this.currY = currY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
