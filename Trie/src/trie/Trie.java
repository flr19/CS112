package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root=new TrieNode(null,null,null);
		if (allWords.length==0) {
			return root;
		}
		for(int i=0;i<allWords.length;i++) {
			String word=allWords[i];
			if(root.firstChild==null) {
				Indexes firstWord=new Indexes(0, (short) 0,(short)(allWords[0].length()-1));
				root.firstChild=new TrieNode(firstWord,null,null);
			}
			else {
				commonPrefix(word,root,allWords,i);
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}

	private static TrieNode commonPrefix(String word,TrieNode root,String[] allWords,int i) {
		TrieNode prev=null;
		TrieNode temproot=root;
		TrieNode ptr=root.firstChild;
		int q = 1;
		while (q!=0) {
			int count = findCommonPrefix(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1), word.substring(ptr.substr.startIndex));
			if (count == 0 && ptr.sibling==null) {
				Indexes ofThisNode = new Indexes(i, (short) ptr.substr.startIndex, (short) (word.length() - 1));
				ptr.sibling = new TrieNode(ofThisNode, null, null);
				q=0;
			}
			else if(count == 0){
				prev=ptr;
				ptr = ptr.sibling;
			}
			else if(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1).length()==count) {
				temproot=ptr;
				prev=null;
				ptr=ptr.firstChild;

			}
			else if (count>0 && count<allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1).length()) {
				Indexes ofTemp = new Indexes(ptr.substr.wordIndex,ptr.substr.startIndex, (short) (ptr.substr.startIndex+count-1));
				TrieNode temp=new TrieNode(ofTemp,ptr,ptr.sibling);
				ptr.substr.startIndex= (short) (temp.substr.endIndex+1);
				ptr.sibling=new TrieNode(new Indexes(i,ptr.substr.startIndex, (short) (word.length()-1)),null,null);
				if(prev!=null) {
					prev.sibling=temp;
				}
				else {
					temproot.firstChild=temp;
				}
				q=0;
			}
		}
		return root;
	}

	private static int findCommonPrefix(String a, String b) {
		int count=0;
		for (int i = 0; i < a.length() && i < b.length(); i++) {
			char ptr1 = a.charAt(i);
			char ptr2=b.charAt(i);
			if(ptr1==ptr2) {
				count++;
			}
			else {
				break;
			}
		}
		return count;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		ArrayList<TrieNode> result = new ArrayList<>();
		TrieNode ptr = root.firstChild;
		while (ptr != null) {
			if (prefix.length() == 0) {
				if (ptr.firstChild != null) {
					result.addAll(completionList(ptr, allWords, prefix));
				} else {
					result.add(ptr);
				}
				//ptr = ptr.sibling;
			} else if (prefix.length() > allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1).length()) {
				if (prefix.substring(0, ptr.substr.endIndex-ptr.substr.startIndex + 1).equals(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1))) {
					if (ptr.firstChild != null) {
						result.addAll(completionList(ptr, allWords, prefix.substring(ptr.substr.endIndex -ptr.substr.startIndex+ 1)));
					}
				}
				//ptr = ptr.sibling;
			} else if(prefix.length() <= allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1).length()){
				if (prefix.equals(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.startIndex + prefix.length()))) {
					if (ptr.firstChild == null) {
						result.add(ptr);
					} else {
						result.addAll(completionList(ptr, allWords, ""));
					}

				}
				//ptr = ptr.sibling;

			}
			ptr=ptr.sibling;
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
