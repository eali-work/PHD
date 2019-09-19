import java.io.File;
import java.util.*;

import org.apache.lucene.store.FSDirectory;


import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.*;
import de.tudarmstadt.ukp.wikipedia.api.CategoryGraph;
import de.tudarmstadt.ukp.wikipedia.api.CategoryGraphManager;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;

public class TermRelatedness {
	
	public static void main(String[] args) throws Exception
	{
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
	    dbConfig.setHost("localhost");
	    dbConfig.setDatabase("wikidb");
	    dbConfig.setUser("root");
	    dbConfig.setPassword("admin");
	    dbConfig.setLanguage(Language.english);
	    try 
	    {
		    Wikipedia wiki = new Wikipedia(dbConfig);
		    
		    
		  //  CategoryGraph CG = new CategoryGraph();
		    CategoryGraph CG  = CategoryGraphManager.getCategoryGraph(wiki, true);
		    System.out.println(CG.getDepth());
		    System.out.println(CG.getNumberOfNodes());
		    System.out.println(CG.getAverageShortestPathLength());
		    
		    Page p1 = wiki.getPage("Flood");
		    Page p2 = wiki.getPage("Huricane");
		    Category C1 = new Category(wiki,"Flood");
		    Category C2 = new Category(wiki,"Global warming and hurricanes");
		    Category C3 = CG.getLCS(C1, C2);
		    Category C4 = new Category(wiki,"Ctaegories");
		    System.out.println(CG.getPathLengthInEdges(C3, C4));
		    /*Set<Category> CatList1 = p1.getCategories();
		    
		    for(Category C: p1.getCategories())
		    {
		     System.out.println(C.getTitle());
		    }
		    
		    for(Category C: p2.getCategories())
		    {
		     System.out.println(C.getTitle());
		    }
		    
		    //Category C1 = new Category(wiki,"Flood");
		    //Category C2 = new Category(wiki,"Global warming and hurricanes");
		    
		   // Category C3 = CG.getLCS(C1, C2);
		    //System.out.println(CG.getNumberOfNodes());
		    //if (C3 != null) System.out.println(C3.getTitle());
		    //else System.out.println("Doesn't Exist");
		    System.out.println("------------------");
		   // CategoryGraphManager CM = new CategoryGraphManager();
		   //CG.saveGraph(graphPath);
		    //if (C1 != null) System.out.println(C1.getTitle());
		    //if (C2 != null) System.out.println(C2.getTitle());
		    //System.out.println(C3.getTitle());
		    
		  //  CG.getLCS(C1, C2)
		   * 
		   */
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e.getMessage());
	    }
	  }
	    
	    
		
		
	}


