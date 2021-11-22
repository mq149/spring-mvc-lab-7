package springmvclab7.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class Employee {
	
	@Id
	private String id;
	private String name;
	private int year_of_experience;
	private boolean probationary;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getYOE() {
		return year_of_experience;
	}
	
	public void setYOE(int year_of_experience) {
		this.year_of_experience = year_of_experience;
	}
	
	public boolean isProbationary() {
		return probationary;
	}
	
	public void setProbationary(boolean probationary) {
		this.probationary = probationary;
	}
	
	public Employee(String id, String name, int year_of_experience, boolean probationary) {
		super();
		this.id = id;
		this.name = name; 
		this.year_of_experience = year_of_experience;
		this.probationary = probationary;
	}
	
	public Employee() {
		super();
	}
}
