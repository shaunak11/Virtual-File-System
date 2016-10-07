package system;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.nio.file.DirectoryNotEmptyException;
import java.util.ArrayList;
import java.util.Arrays;


public class FileSystem {	
	private Tree fileSystemTree;	
	public static Space fileStorage;
	public FileSystem(int m) {
		fileSystemTree = new Tree("root");
		fileStorage = new Space(m);
	}
	public boolean Createdir(String[] name) throws BadFileNameException {
		Tree workingTree = fileSystemTree;
		if (name[0].equals("\"root\"") || (FileExists(name) != null)) {
			throw new BadFileNameException();
		}
		if (DirExists(name) != null) {
			return false;
		}
		for (int i = 0; i < name.length; i++) {
			workingTree = workingTree.GetChildByName(name[i]);
		}
		return true;
	}
	public String[][] disk() {
		Leaf[] alloc = FileSystem.fileStorage.getAlloc();
		String[][] disk = new String[alloc.length][];
		int i = 0;
		for (Leaf elem : alloc) {
			if (elem == null) {
				i++;
				continue;
			} else {
				disk[i++] = elem.getPath();
			}
		}
		return disk;
	}
	public boolean Createfile(String[] name, int k,int filetype,int rating,String comments) throws BadFileNameException, OutOfSpaceException {
		Tree workingTree = fileSystemTree;
		String fileName = name[name.length - 1];
		if (name[0].equals("\"root\"")) {
			throw new BadFileNameException();
		}
		if (k > FileSystem.fileStorage.countFreeSpace()) { //not enough space free
			Leaf file = FileExists(name);
			if (file == null) {
				throw new OutOfSpaceException();
			} else if (k <= (FileSystem.fileStorage.countFreeSpace() - file.allocations.length)) { //if there will be enough space free after deleting the old file, do it
				rmfile(name);
			}
		}
		for (int i = 0; i < name.length - 1; i++) {
			workingTree = workingTree.GetChildByName(name[i]);
		}
		if (workingTree.children.containsKey(fileName)) { //file exists, remove (reached this point, so file can fit)
			if (workingTree.children.get(fileName).getClass().getName() == "Tree") { //name of existing directory 
				throw new BadFileNameException();
			}
			rmfile(name);
		}
		Leaf newLeaf = new Leaf(fileName, k,filetype,rating,comments);
		newLeaf.parent = workingTree;
		newLeaf.depth = newLeaf.parent.depth + 1;
		if(workingTree.children.put(fileName, newLeaf)!=newLeaf)
			return true;
		else
			return false;
	}
	public String[] lsdir(String[] name) {
		Tree file = DirExists(name);
		String[] fileList;
		if (file == null) {
			return null;
		}
		fileList = new String[file.children.size()];
		fileList = file.children.keySet().toArray(fileList);
		Arrays.sort(fileList);
		return fileList;
		
	}
	public boolean rmfile(String[] name) {
		Leaf file = FileExists(name);
		if (file == null) { //file doesn't exist
			return false;
		}
		if(FileSystem.fileStorage.Dealloc(file))
			return true;
		else
			return false;
			
	}
	public boolean rmdir(String[] name) throws DirectoryNotEmptyException {
		Tree dir = DirExists(name);
		if (dir == null) {
			return false;
		}
		if (dir.children.size() > 0) {
			throw new DirectoryNotEmptyException(null);
		}
		if(dir.parent.children.remove(dir.name)!=null)
			return true;
		else
			return false;
	}
	private Node PathExists(String[] name) {
		Tree workingTree = fileSystemTree;
		for (int i = 0; i < name.length - 1; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				return null;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
		return workingTree.children.get(name[name.length - 1]);
	}
	public Leaf FileExists(String[] name) {
		Node found = PathExists(name);
		if (found == null || found.getClass().getName() == "Node") {
			return null;
		}
		return (Leaf) found;
	}
	public Tree DirExists(String[] name) {
		Node found = PathExists(name);
		if (found == null || found.getClass().getName() == "Leaf") {
				return null;
			}
			return (Tree) found;
		}
	public ArrayList<String> searchbyType(String[] name,ArrayList<String> results,int type)
	{
		Tree workingTree = fileSystemTree;
		for (int i = 0; i < name.length; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				return null;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
		Node[] n=workingTree.getChildren();
		for(int j=0;j<n.length;j++)
		{
			int length=name.length;
			String[] temp=Arrays.copyOf(name,name.length+1);
			temp[length]=n[j].name;
			if(isFile(workingTree, temp))
			{
				Leaf l=(Leaf)n[j];
				if(l.filetype==type)
				{
					results.add(l.name);
				}
			}
			else
			{
				searchbyType(temp, results,type);
			}
		}	
		return results;
	}
	
	public ArrayList<String> writeText(String[] name, ArrayList<String> w)
	{
		Tree workingTree = fileSystemTree;
		for (int i = 0; i < name.length-1; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				return null;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
		Leaf l = (Leaf)workingTree.children.get(name[name.length-1]);
		if(l.filetype==2)
				l.setText(w);
		else
			System.out.println("not a text file");	
	return null;
	}
	
	public ArrayList<String> readText(String[] name)
	{
		Tree workingTree = fileSystemTree;
		for (int i = 0; i < name.length-1; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				return null;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
	Leaf l = (Leaf)workingTree.children.get(name[name.length-1]);
	if(l.filetype==2){
		return l.getText();
	}
	else
		System.out.println("not a text file");		
	return null;
	}
	
	public ArrayList<String> searchbyRating(String[] name,ArrayList<String> results,int rating)
	{
		Tree workingTree = fileSystemTree;
		for (int i = 0; i < name.length; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				System.out.println("Rating:"+rating);
				return null;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
		
		Node[] n=workingTree.getChildren();
		for(int j=0;j<n.length;j++)
		{
			int length=name.length;
			String[] temp=Arrays.copyOf(name,name.length+1);
			temp[length]=n[j].name;
			if(isFile(workingTree, temp))
			{
				Leaf l=(Leaf)n[j];
				if(l.rating==rating)
				{
					results.add(l.name);
				}
			}
			else
			{
				searchbyRating(temp, results,rating);
			}
		}	
		return results;
	}
	public void listFiles(String[] name)
	{
		Tree workingTree = fileSystemTree;
		for (int i = 0; i < name.length; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				System.out.println("No such path\n");;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
		Node[] n=workingTree.getChildren();
		for(int j=0;j<n.length;j++)
		{
			int length=name.length;
			String[] temp=Arrays.copyOf(name,name.length+1);
			temp[length]=n[j].name;
			if(isFile(workingTree, temp))
			{
				Leaf l=(Leaf)n[j];
				l.printLeaf();
			}
			else
			{
				listFiles(temp);
			}
		}		
	}
	public boolean isFile(Tree workingTree,String[] name)
	{
		if(workingTree==null)
		{
			workingTree=fileSystemTree;
		}
		int i;
		for (i = workingTree.depth; i < name.length-1; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				return false;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
		Node n=workingTree.children.get(name[i]);
		if(n.tf==0)
			return true;
		return false;
	}
	public boolean isDir(Tree workingTree,String[] name)
	{
		if(workingTree==null)
		{
			workingTree=fileSystemTree;
		}
		int i;
		for (i = workingTree.depth; i < name.length-1; i++) {
			if (!workingTree.children.containsKey(name[i])) {
				return false;
			}
			workingTree = (Tree) workingTree.children.get(name[i]);
		}
		Node n=workingTree.children.get(name[i]);
		if(n.tf==1)
			return true;
		return false;
	}
	
}

