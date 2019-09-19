package categorization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * @purpose:     To weight terms extracted from web page and extract top 10 weighted terms
 * @requirement: file of stop words
 * @author:      Tejas Nomulwar
 */

public class TermFrequencyCalculation {
	private String stopFilePath;

	public TermFrequencyCalculation(String stopFilePath){
		this.stopFilePath = stopFilePath;
	}
	
	public String calculateTermFrequency(String text, String keywords, String title) throws IOException{
		StringTokenizer st = new StringTokenizer(text,"/, ");
		Map<String,Integer> words = new TreeMap<String,Integer>();

		int total = 0;
		while (st.hasMoreTokens()) {
			BufferedReader in = new BufferedReader(new FileReader(stopFilePath));
			String token = st.nextToken();
		    //token is a word not a number    
	        if(!isStopWord(token.toLowerCase(), in) ){
					if(!words.containsKey(token.toLowerCase()) )
						words.put(token.toLowerCase(),1);
					else{
						int old = words.get(token.toLowerCase());
						words.remove(token.toLowerCase());
						words.put(token.toLowerCase(), old+1);
					}
					total++;
			}
			in.close();
  		 }
		
		ArrayList<String> keywordsList = null;

		if(keywords != null){
			keywordsList = new ArrayList<String>();
		
			st = new StringTokenizer(keywords,"/, ");
			while(st.hasMoreTokens()){
				keywordsList.add(st.nextToken().toLowerCase());
			}
			//System.out.println("keywordsList: " + keywordsList);
		}
		ArrayList<String> titleList = null;
		if(title != null){
			titleList = new ArrayList<String>();
		
			st = new StringTokenizer(title,"/, ");
			while(st.hasMoreTokens()){
				titleList.add(st.nextToken().toLowerCase());
			}
			//System.out.println("titleList: " + titleList);
		}

		 Map<String,Double> wordsWithTFs = new TreeMap<String,Double>();

		 for(String word : words.keySet()){
			   double tf = words.get(word);
			   tf = tf / (double) total;
			   
			   if(keywordsList != null && keywordsList.contains(word))
				   tf *= 2;
			   
			   if(titleList != null && titleList.contains(word))
				   tf *= 2;

			   wordsWithTFs.put(word, new Double(tf));
		 }
		 
		 Map<String,Double> sortedWords = new TreeMap<String,Double>();
	 
		 sortedWords = sortByValues(wordsWithTFs);
		// System.out.println(sortedWords);
		 
		 Iterator<String > iter = sortedWords.keySet().iterator();
		// System.out.println("Top 10 keywords:");
		 String key = "";
		 String topTenWords = "";
		 
		//System.out.println(sortedWords.size());
		int j = 0;
		for(int i = 0 ; i < 10 && i < sortedWords.size(); i++){
			 j++;	 
			 key = iter.next().toString();

			 if(j < 5)
				 topTenWords += key+"^" +2+ " ";
			 else 
				 topTenWords += key+" ";
		}
		return topTenWords;
	}
	
	public boolean isStopWord(String token, BufferedReader in) throws IOException{
		String word = "";
		
        while((word = in.readLine()) != null){
			if(word.equalsIgnoreCase(token)){
				return true;
			}
		}
        return false;
	}
	
	  
    public boolean isIntNumber(String num){
    	try{
    		Integer.parseInt(num);
    	}	 
    	catch(NumberFormatException nfe) {
    		return false;
	    }
	   	return true;
	 }
    
    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
    	Comparator<K> valueComparator =  new Comparator<K>() {
    	    public int compare(K k1, K k2) {
    	        int compare = map.get(k2).compareTo(map.get(k1));
    	        if (compare == 0) return 1;
    	        else return compare;
    	    }
    	};
    	Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
    	sortedByValues.putAll(map);
    	return sortedByValues;
    }
}