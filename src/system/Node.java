package system;

abstract class Node {
	String name;
	public Tree parent;
	int tf;
	int depth = 0;
	public String[] getPath() {
		String[] path = new String[this.depth];
		Node workingNode = this;
		for (int i = this.depth - 1; i >= 0; i--) {
			path[i] = workingNode.name;
			workingNode = workingNode.parent;
		}
		return path;
	}
}