package Build;

public class Nodes {
	private static final int NOT_SET = -1;
	private int nodeNumber;
	private int childNodeNumber;
	private Node[] nodes = new Node[Btree.FAN_OUT];
	private Nodes[] parent = new Nodes[1];
	private Nodes[] childNode = new Nodes[Btree.FAN_OUT + 2];
	
	public Nodes(){
		resetNodes();
	}
	
	public void add(Node node){
		int count = 0;
		//loop to node to replace
		while (node.key > nodes[count].key){
			if (nodes[count].key == NOT_SET){
				nodes[count] = node;
				nodeNumber++;
				return;
			}
			count++;
			if (count == nodes.length){
				break;
			}
		}
		
		//swap the node until unfilled node
		while (count < nodes.length){
			if (nodes[count].key == NOT_SET){
				nodes[count] = node;
				break;
			}
			
			Node temp = nodes[count];
			nodes[count] = node;
			node = temp;
			count++;
		}
		//check if all the 'node' are full
		if (nodeNumber>=Btree.FAN_OUT){
			PassByReference.pointer[0] = this;
			Btree.splitNode(PassByReference.pointer, node);
			return;
		}
		nodeNumber++;
	}
	
	public void addChildNode(Nodes childNode){
		if (childNode==null){
			return;
		}
		PassByReference.pointer[0] = this;
		childNode.setParentNode(PassByReference.pointer);
		for (int i=0; i < Btree.FAN_OUT + 2; i++){
			if (this.childNode[i]!=null && childNode.getNodes()[0].key <= this.childNode[i].getNodes()[0].key){
				Nodes temp = this.childNode[i];
				this.childNode[i] = childNode;
				childNode = temp;
			}
			if (this.childNode[i]==null){
				this.childNode[i] = childNode;
				break;
			}
		}
		
		childNodeNumber++;
	}
	
	//function used when splitting to reuse node
	public void resetNodes(){
		nodeNumber = 0;
		childNodeNumber = 0;
		for (int i=0; i<nodes.length; i++){
			int tempID[] = new int[2]; 
			nodes[i] = new Node(NOT_SET, tempID);
		}
		for (int i=0; i<childNode.length; i++){
			childNode[i] = null;
		}
	}
	
	public int getNodeNumber(){
		return nodeNumber;
	}
	
	public void setNodeNumber(int nodeNumber){
		this.nodeNumber = nodeNumber;
	}
	
	public int getChildNodeNumber(){
		return childNodeNumber;
	}
	
	public Node[] getNodes(){
		return nodes;
	}
	
	public Nodes[] getChildNode(){
		return childNode;
	}

	public void setParentNode(Nodes[] pointer){
		this.parent[0] = pointer[0];
	}

	public Nodes getParentNode(){
		return parent[0];
	}
}
