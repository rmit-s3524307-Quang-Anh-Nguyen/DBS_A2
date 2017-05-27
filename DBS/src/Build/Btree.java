package Build;

public final class Btree {
	private static final int NOT_SET = -1;
	public static final int FAN_OUT = 133;
	private static Nodes Root = new Nodes();
	
	private Btree(){}
		
	public static void addNode(Node node){
		//check for child
		if (Root.getChildNode()[0] == null){
			boolean full = Root.add(node);
			
			
			//split the nodes when it's full
			if (full){
				Root = Btree.splitNode(Root, Root.getNodes(), node);
			}
			
		} else {
			addNode(Root, compare(Root.getNodes(), Root.getChildNode(), node), node);
		}
		
	}
	
	public static boolean addNode(Nodes parent, Nodes current, Node node){
		boolean full = false;
		if (current.getChildNode()[0] == null){
			//simply add to node
			full = current.add(node);
			
			//split the nodes when it's full
			if (full){
				Nodes temp = Btree.splitNode(current, current.getNodes(), node);
				//add to parent
				parent.add(temp.getNodes()[0]);
				parent.addChildNode(temp.getChildNode()[0]);
				parent.addChildNode(temp.getChildNode()[1]);
			}
		} else {
			//recursively look for correct child
			if (addNode(current, compare(current.getNodes(), current.getChildNode(), node), node)){
				Nodes temp = Btree.splitNode(current, current.getNodes(), node);
				//add to parent
				parent.add(temp.getNodes()[0]);
			}
		}
		//nodes view
		for (int i=0; i<current.getNodes().length;i++){
			System.out.print(current.getNodes()[i].key+" ");
		}
		System.out.println();
		return full;
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
	
	public static Nodes splitNode(Nodes current, Node[] nodes, Node node){
		Nodes branchNode1 = new Nodes();
		Nodes branchNode2 = new Nodes();
		
		for (int i=0; i <= (int) Math.floor(FAN_OUT/2); i++){
			branchNode1.add(nodes[i]);
		}
		
		for (int i=(int) Math.floor(FAN_OUT/2) + 2; i < FAN_OUT; i++){
			branchNode2.add(nodes[i]);
		}
		branchNode2.add(node);
		
		//get the middle node
		node = nodes[(int) Math.floor(FAN_OUT/2) + 1];
		
		//push to parent nodes
		nodes = resetNode(nodes);
		nodes[0] = node;
		current.addChildNode(branchNode1);
		current.addChildNode(branchNode2);
		return current;
	}

	public static Node[] resetNode(Node[] nodes){
		for (int i=0; i<nodes.length; i++){
			int tempID[] = new int[2]; 
			nodes[i] = new Node(NOT_SET, tempID);
		}
		return nodes;
	}
}
