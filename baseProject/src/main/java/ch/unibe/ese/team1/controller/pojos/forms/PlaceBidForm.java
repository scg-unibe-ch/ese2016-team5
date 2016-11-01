/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unibe.ese.team1.controller.pojos.forms;

import javax.validation.constraints.NotNull;

/**
 *
 * @author stefa
 */
public class PlaceBidForm {
	
    @NotNull
    private int lastBid;
        
    public int getLastBid() {
        return this.lastBid;
    }    
    public void setLastBid(int lastBid) {
        this.lastBid = lastBid;
    }
}
