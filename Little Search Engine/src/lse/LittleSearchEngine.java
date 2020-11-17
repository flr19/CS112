package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		HashMap<String, Occurrence> keywordOccurences = new HashMap<String, Occurrence>();
		File scanner=new File(docFile);
		if(!scanner.exists()) {
			throw new FileNotFoundException();
		}
		Scanner sc=new Scanner(scanner);

		while(sc.hasNext()) {
				String key=getKeyword(sc.next());
				//System.out.println("k");
				if (key!=null) {
					//System.out.println(key);
					if (keywordOccurences.containsKey(key)) {
						Occurrence ofKey = keywordOccurences.get(key);
						ofKey.frequency++;

					} else {
						Occurrence ofKey = new Occurrence(docFile, 1);
						keywordOccurences.put(key, ofKey);
					}
				}
		}
		System.out.println(keywordsIndex);
		return keywordOccurences;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		if(kws==null||kws.isEmpty()) {return;}
		Set<String> keys = kws.keySet();  // Set is of type String since keys are Strings
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Occurrence ofKey=kws.get(key);
			if (keywordsIndex.containsKey(key)) {
				keywordsIndex.get(key).add(ofKey);
				insertLastOccurrence(keywordsIndex.get(key));
			}
			else {
				ArrayList<Occurrence> oc=new ArrayList<>();
				oc.add(ofKey);
				keywordsIndex.put(key,oc);
			}

		}

	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if(word==null) {return null;}
		word=word.toLowerCase();
		word=recurseFront(word);
		word=recurseBack(word);

		if ((word == null) || (word.equals(null)))
			return null;

		for(int i=0;i<word.length();i++) {

			char compared = word.charAt(i);
			if (!(Character.isLetter(compared))) {
				return null;
			}
		}
			if (noiseWords.contains(word)) {
				return null;
			} else {
				return word;
			}

	}

	private String recurseFront(String word) {
		if(word==null || word.length()==0) {
			return null;
		}
		if (word.charAt(0)==','||word.charAt(0)=='.'||word.charAt(0)=='?'||word.charAt(0)=='!'||word.charAt(0)==':'|| word.charAt(0)==';') {
			return recurseFront(word.substring(1));
		} else {
			return word;
		}
	}

	private String recurseBack(String word){
		if(word==null) {
			return null;
		}
		if(word.charAt(word.length()-1)==','||word.charAt(word.length()-1)=='.'||word.charAt(word.length()-1)=='?'||word.charAt(word.length()-1)=='!'||word.charAt(word.length()-1)==':'|| word.charAt(word.length()-1)==';') {
			return recurseBack(word.substring(0,word.length()-1));
		}
		else {
			return word;
		}
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		int lo = 0;
		int hi= occs.size()-2;
		int lastFreq=occs.get(occs.size()-1).frequency;
		int mid = 0;
		ArrayList<Integer> midpoints = new ArrayList<Integer>();
		while(lo<=hi) {

			mid=(lo+hi)/2;
			midpoints.add(mid);
			int midFreq=occs.get(mid).frequency;
			if(lastFreq<midFreq) {
				lo=mid+1;
			}
			else if(lastFreq>midFreq) {
				hi=mid-1;
			}
			else {
				break;
			}
		}
		int targetFreq=occs.get(mid).frequency;
		if(lastFreq>targetFreq) {
			Occurrence temp=occs.remove(occs.size()-1);
			occs.add(mid,temp);
		}
		else {
			Occurrence temp=occs.remove(occs.size()-1);
			occs.add(mid+1,temp);
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		//System.out.println(midpoints);
		return midpoints;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			//System.out.println("k");
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
			//System.out.println("k");
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> results=new ArrayList<>(5);
		ArrayList<Occurrence> occArr1=new ArrayList<>();
		ArrayList<Occurrence> occArr2=new ArrayList<>();
		ArrayList<String> documents=new ArrayList<>(5);
		occArr1= keywordsIndex.get(kw1);
		occArr2=keywordsIndex.get(kw2);
		int i=0;
		int j=0;
		if (occArr1!=null && occArr2!=null) {

			while (i < occArr1.size() || j < occArr2.size()) {
				//System.out.println("k");
				if (i == occArr1.size()) {
					results.add(occArr2.get(j).document);
					j++;
				}
				else if (j == occArr2.size()) {
					results.add(occArr1.get(i).document);
					i++;
				}
				else if (occArr1.get(i).frequency > occArr2.get(j).frequency) {
					results.add(occArr1.get(i).document);
					i++;

				}
				else if (occArr2.get(j).frequency > occArr1.get(i).frequency) {
					results.add(occArr2.get(j).document);
					j++;
				}
				else {
					results.add(occArr1.get(i).document);
					i++;
				}
			}
		}

		else if (occArr1==null && occArr2!=null){
					while(i!=occArr2.size()){
						results.add(occArr2.get(i).document);
						i++;
					}
				}

		else if (occArr2==null && occArr1!=null){
					while(j!=occArr1.size()) {
						results.add(occArr1.get(j).document);
						j++;
					}
				}

		else {return null;}
		//System.out.println(occArr1);
		//System.out.println(occArr2);
		//System.out.println(results);

		for(int a=0;a<results.size()-1;a++) {
			for(int b=a+1;b<results.size();b++) {
				if(results.get(a).equals(results.get(b))) {
					results.remove(b);
				}
			}
		}
		//System.out.println(results);
		while(results.size()>5) {
			results.remove(results.size()-1);
		}



		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return results;
	
	}
}
