import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import net.htmlparser.jericho.*;

public class CorpusIndex {

public void generateIndex()
{   

//  Following 4 lines create a folder for output in current directory, and save it's path into the string indexPath
//  I find this better than to give an absolute path 
	
	String outputfolder="IndexedOutput";    //name of the output directory
	String indexPath = System.getProperty("user.dir")+"\\"+outputfolder;    //path of the output directory
	File outDir = new File(indexPath);
	outDir.mkdir();
	
//	String indexPath = "C:\\Javaworkspace\\Indexing\\output"; //  -- Add the folder for the generated index
	IndexWriter writer = null;
	int pCount = 0;
	   try 
	   {
	   	  Directory dir = FSDirectory.open(new File(indexPath));
		  Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
		  IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);
		  iwc.setOpenMode(OpenMode.CREATE);
		  iwc.setRAMBufferSizeMB(512);
		  writer = new IndexWriter(dir, iwc);
		}
		catch (IOException e) 
		{
		    System.out.println(" caught a " + e.getClass() +
		      "\n with message: " + e.getMessage());
		}
		try {
			  /*
			   * in this part you have to read all the URLs form a text file to a set and iterate over them
			   */
	                                        
	                                        
	           Iterable<String> urls = getUrls(); // You have to write getUrls
			   Iterator<String> iter = urls.iterator();
	            while(iter.hasNext())
	            {
	            	String url = (String)iter.next();
					//-- send the URl to text extractor here
	            	TextExtracter webpage = new TextExtracter(url);
	            	webpage.extractText();
	            	String content =webpage.getText(); //extracted Text();
	            	String title =webpage.getTitle(); // extracted title 
					System.out.println(pCount);
					pCount++;
				    Document doc = new Document();
					Field contentField = new Field("contents",content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
					Field titleField = new Field("title", title,Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES );
					doc.add(contentField);
					doc.add(titleField);
					writer.addDocument(doc);
					
	            }
					writer.close();
				}
				catch (Exception e) 
				{
				  e.printStackTrace();
				}
					  		
			}

            public static Vector<String> getUrls()
            {
            	Vector<String> urls=new Vector<String>();
            	  try
            	  {            	  
            	     FileInputStream fstream = new FileInputStream("c:\\dataset\\Weather.txt");
            	     DataInputStream in = new DataInputStream(fstream);
            	     BufferedReader br = new BufferedReader(new InputStreamReader(in));
            	     String strLine;            	  
            	     while ((strLine = br.readLine()) != null)   
            	     {
            	    	 strLine=strLine.trim();
            	    	 if(strLine.length()==0)
            	    	 {continue;}
            	    	 urls.add(strLine);
            	     }
            	     in.close();
            	   }
            	  catch (Exception e)
            	  {
            	    System.err.println("Error: " + e.getMessage());
            	  }
            	  return(urls);
            }
            
            public static void searchWiki(String indexpath)
            {
             
            	
              try{
            	  Directory wikidir = FSDirectory.open(new File(indexpath));
               Iterable<String> urls = getUrls(); // You have to write getUrls
 			   Iterator<String> iter = urls.iterator();
 	           while(iter.hasNext())
 	            {
 	        	  String url = (String)iter.next();
 	        	  TextExtracter webpage = new TextExtracter(url);
	            	webpage.extractText();
	            	String content =webpage.getText(); //extracted Text();
	            	String title =webpage.getTitle(); // extracted title
	            	String info = content + title;
	            	//Directory fsDir = FSDirectory.open(wikidir);
	        		
	            	//---Start of Wiki search
	            	
	            	@SuppressWarnings("deprecation")
					IndexSearcher searcher = new IndexSearcher(wikidir);
	        	    //searcher.setSimilarity( new IsolationSimilarity() );
	        	    
	        	    QueryParser qparse = new QueryParser(Version.LUCENE_30,info, new StandardAnalyzer(Version.LUCENE_30));
	        	    // qparse.setDefaultOperator(QueryParser.AND_OPERATOR);
	        	    // String escaped = QueryParser.escape(q);
	        	    Query query = qparse.parse(info);

	        	    TopScoreDocCollector collector = TopScoreDocCollector.create(10,true);
	        	    searcher.search(query,collector );
	        	    // System.out.println("Hits: " + collector.getTotalHits());
	        	       
	        	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	        	     
	        	    String pageTitle = "";
	        	    if(hits.length > 0){
	        	    	Document hitDoc = searcher.doc(hits[0].doc);
	        	        pageTitle = hitDoc.get("title");
	        	        System.out.println(pageTitle);
	        	    }

	        	    //return pageTitle; 

 	            }
 	           
 	           //---End of Wikisearch
 	           
 	           
 	           
              }
              catch (Exception e) 
              {
				  e.printStackTrace();
              }
            }

			public static void main(String[] args) throws Exception
			{
			 
				//CorpusIndex index = new CorpusIndex();
			    //index.generateIndex();
				searchWiki("c:\\WikipediaIndex\\");
			
			}
	}
	


