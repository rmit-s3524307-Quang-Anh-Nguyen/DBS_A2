package Build;

public class Nodes {
	private static final int NOT_SET = -1;
	private int nodeNumber;
	private Node[] nodes = new Node[Btree.FAN_OUT];
	private Nodes[] parent = new Nodes[1];
	private Nodes[] childNode = new Nodes[Btree.FAN_OUT + 1];
	
	public Nodes(){
		nodeNumber = 0;
		nodes = Btree.resetNode(nodes);
	}
	
	public boolean add(Node node){
		boolean full = false;
		
		//check if all the 'node' are full
		if (nodes[nodes.length - 1].key>=0){
			full = true;
		}
		
		int count = 0;
		//loop to node to replace
		while (node.key > nodes[count].key){
			if (nodes[count].key == NOT_SET){
				nodes[count] = node;
				return full;
			}
			count++;
			if (count == nodes.length){
				break;
			}
		}
		
		//swap the 'node'
		while (count < nodes.length){
			if (nodes[count].key == NOT_SET)
				return full;
			
			Node temp = nodes[count];
			nodes[count] = node;
			node = temp;
			count++;
		}
		return full;
	}
	
	public int getNodeNumber(){
		return nodeNumber;
	}
	
	public Node[] getNodes(){
		return nodes;
	}
	
	public Nodes[] getChildNode(){
		return childNode;
	}
	
	public void addChildNode(Nodes childNode){
		for (int i=0; i< this.childNode.length; i++){
			if (this.childNode[i]!=null && childNode.getNodes()[0].key < this.childNode[i].getNodes()[0].key){
				Nodes temp = this.childNode[i];
				this.childNode[i] = childNode;
				childNode = temp;
			}
			if (this.childNode[i]==null){
				this.childNode[i] = childNode;
			}
		}
		
		childNode.setParentNode(this);
	}

	public void setParentNode(Nodes parent){
		this.parent[0] = parent;
	}
}
