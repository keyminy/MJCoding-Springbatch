package com.example.SpringBatchTutorial.job.FileDataReadWrite;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.example.SpringBatchTutorial.job.FileDataReadWrite.dto.Player;

public class PlayerFieldSetMapper implements FieldSetMapper<Player>{
    //FieldSetMapper : FieldSet -> Player객체로 변경
	@Override
	public Player mapFieldSet(FieldSet fieldSet) throws BindException {
		Player player = new Player();

        player.setID(fieldSet.readString(0));
        player.setLastName(fieldSet.readString(1));
        player.setFirstName(fieldSet.readString(2));
        player.setPosition(fieldSet.readString(3));
        player.setBirthYear(fieldSet.readInt(4));
        player.setDebutYear(fieldSet.readInt(5));

        return player;
	}
	

}
