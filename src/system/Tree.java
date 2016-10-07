package system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

class Tree extends Node {
	public HashMap<String, Node> children = new HashMap<String, Node>();
	public Tree(String name) {
		this.name = name;
	}
	public Tree GetChildByName(String name) {
		if (this.children.containsKey(name)) {
			//System.out.println(this.children.get(name));
			return (Tree) this.children.get(name);
		}
		Tree newTree = new Tree(name);
		newTree.parent = this;
		newTree.tf=1;
		newTree.depth = newTree.parent.depth + 1;
		if(this.children.put(name, newTree)!=null)
		{
		  System.out.println("Tree insertion Successfull");
		}
		return newTree;
	}
	public int getchildCount()
	{
		return children.size();
	}
	public Node[] getChildren()
	{
		Node[] n=new Node[getchildCount()];
		Iterator<Node> iterator= children.values().iterator();
		for(int i=0;i<n.length;i++)
		{
			n[i]=iterator.next();
		}
		return n;
	}
}

