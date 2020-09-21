import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FacebookFriends {

	public static void main(String[] args) throws IOException {

		addData("src/facebook_combined.txt");

	}

	static List<List<Integer>> graph2 = new ArrayList<List<Integer>>(4031);
	static List<Integer> discovered = new ArrayList<Integer>();
	static List<Integer> discoveredDFS = new ArrayList<Integer>();
	static List<Integer> frontiers = new ArrayList<Integer>();
	static LinkedList<Integer> queue = new LinkedList<Integer>();
	static LinkedList<Integer> stack = new LinkedList<Integer>();

	static int frontier = 0;
	static int max = 0;
	static int reset = 0;

	/**
	 * Reads from the inputed file and first finds the largest node. Then it creates
	 * a list of lists that will store each node's edges in a list at that node's
	 * index. No inputs or outputs.
	 * 
	 * @param fileLoc is the location of the file that will be read
	 */
	public static void addData(String fileLoc) throws IOException {

		try {
			BufferedReader reader1 = new BufferedReader(new FileReader(fileLoc));
			String line1 = reader1.readLine();
			while (line1 != null) {
				String[] data = line1.split(" ");
				int node11 = Integer.parseInt(data[0]);
				int node21 = Integer.parseInt(data[1]);
				if (node11 > max || node21 > max) {
					max = Math.max(node11, node21);
				}
				line1 = reader1.readLine();
			}

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < max + 1; i++) {
			graph2.add(null);
			frontiers.add(0);
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/facebook_combined.txt"));
			String line = reader.readLine();
			while (line != null) {

				String[] data = line.split(" ");
				int node1 = Integer.parseInt(data[0]);
				int node2 = Integer.parseInt(data[1]);

				// Adds nodes and edges (to both sides)
				if (graph2.get(node1) == null) {
					List<Integer> toAdd = new ArrayList<Integer>();
					toAdd.add(node2);
					graph2.set(node1, toAdd);
				} else {
					graph2.get(node1).add(node2);
				}
				if (graph2.get(node2) == null) {
					List<Integer> toAdd = new ArrayList<Integer>();
					toAdd.add(node1);
					graph2.set(node2, toAdd);

				} else {
					graph2.get(node2).add(node1);

				}
				line = reader.readLine();
			}

			reader.close();

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Takes in a starting node and runs a breadth first search of the graph
	 * beginning at that node. It doesn't return anything but it updates the
	 * frontiers list and discovered list.
	 * 
	 * @param start1 starting node
	 */
	public static void bfs(int start1) {

		if (start1 > graph2.size() || start1 < 0) {
			System.out.println("Invalid start node");
		} else {

			discovered = new ArrayList<Integer>();
			frontiers = new ArrayList<Integer>();
			for (int i = 0; i < max + 1; i++) {
				frontiers.add(0);
			}
			queue = new LinkedList<Integer>();
			int start = start1;
			discovered.add(start);
			addDiscover(start);

			// Goes through every node
			while (discovered.size() != graph2.size()) {

				if (queue.size() != 0) {
					// removes first from queue
					int y = queue.remove();
					addDiscover(y);
				}

				if (queue.size() == 0) {
					// finds random node not in discovered
					for (int i = 0; i < graph2.size(); i++) {
						if (discovered.contains(i) == false) {
							if (graph2.get(i).size() != 0) {
								int y = i;
								addDiscover(y);
								reset++;
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Takes in a starting node and runs a depth first search of the graph beginning
	 * at that node. It doesn't return anything but it updates the discoveredDFS
	 * list.
	 * 
	 * @param start1 starting node
	 */
	public static void dfs(int start1) {

		if (start1 > graph2.size() || start1 < 0) {
			System.out.println("Invalid start node");
		} else {
			discoveredDFS = new ArrayList<Integer>();
			stack = new LinkedList<Integer>();
			discoveredDFS.add(start1);
			// addDiscoverDFS(start);
			stack.add(start1);
			addDiscoverDFS(start1);
			// Goes through every node
			while (discoveredDFS.size() != graph2.size()) {

				if (stack.size() != 0) {
					// removes first from queue
					int src = stack.removeFirst();

					addDiscoverDFS(src);

				}
				if (stack.size() == 0) {
					// finds random node not in discovered
					for (int i = 0; i < graph2.size(); i++) {
						if (discoveredDFS.contains(i) == false) {
							if (graph2.get(i).size() != 0) {
								int y = i;
								addDiscoverDFS(y);
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Takes in a node and finds all the nodes adjacent to it. It adds these to the
	 * discovered list and also to the queue. Then it finds which frontier it is in
	 * and places that in the corresponding index of the frontiers list
	 * 
	 * @param node node of which the edges will be found
	 */
	public static void addDiscover(int node) {
		List<Integer> edges = new ArrayList<Integer>();
		edges = graph2.get(node);
		Iterator<Integer> iter = edges.iterator();
		// iterates through the list of edges
		while (iter.hasNext()) {
			int x = iter.next();
			// if it isnt discovered yet it adds it to the discovered list
			if (!discovered.contains(x)) {
				discovered.add(x);
				queue.add(x);
				frontiers.set(x, (frontiers.get(node) + 1));

			}
		}
	}

	/**
	 * Takes in a node and finds all the nodes adjacent to it. It adds these to the
	 * discovered list and also to the stack.
	 * 
	 * @param node node of which the edges will be found
	 */
	public static void addDiscoverDFS(int node) {
		List<Integer> edges = new ArrayList<Integer>();
		edges = graph2.get(node);
		Iterator<Integer> iter2 = edges.iterator();
		// iterates through the list of edges
		while (iter2.hasNext()) {
			int x = iter2.next();
			// if it isnt discovered yet it adds it to the discovered list
			if (!discoveredDFS.contains(x)) {
				stack.addFirst(x);
				discoveredDFS.add(x);
			}

		}
	}

}
