import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Graph<T>{
	HashMap<Integer, T> names = new HashMap<>(); // mapping integer to node of type T

	HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
	private int nodes_num;
	public Graph(){
		nodes_num = 0;
	}
	public Graph(boolean[][] adjucencyMatrex, T[] nodes){
		nodes_num = adjucencyMatrex.length;
		for(int i=0;i<nodes_num;i++){
			ArrayList<Integer> adjs = new ArrayList<>();
			for(int j=0;j<nodes_num;j++){
				if(adjucencyMatrex[i][j] == true){
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
	
	private int getNodeIndex(T node){
		for(int i : names.keySet()){
			if(names.get(i).equals(node)){
				return i;
			}
		}
		return -1;
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

	}
}
