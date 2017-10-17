
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Graph<T>{
	
	HashMap<Integer, T> names = new HashMap<>(); // mapping integer to node of type T
	int[][] adjucencyWeightMatrex;
	HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
	private int nodes_num;
	public Graph(){
		nodes_num = 0;
	}
	public Graph(boolean[][] adjucencyMatrex, T[] nodes){
		nodes_num = adjucencyMatrex.length;
		adjucencyWeightMatrex = new int[nodes_num][nodes_num];
		for(int i=0;i<nodes_num;i++){
			ArrayList<Integer> adjs = new ArrayList<>();
			for(int j=0;j<nodes_num;j++){
				 
				if(adjucencyMatrex[i][j] == true){
					adjucencyWeightMatrex[i][j] = 1; // the weight is 1 between the two nodes
					adjs.add(j);
				}
			}
			graph.put(i,  adjs);
		}
		for(int i=0;i<nodes_num;i++){
			names.put(i, nodes[i]);
		}
	}
	/***
	 * receive weights on edges between nodes
	 * @param adjucencyWeightMatrex
	 * @param nodes
	 */
	public Graph(int[][] adjucencyWeightMatrex, T[] nodes){
		nodes_num = adjucencyWeightMatrex.length;
		this.adjucencyWeightMatrex = adjucencyWeightMatrex.clone();
		for(int i=0;i<nodes_num;i++){
			ArrayList<Integer> adjs = new ArrayList<>();
			for(int j=0;j<nodes_num;j++){
				if(adjucencyWeightMatrex[i][j] != 0){
					adjs.add(j);
				}
			}
			graph.put(i,  adjs);
		}
		for(int i=0;i<nodes_num;i++){
			names.put(i, nodes[i]);
		}
	}
	public boolean nodeExists(T node){
		for(int i : names.keySet()){
			if(names.get(i).equals(node)){
				return true;
			}
		}
		return false;
	}
	/***
	 * run Bellman Ford from the node input. it is needed if the graph is directed - in this case we cannot pickup one.
	 * @param source if the node is null, then the first node will be selected
	 * @return the distance of each node from the source node in a hashmap and its predecessor in int[2]{distance, predecessor}
	 */
	public HashMap<T, Integer[]> bellmanFordSPT(T source){
		int[] dist = new int[nodes_num];
		int[] predecessor = new int[nodes_num];
		
		
				
		int srcIndx = 0;
		if(source != null){
			// find the index of the source
			srcIndx = getNodeIndex(source);
		}
		for(int i=0;i<nodes_num;i++){
			dist[i] = Integer.MAX_VALUE;
			predecessor[i] = -1;
		}
		dist[srcIndx] = 0;
		for(int u=0;u<nodes_num;u++){
			for(int v=0;v<nodes_num;v++){ // needed to pass over the edges from node i to j
				if(v==u){ // no need for self edges
					continue;
				}
				
				if( dist[v]> dist[u]+ adjucencyWeightMatrex[u][v] ){
					dist[v] = dist[u]+ adjucencyWeightMatrex[u][v];
					predecessor[v] = u;
				}	
			}
		}
		// make sure there are no negative loops as we must quit
		for(int u=0;u<nodes_num;u++){
			for(int v=0;v<nodes_num;v++){
				if(adjucencyWeightMatrex[u][v] != 0 && dist[u]+ adjucencyWeightMatrex[u][v] < dist[u]){
					// means we have a negative loop
					throw new IllegalArgumentException("We have a negative loop in the graph");
				}
			}
		}
		// now set the result into hashtable - each node and its parent
		HashMap<T, Integer[]> res = new HashMap<>();
		for(int i=0;i<nodes_num;i++){
			if(predecessor[i] == -1 && i != srcIndx){ // make sure the predecessor was calculated correctly
				throw new IllegalArgumentException("Cannot calculate a predecessor for a node");
			}
			res.put(names.get(i), new Integer[]{dist[i], predecessor[i]});
		}
		return res;
	}
	
	
	/***
	 * run dijsktra from the node input. it is needed if the graph is directed - in this case we cannot pickup one.
	 * in case there is a negative edge, an error will be thrown
	 * @param source if the node is null, then the first node will be selected
	 * @return the distance of each node from the source node in a hashmap
	 */
	public HashMap<T, Integer> dijsktraSPT(T source){
		int[] dist = new int[nodes_num];
		boolean[] sptSet = new boolean[nodes_num];
		
				
		int srcIndx = 0;
		if(source != null){
			// find the index of the source
			srcIndx = getNodeIndex(source);
			
		}
		for(int i=0;i<nodes_num;i++){
			dist[i] = Integer.MAX_VALUE;
			sptSet[i] = false;
		}
		dist[srcIndx] = 0;
		for(int i=0;i<nodes_num;i++){
			int u = getMinDistanceThroughSPTSet(sptSet, dist);
			sptSet[i] = true;
			ArrayList<Integer> adj = getAdjucents(u);
			for(int v : adj){
				if( adjucencyWeightMatrex[u][v]  < 0){
					throw new IllegalStateException("Cannot calculate dijkstra on negative weighted edges");
				}
	            // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src (dist[u]) to
                // v through u is smaller than current value of dist[v]
				if(!sptSet[v] && dist[u] != Integer.MAX_VALUE && adjucencyWeightMatrex[u][v] !=0 && dist[v]> dist[u]+ adjucencyWeightMatrex[u][v] ){
					dist[v] = dist[u]+ adjucencyWeightMatrex[u][v];
				}
			}
		}
		
		// now set the result into hashtable - each node and its parent
		HashMap<T, Integer> res = new HashMap<>();
		for(int i=0;i<nodes_num;i++){
			res.put(names.get(i), dist[i]);
		}
		return res;
	}
	
	/***
	 * this function is used for dijkstra algorithm and it is to find the min dist node through the cut
	 * @param sptSet
	 * @param dist
	 * @return the index of the minimum node
	 */
	private int getMinDistanceThroughSPTSet(boolean[] sptSet, int[] dist) {
		int index = -1;
		int min = Integer.MAX_VALUE;
		for(int i=0;i<nodes_num;i++){
			if(!sptSet[i] && dist[i] != Integer.MAX_VALUE){
				if(dist[i] < min){
					min = dist[i];
					index = i;
				}
			}
		}
		return index;
	}
	
	/**
	 * kruskal is used to calculate the Minimum Spanning Tree (MST) in the graph
	 * @param source if the node is null, then the first node will be selected
	 * @return the weight adjacency matrix of the Minimum Spanning Tree (MST)
	 */
	public ArrayList<T> kruskalMST(T source){
		//TODO: 
		return null;
	}
	
	/**
	 * Prim is used to calculate the Minimum Spanning Tree (MST) in the graph
	 * @param source if the node is null, then the first node will be selected
	 * @return the weight adjacency matrix of the Minimum Spanning Tree (MST)
	 */
	public ArrayList<T> primMST(T source){
		return null;
	}
	
	private int getNodeIndex(T node){
		int nodeIndex = -1;
		for(int key : names.keySet()){
			if(names.get(key).equals(node)){
				nodeIndex = key;
				break;
			}
		}
		if(nodeIndex == -1){
			throw new IllegalArgumentException("The node does not exist");
		}
		return nodeIndex;
	}
	public ArrayList<T> topologicalSort(){
		ArrayList<T> res = new ArrayList<>();
		Stack<Integer> stack = new Stack<>();
		
		boolean[] visited = new boolean[nodes_num];  // store all the sub-graph in the result
		
		for(int i=0;i < nodes_num;i++){
			// the loop is needed to scan all graph components
			if(! visited[i]){
				topologicalSortUtil(i, stack,visited);
			}
		}
		
		// convert the indices into T obj
		for(int i : stack){
			res.add(0, names.get(i)); // add to the beginning as we are reading from stack
		}
		
		return res;
		
	}
	
	/**
	 * run topological scan on node i with the already given stack and the visited array to mark nodes in the connected graph
	 * @param root
	 * @param stack
	 * @param visited
	 */
	private void topologicalSortUtil(int root, Stack<Integer> stack, boolean[] visited) {
		visited[root] = true;
		ArrayList<Integer> adj = getAdjucents(root);
		for(int adjNode : adj){ // cover all graph connectivities
			if(!visited[adjNode]){
				topologicalSortUtil(adjNode, stack, visited);
			}
		}
		stack.push(root);
		
	}

	
	public void addNode(final T node){
		if(!nodeExists(node)){
			int newIndex = names.size();
			names.put(newIndex, node);
		}
	}
	
	public void addEdge(T n1, T n2){
		int n1Indx = getNodeIndex(n1);
		int n2Indx = getNodeIndex(n2);
		if(n1Indx != -1 && n2Indx != -1){
			
			ArrayList<Integer> adj = graph.get(n1);
			if(! adj.contains(n2Indx)){
				adj.add(n2Indx);
			}
		}
	}
	private boolean canNodeBeAddedToPath(ArrayList<Integer> path, int node){
		return !path.contains(node);
	}
	
	private boolean hasHamiltonianCycleAux(ArrayList<Integer> path){
		if(nodes_num == 0){
			return false;
		}
		if(path.size() == nodes_num && doesEdgeExists(path.get(path.size() -1 ), path.get(0)) ){
			return true;
		}
		ArrayList<Integer> adj = getAdjucents(path.get(path.size() - 1));
		for(Integer i : adj){
			if(canNodeBeAddedToPath(path, i)){
				path.add(i);
				return hasHamiltonianCycleAux(path);
			}
		}
		return false;
	}

	/**
	   Hamiltonian Path in an undirected graph is a path that visits each vertex exactly once. A Hamiltonian cycle (or Hamiltonian circuit) is a Hamiltonian Path such that there is an edge (in graph) from the last vertex to the first vertex of the Hamiltonian Path. Determine whether a given graph contains Hamiltonian Cycle or not. If it contains, then print the path. Following are the input and output of the required function.

		Input:
		A 2D array graph[V][V] where V is the number of vertices in graph and graph[V][V] is adjacency matrix representation of the graph. A value graph[i][j] is 1 if there is a direct edge from i to j, otherwise graph[i][j] is 0.
		
		@return An array path[V] that should contain the Hamiltonian Path. path[i] should represent the ith vertex in the Hamiltonian Path. The code should also return false if there is no Hamiltonian Cycle in the graph.
		
		For example, a Hamiltonian Cycle in the following graph is {0, 1, 2, 4, 3, 0}. There are more Hamiltonian Cycles in the graph like {0, 3, 4, 2, 1, 0}
	 * 
	 */
	public ArrayList<T> getHamiltonianCycle(){
		ArrayList<T>  res = new ArrayList<>();
		for(int i :names.keySet()){
			ArrayList<Integer> path = new ArrayList<>();
			path.add(i);
			if(hasHamiltonianCycleAux(path)){
				for(int node: path){
					res.add(names.get(node));
				}
				return res;
			}
		}
		
		return null;
	}
	
	public boolean hasHamiltonianCycle(){
		boolean res = false;
		for(int i :names.keySet()){
			ArrayList<Integer> path = new ArrayList<>();
			path.add(i);
			res = hasHamiltonianCycleAux(path);
			if(res){
				return true;
			}
		}
		
		return res;
	}
	public boolean hasCycle(){
		if(nodes_num == 0){
			return false;
		}
		Stack<Integer> s = new Stack<>();
		boolean[] visited = new boolean[nodes_num];
		boolean[] visiting = new boolean[nodes_num];
		s.push(0);
		while(!s.isEmpty()){
			int n = s.pop();
			
			visiting[n] = true;
			ArrayList<Integer> adj = getAdjucents(n);
			boolean path_ended = true;
			for(int i : adj){
				if(doesEdgeExists(n,i) && visited[i] == false){
					if(visiting[i] == true){
						return true;
					}
					s.push(i);
					path_ended = false;
				}
			}
			if(path_ended){
				for(int i = 0;i<nodes_num;i++){
					if(visiting[i] && !visited[i]){
						visited[i] = true;
					}
					visiting[i] = false;
				}
			}
		}
		return false;
	}

	private boolean doesEdgeExists(int n, int i) {
		return graph.containsKey(n) && graph.get(n).contains(i);
	}

	private ArrayList<Integer> getAdjucents(int n) {
		ArrayList<Integer> res = new ArrayList<>();
		if(!graph.containsKey(n))
			return res;
		return graph.get(n);
	}	
	
	public static void main(String[] args){
		
		boolean[][] graph = new boolean[6][6];
		int nodes_num = graph.length;
		graph[0][1] = true;
		graph[0][2] = true;
		graph[1][2] = true;
		graph[2][4] = true;
		graph[3][5] = true;
		graph[4][3] = true;
		graph[5][4] = true;
		for(int i=0;i<nodes_num;i++){
			for(int j=0;j<nodes_num;j++){
				System.out.print(graph[j][i]?" 1 ":" 0 ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasCycle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasHamiltonianCycle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).getHamiltonianCycle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).topologicalSort());

		
		graph = new boolean[4][4];
		nodes_num = graph.length;
		graph[0][1] = true;
		graph[1][2] = true;
		graph[2][3] = true;
		graph[3][0] = true;
		
		for(int i=0;i<nodes_num;i++){
			for(int j=0;j<nodes_num;j++){
				System.out.print(graph[j][i]?" 1 ":" 0 ");
			}
			System.out.println();
		}
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).hasCycle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).hasHamiltonianCycle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).getHamiltonianCycle());
		
		
		graph = new boolean[4][4];
		nodes_num = graph.length;
		graph[0][1] = true;
		graph[1][2] = true;
		graph[2][3] = true;
		graph[3][0] = true;
		
		for(int i=0;i<nodes_num;i++){
			for(int j=0;j<nodes_num;j++){
				System.out.print(graph[j][i]?" 1 ":" 0 ");
			}
			System.out.println();
		}
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).hasCycle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).hasHamiltonianCycle());

		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).getHamiltonianCycle());
		
		
		System.out.println("Topological test");
		graph = new boolean[6][6];
		nodes_num = graph.length;
		graph[0][1] = true;
		graph[0][2] = true;
		graph[1][2] = true;
		graph[2][4] = true;
		graph[3][5] = true;
		graph[4][3] = true;
		for(int i=0;i<nodes_num;i++){
			for(int j=0;j<nodes_num;j++){
				System.out.print(graph[j][i]?" 1 ":" 0 ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).topologicalSort());

	}
}
