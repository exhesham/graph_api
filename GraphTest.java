package training;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphTest {

	@Test
	public void test1() {
		boolean[][] graph = new boolean[6][6];
		graph[0][1] = true;
		graph[0][2] = true;
		graph[1][2] = true;
		graph[2][4] = true;
		graph[3][5] = true;
		graph[4][3] = true;
		graph[5][4] = true;

		assertTrue(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasCycle());
		assertFalse(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasHamiltonianCycle());
		assertTrue(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).getHamiltonianCycle() == null);
		assertArrayEquals(new Integer[]{0, 1, 2, 4, 3, 5}, new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).topologicalSort().toArray());
		
	}
	
	@Test
	public void test2() {
		boolean[][] graph = new boolean[6][6];
		graph[0][1] = true;
		graph[0][2] = true;
		
		graph[2][4] = true;
		graph[3][5] = true;
		graph[4][3] = true;
		

		assertFalse(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasCycle());
		assertFalse(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).hasHamiltonianCycle());
		assertTrue(new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).getHamiltonianCycle() == null);
		assertArrayEquals(new Integer[]{0, 2, 4, 3, 5, 1}, new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).topologicalSort().toArray());
		
		
	}
	
	
	@Test
	public void test3() {
		boolean[][] graph = new boolean[4][4];
		int nodes_num = graph.length;
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
		assertTrue(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).hasCycle());
		assertTrue(new Graph<Integer>(graph, new Integer[]{0,1,2,3}).hasHamiltonianCycle());
		assertArrayEquals(new Integer[]{0, 1, 2, 3}, new Graph<Integer>(graph, new Integer[]{0,1,2,3}).getHamiltonianCycle().toArray());
		
	}
	
	@Test
	public void test4() {
		boolean[][] graph = new boolean[6][6];
		int nodes_num = graph.length;
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
		assertArrayEquals(new Integer[]{0, 1, 2, 4, 3, 5}, new Graph<Integer>(graph, new Integer[]{0,1,2,3,4,5}).topologicalSort().toArray());
	}
	
	@Test
	public void test5() {
		
	}
	
	@Test
	public void test6() {
		
	}
	


}
