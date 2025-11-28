package com.example.assignment1tripplanning;

public class Place {

    private String from;
    private String to;
    private String date;
    private String time;
    private String category;
    private boolean important;

    public Place(String from, String to, String date, String time, String category, boolean important) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.category = category;
        this.important = important;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getCategory() { return category; }
    public boolean isImportant() { return important; }
}
