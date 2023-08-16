package com.example.placetest;

public class Place {
    private String name;
    private Long votes;

    public Place(String name, Long votes) {
        this.name = name;
        this.votes = votes;
    }

    public String getName() {
        return name;
    }

    public Long getVotes() {
        return votes;
    }
}
