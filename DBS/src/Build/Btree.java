package Build;

public final class Btree {
	public static final int FAN_OUT = 3;
	public static Nodes Root = new Nodes();
	public static int nodeNumber = 0;
	
	private Btree(){}
		
	public static void addNode(Node node){
		//check for child
		if (Root.getChildNode()[0] == null){
			//add node
			Root.add(node);
		} else {
			addNode(Root, compare(Root.getNodes(), Root.getChildNode(), node), node);
		}
		nodeNumber++;
	}
	
	public static void addNode(Nodes parent, Nodes current, Node node){
		if (current.getChildNode()[0] == null){
			current.add(node);
		} else {
			
			//recursively look for correct child
			//addNode(current, compare(current.getNodes(), current.getChildNode(), node), node);
			
		}
	}
	
	//compare to find the child
	public static Nodes compare(Node[] current, Nodes[] child, Node node){
		int result = 0;
		if (node.key >= current[result].key){
			for (int i=0; i<Btree.FAN_OUT; i++){
				//check if there is no more child
				if (child[i+1]==null){
					break;
				}
				
				result = i+1;
				
				if (current[i].key <= node.key && node.key <= child[i+1].getNodes()[0].key){
					break;
				}				
			}
		}
		return child[result];
	}
	
	public static void splitNode(Nodes[] pointer, Node node){
		Nodes current = pointer[0];
		Nodes branchNode = new Nodes();
		Nodes temp = new Nodes();
		
		for (int i=0; i <= (int) Math.floor(FAN_OUT/2); i++){
			temp.add(current.getNodes()[i]);
		}
		
		for (int i=(int) Math.floor(FAN_OUT/2) + 2; i < FAN_OUT; i++){
			branchNode.add(current.getNodes()[i]);
		}
		branchNode.add(node);
		
		//split child
		if (current.getChildNodeNumber()!=0){
			for (int i=0; i < current.getChildNodeNumber(); i++){
				if (i <= current.getChildNodeNumber()/2){
					temp.addChildNode(current.getChildNode()[i]);
				}
				if (i > current.getChildNodeNumber()/2){
					branchNode.addChildNode(current.getChildNode()[i]);
				}
			}
		}

		//get the middle node
		node = current.getNodes()[(int) Math.floor(FAN_OUT/2) + 1];
		
		//reuse node
		current.resetNodes();
		for (int i=0; i <= (int) Math.floor(FAN_OUT/2); i++){
			current.add(temp.getNodes()[i]);
			if (i < temp.getChildNodeNumber())
				current.addChildNode(temp.getChildNode()[i]);
		}
		
		//push to parent nodes
		if (current.getParentNode()==null){
			Btree.Root.resetNodes();
			Btree.Root.addChildNode(current);
		}
		current.getParentNode().addChildNode(branchNode);
		current.getParentNode().add(node);
	}
}
