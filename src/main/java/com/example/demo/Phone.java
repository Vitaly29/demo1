package com.example.demo;

public class Phone {
	private long id;
	private String fio;
	private String num;

	public Phone(long id, String fio, String num) {
		super();
		this.id = id;
		this.fio = fio;
		this.num = num;
	}

	@Override
	public String toString() {
		return "Phone(id=" + id + ") [ФИО=" + fio + ", Номер телефона=" + num + "]</br>";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

}
