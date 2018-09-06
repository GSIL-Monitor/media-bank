package com.syswin.temail.media.bank.bean;

public class Range {
	private Long pos = 0l;
	private Long end = 0l;
	private boolean isRange = true;
	public Range(String Range){
		 if(!Range.matches("^bytes=[0-9]{1,}-([0-9]+)?")){
			 isRange = false ;
		 }   
	            try {    
	            	String[] tmpStr  =  Range.replaceAll("bytes=", "").split("-");
	                pos = Long.parseLong(tmpStr[0]);
	                if(tmpStr.length>1)
	                end =  Long.parseLong(tmpStr[1]); 
	            } catch (NumberFormatException e) {  
	            	isRange = false;  
	            }
	}
	public Long getPos() {
		return pos;
	}
	public void setPos(Long pos) {
		this.pos = pos;
	}
	public Long getEnd() {
		return end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}
	public boolean isRange() {
		return isRange;
	}
	public void setRange(boolean isRange) {
		this.isRange = isRange;
	}
}
