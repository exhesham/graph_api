# Graph

## Creation
You can create a graph using adjacency matrix or using the API to create the nodes and the edges.
The adjecency matrix can be boolean or int weighted edges.

When adding a edge to empty graph, you can add the weight too.

Here is an example of constructing a graph from adjacencey matrix:

```
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
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasCicle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasHamiltonianCycle());
		System.out.println(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).getHamiltonianCycle());
```

## Hamiltonian Cycle
Hamiltonian Path in an un/directed graph is a path that visits each vertex exactly once. A Hamiltonian cycle (or Hamiltonian circuit) is a Hamiltonian Path such that there is an edge (in graph) from the last vertex to the first vertex of the Hamiltonian Path. Determine whether a given graph contains Hamiltonian Cycle or not. If it contains, then print the path. Following are the input and output of the required function.
The API Tells whether there is a hamiltonian cycle and also returns it

```
getHamiltonianCycle: Returns the array of the cycle. null if doesn't exist
hasHamiltonianCycle: boolean result if there is a hamiltonian cycle
```

## Cycle
Depth First Traversal can be used to detect cycle in a Graph. DFS for a connected graph produces a tree. There is a cycle in a graph only if there is a back edge present in the graph. A back edge is an edge that is from a node to itself (selfloop) or one of its ancestor in the tree produced by DFS. In the following graph, there are 3 back edges, marked with cross sign. We can observe that these 3 back edges indicate 3 cycles present in the graph.

```
public boolean hasCycle()
```

## Shortest Path Tree
SPT (Shortest path tree) can be calculated using whether Dijkstra or Bellman-Ford Algorithm. The first is more efficient. However, it is not effective when we have negative weighted edges. Therefore, in this case, we should be using Bellman-Ford.

The Dijkstra function input is the node we want to start running the algorithm from. since it doesn't matter if the graph is undirected, the value of the input parameter in this case should be null. as a result, the first node will be the source.

Exception of illigal input will be thrown in case there is a negative weighted graph.

```
public HashMap<T, Integer> dijsktraSPT(T source)

```

The next function will return for each node its distance from source and predecessor. both values in int[2]{dist, predecessor}
```
public HashMap<T, Integer[]> bellmanFordSPT(T source)
```
