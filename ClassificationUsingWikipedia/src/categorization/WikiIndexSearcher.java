package categorization;

import java.io.File;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @purpose:     To extract Wikipedia page for the top 10 weighted terms
 * @requirement: Wikipedia index and Lucene
 * @author:      Tejas Nomulwar
 */

public class WikiIndexSearcher {

	public static String  searchWiki(String q) throws Exception {
		File indexDir = new File("C:\\Wikipedia_Indexes\\wikiindex2009");
	        
		if (!indexDir.exists() || !indexDir.isDirectory()) {
			throw new Exception(indexDir + " is does not exist or is not a directory.");
	    }

		 return search(indexDir, q);
	        
	}

	public static String  search(File indexDir, String q)  throws Exception{
		
		Directory fsDir = FSDirectory.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(fsDir);
	    searcher.setSimilarity( new IsolationSimilarity() );
	    
	    QueryParser qparse = new QueryParser(Version.LUCENE_30,"contents", new StandardAnalyzer(Version.LUCENE_30));
	    // qparse.setDefaultOperator(QueryParser.AND_OPERATOR);
	    // String escaped = QueryParser.escape(q);
	    Query query = qparse.parse(q);

	    TopScoreDocCollector collector = TopScoreDocCollector.create(10,true);
	    searcher.search(query,collector );
	    // System.out.println("Hits: " + collector.getTotalHits());
	       
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	     
	    String pageTitle = "";
	    if(hits.length > 0){
	    	Document hitDoc = searcher.doc(hits[0].doc);
	        pageTitle = hitDoc.get("page");
	    }

	    return pageTitle; 
	    
	}
	    
}
