package com.example.SpringBatchTutorial.job.FileDataReadWrite.dto;

import java.time.Year;

import lombok.Data;

@Data
public class PlayerYears {
	private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
    private int yearsExperience;
    
    //Plyaer객체 -> PlayerYears객체로
    public PlayerYears(Player player) {
        this.ID = player.getID();
        this.lastName = player.getLastName();
        this.firstName = player.getFirstName();
        this.position = player.getPosition();
        this.birthYear = player.getBirthYear();
        this.debutYear = player.getDebutYear();
        //경력 : 현재년도 - 데뷔날짜
        this.yearsExperience = Year.now().getValue() - player.getDebutYear();
    }
}
