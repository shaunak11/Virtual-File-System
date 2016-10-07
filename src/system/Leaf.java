package system;

import java.util.ArrayList;

class Leaf extends Node {
	public int size;
	public int filetype;
	public int rating;
	public String commnets;
	public int[] allocations;
	public ArrayList<String> text = new ArrayList<String>();
	public Leaf(String name, int size, int filetype,int rating,String comments) throws OutOfSpaceException {
		this.name = name;
		this.size = size;
		this.filetype=filetype;
		this.tf=0;
		this.rating=rating;
		this.commnets=comments;
		allocateSpace(size);
	}
	private void allocateSpace(int size) throws OutOfSpaceException {
		FileSystem.fileStorage.Alloc(size, this);
	}
	public String getLeafName()
	{
		return this.name;
	}
	public int getchildCount()
	{
		return -1;
	}
	public void setText(ArrayList<String> text){
		this.text.addAll(text);
	}
	public ArrayList<String> getText(){
		return this.text;
	}
	public void printLeaf()
	{
		System.out.println(this.name+" "+this.size+" "+this.filetype+" "+this.rating+" "+this.commnets);
	}
}

