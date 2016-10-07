package system;

import java.util.LinkedList;

class Space {
	private Leaf[] blocks;
	private LinkedList<Integer> freeBlocks;
	public Space(int size) {
		blocks = new Leaf[size];
		freeBlocks = new LinkedList<Integer>();
		for (int i = 0; i < size; i++) {
			freeBlocks.add(i);
		}
	}
	public void Alloc(int size, Leaf file) throws OutOfSpaceException {
		file.allocations = new int[size];
		for (int i = 0; i < size; i++) {
			file.allocations[i] = freeBlocks.poll();
			blocks[file.allocations[i]] = file;
		}
	}
	public boolean Dealloc(Leaf file) {
		for (int i = 0; i < file.allocations.length; i++) {
			blocks[file.allocations[i]] = null;
			//record new free block
			freeBlocks.add(file.allocations[i]);
		}
		if(file.parent.children.remove(file.name)!=null)
			return true;
		else
			return false;
		
	}
	public int countFreeSpace() {
		return freeBlocks.size();
	}
	public Leaf[] getAlloc() {
		return blocks;
	}
}