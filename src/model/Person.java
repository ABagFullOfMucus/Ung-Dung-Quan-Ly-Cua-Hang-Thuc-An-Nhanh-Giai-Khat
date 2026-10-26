package model;

import interfaces.IStatusable;

public abstract class Person implements IStatusable {
	protected String id;
	protected String name;
	protected String phoneNumber;
	protected boolean status;

	// Constructor
	public Person() {
		this.status = true;
	}

	public Person(String id, String name, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.status = true;
	}

	public abstract void displayInfo();

	public abstract void updateStatus();

	// setter

	public void setID(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public void setStatus(boolean status) {
		this.status = status;
	}

	// getter

	public String getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@Override
	public boolean isStatus() {
		return this.status;
	}
}
