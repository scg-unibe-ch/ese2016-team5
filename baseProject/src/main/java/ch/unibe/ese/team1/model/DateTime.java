package ch.unibe.ese.team1.model;

import java.util.Date;

public class DateTime implements IDateTime{
	
	@Override
	public Date getDate(){
		return new Date(); 
	}

}
