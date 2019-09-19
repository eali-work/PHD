package categorization;

import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import java.util.*;

/**
 * @purpose: To rank categories for a Wikipedia page using in-links to the page and extract highest ranked category
 * @author:  Tejas Nomulwar
 */

public class DomainConcept {

	private Wikipedia wiki ;
	private String page;
	private String category;

	public String getPage() {
		return page;
	}

	public String getCategory() {
		return category;
	}

	
	public DomainConcept(Wikipedia wiki){
		this.wiki = wiki;
	}

	private boolean isMaintenanceCategory (String title)
	{
		String filterOut[] = {"all articles with unsourced statements" , "all orphaned articles",
		"all articles needing style editing", "all disambiguation pages" , "all article", "all pages", "articles ", "aikipedia articles", "arphaned articles" };

		boolean found = false;
		String lowerCaseTitle = title.toLowerCase();

		for (int i =0; i< filterOut.length; i++){
			if (lowerCaseTitle.indexOf(filterOut[i])>0){
				found = true;
				break;
			}
		}
		return found;

	}

	private double getCatValue(Page page, Category cat)throws WikiApiException {
		int catValue = 0;
		double value =0;
	
		int total = page.getNumberOfInlinks();
		
		if(total > 10)
			total = 10;
		
		int cnt = 0;
		
		for (Page l_page : page.getInlinks()) {
			if(cnt++ == 10) break;
		
		for (Category p_cat : l_page.getCategories()) {
			if (! isMaintenanceCategory(p_cat.getTitle().toString()))
				if (cat.getTitle().toString().equalsIgnoreCase(p_cat.getTitle().toString())) {
					catValue ++;
					//System.out.println("found");
				}
			}
		}
		
		value = (double)catValue/(double)total;
		return value;
	}

	public String rankPageCategory(String p) throws WikiApiException {
		TreeMap<String, Double> cats = new TreeMap<String, Double>();
        String cat = "";
        
        Page page = wiki.getPage(p);
        
        if(!page.isDisambiguation()){
        	for (Category p_cat : page.getCategories()){
        		String catTitle = p_cat.getTitle().toString();
			
        		if(catTitle.indexOf("articles") == -1 && catTitle.indexOf("Articles") == -1 && catTitle.indexOf("pages") == -1 && catTitle.indexOf("Cleanup") == -1 && catTitle.indexOf("Disambiguation") == -1 && catTitle.indexOf("Living People") == -1){
        			double value = getCatValue(page,p_cat);
        			//System.out.println(catTitle);
        			//if(value >0)
					cats.put(catTitle, value);
        		}
        	}

        	Iterator<String> iter = sortByValues(cats).keySet().iterator();
        	
        	if(iter.hasNext()){
        		cat = iter.next().toString();
        	
        	}
        }
        else
        	cat = "dismbiguation page";
            
        if(cat.equals(""))
        	cat = "null";
        
        return cat;
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
