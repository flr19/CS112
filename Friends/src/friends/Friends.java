package friends;

import java.util.ArrayList;

import structures.Queue;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 *
	 * @param g  Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 * path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		if (p1.equals(p2)) {
			return null;
		}
		if (!g.map.containsKey(p1)||!g.map.containsKey(p2)) {
			return null;
		}
		Boolean[] visited = new Boolean[g.members.length];
		for (int i = 0; i < g.members.length; i++) {
			visited[i] = false;
		}
		Queue<Integer> nodes = new Queue();
		ArrayList<ArrayList<Integer>> dequeued = new ArrayList<>();
		String key = p1;
		if (!g.map.containsKey(p1)) {
			return null;
		}
		int index = g.map.get(p1);
		nodes.enqueue(index);
		visited[index] = true;
		boolean p = false;
		while (!nodes.isEmpty()) {
			int original = nodes.dequeue();
			ArrayList<Integer> dequeued1 = new ArrayList<>();
			dequeued1.add(0, (Integer) original);
			for (Friend f = g.members[original].first; f != null; f = f.next) {

				if (visited[f.fnum] == false) {


					visited[f.fnum] = true;
					nodes.enqueue(f.fnum);
					dequeued1.add(f.fnum);

					if (g.map.get(p2) == f.fnum) {
						p = true;
						break;
					}

				}
			}
			dequeued.add(dequeued1);
			if (p) {
				break;
			}

		}
		if (!p) {
			return null;
		}

		ArrayList<String> result = new ArrayList<>();
		result.add(p2);
		int temp = dequeued.get(dequeued.size() - 1).get(0);
		result.add(0,g.members[dequeued.get(dequeued.size() - 1).get(0)].name);
		for (int i = dequeued.size() - 2; i >= 0; i--) {
			for (int j = 1; j < dequeued.get(i).size(); j++) {
				if (dequeued.get(i).get(j) == temp) {
					result.add(0, g.members[dequeued.get(i).get(0)].name);
					temp = dequeued.get(i).get(0);
					break;
				}
			}
		}
		return result;
	}


	/**
	 * Finds all cliques of students in a given school.
	 * <p>
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 *
	 * @param g      Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 * given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		Boolean[] visited = new Boolean[g.members.length];
		school = school.toLowerCase();
		for (int i = 0; i < g.members.length; i++) {
			visited[i] = false;
		}
		ArrayList<ArrayList<String>> results = new ArrayList<>();
		ArrayList<String> results1 = null;
		for (int i = 0; i < g.members.length; i++) {
			if (!visited[i] && g.members[i].student && g.members[i].school.equals(school)) {
				visited[i] = true;
				results1 = new ArrayList<>();
				results1.addAll(recurse(g, i, school, visited));
				results.add(results1);
			}
			//System.out.println(results);
		}
		if (results.isEmpty()) {
			return null;
		}
		return results;
	}

	private static ArrayList<String> recurse(Graph g, int i, String school, Boolean[] visited) {
		ArrayList<String> names = new ArrayList<>();
		names.add(g.members[i].name);
		ArrayList<String> temp=new ArrayList<>();
		//visited[i]=true;
		for (Friend f = g.members[i].first; f != null; f = f.next) {
			if (!visited[f.fnum] && g.members[f.fnum].student && g.members[f.fnum].school.equals(school)) {
				visited[f.fnum] = true;
				temp=recurse(g, f.fnum, school, visited);
				if (temp!=null) {
					names.addAll(temp);
				}

			}
		}
		return names;
	}


	/**
	 * Finds and returns all connectors in the graph.
	 *
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> connections = new ArrayList<>();
		int topnum = 1;
		int[] dfsnum = new int[g.members.length];
		int[] back=new int[g.members.length];
		boolean startV=false;

		Boolean[] visited = new Boolean[g.members.length];
		ArrayList<String> temp = null;
		for (int i = 0; i < g.members.length; i++) {
			visited[i] = false;
		}

		for (int v = 0; v < visited.length; v++) {
			if (!visited[v]) { // start/restart at v
				startV=false;
				int startIndex=v;
				temp =  dfs(v,visited,g,dfsnum,back,topnum,startIndex,startV);
				if(temp!=null){connections.addAll(temp);}
			}
			/** COMPLETE THIS METHOD **/

			// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
			// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
			//System.out.println(connections);
			for(int i=0; i<connections.size(); i++) {
				for(int j=i+1; j<connections.size(); j++) {
					if(connections.get(i).equals(connections.get(j))) {
						connections.remove(j);
					}
				}
			}

		}
		//System.out.println("sdfsdgsg");
		return connections;
	}


	// recursive dfs
	private static ArrayList<String> dfs(int v, Boolean[] visited,Graph g, int[] dfsnum, int[] back, int topnum, int startindex,boolean startV) {
		ArrayList<String> connectors1=new ArrayList<>();
		visited[v] = true;
		dfsnum[v]=topnum++;
		back[v]=dfsnum[v];
		for (Friend f = g.members[v].first; f!= null; f = f.next) {
			if (!visited[f.fnum]) {
				connectors1.addAll(dfs(f.fnum, visited,g,dfsnum,back,topnum,startindex,startV));
				//coming here, is backing up
				if(dfsnum[v]>back[f.fnum]) {
					back[v]=back[v]>back[f.fnum]?back[f.fnum]:back[v];
				}
				else {
					if(v!=startindex) {
						connectors1.add(g.members[v].name);
					}
					else {
						if (startV) {
							connectors1.add(g.members[v].name);
						}
						else {
							startV=true;
						}
					}
				}

			}
			else{
					back[v]=back[v]>dfsnum[f.fnum]?dfsnum[f.fnum]:back[v];
			}
		}
		return connectors1;
	}

}

