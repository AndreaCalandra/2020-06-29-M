package it.polito.tdp.imdb.model;

public class Vicino implements Comparable{

	Director d;
	int p;
	
	public Vicino(Director d, int p) {
		super();
		this.d = d;
		this.p = p;
	}

	public Director getD() {
		return d;
	}

	public void setD(Director d) {
		this.d = d;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Vicino d = (Vicino) o;
		return -(this.p - d.p);
	}

	@Override
	public String toString() {
		return d.firstName + " " + d.lastName + " = " + p + "\n";
	}
	
	
	
	
}
