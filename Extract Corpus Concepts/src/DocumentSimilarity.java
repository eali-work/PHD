import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.*;
import net.htmlparser.jericho.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;	
import org.apache.lucene.util.Version;

public class DocumentSimilarity {
	private IndexReader reader;
	private String content;
	public DocumentSimilarity(IndexReader rd, String text) 
	{
	}
	
	public Document[] getSimilarDocument(int max)
	{
		int size = max;
		Document[] docs = new Document[size];
		return docs;
	}
	
	private static String getTitle(Source source) {
		Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
		
		if (titleElement==null) return null;
		// TITLE element never contains other tags so just decode it collapsing whitespace:
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}

	private static String getMetaValue(Source source, String key) {
		for (int pos=0; pos<source.length();) {
			StartTag startTag=source.getNextStartTag(pos,"name",key,false);
			if (startTag==null) return null;
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}
	
	private static String getBodyValue(Source source)
	{
		Element bodyElement=source.getFirstElement(HTMLElementName.BODY);
		if (bodyElement == null) return null;
		return CharacterReference.decodeCollapseWhiteSpace(bodyElement.getContent());
	}
	
	private static String extractTextHtml(String sourceUrlString) throws Exception
	{
		String text = null;
		MicrosoftConditionalCommentTagTypes.register();
		PHPTagTypes.register();
		PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
		MasonTagTypes.register();
		Source source=new Source(new URL(sourceUrlString));
		source.fullSequentialParse();

		/*System.out.println("Document title:");
		String title=getTitle(source);
		System.out.println(title==null ? "(none)" : title);

		System.out.println("\nDocument description:");
		String description=getMetaValue(source,"description");
		System.out.println(description==null ? "(none)" : description);

		System.out.println("\nDocument keywords:");
		String keywords=getMetaValue(source,"keywords");
		System.out.println(keywords==null ? "(none)" : keywords);
		//IndexReader reader = IndexReader.open(FSDirectory.open(new File("c:\\WikipediaIndex-vectors\\")));
		//System.out.println(reader.maxDoc());
	*/
	//	System.out.println("\nDocument Body:");
	//	String body = getBodyValue(source);
	//	System.out.println(body==null? "none" : body);
		/*List<Element> linkElements=source.getAllElements(HTMLElementName.A);
		for (Element linkElement : linkElements) {
			String href=linkElement.getAttributeValue("href");
			if (href==null) continue;
			// A element can contain other tags so need to extract the text from it:
			String label=linkElement.getContent().getTextExtractor().toString();
			System.out.println(label+" <"+href+'>');
		}
*/
		System.out.println("\nAll text from file (exluding content inside SCRIPT and STYLE elements):\n");
		text = source.getTextExtractor().setIncludeAttributes(false).toString();

		//System.out.println("\nSame again but this time extend the TextExtractor class to also exclude text from P elements and any elements with class=\"control\":\n");
		//TextExtractor textExtractor=new TextExtractor(source) {
		//	public boolean excludeElement(StartTag startTag) {
		//		return startTag.getName()==HTMLElementName.P || "control".equalsIgnoreCase(startTag.getAttributeValue("class"));
		//	}
		//};
		//System.out.println(textExtractor.setIncludeAttributes(true).toString());
			
		return text;
	}
	public String extractPdf()
	{
		String text = null;
		//IndexReader reader = IndexReader.open(FSDirectory.open(new File("c:\\WikipediaIndex-vectors\\")));
		//System.out.println(reader.maxDoc());
		return text;
	}
	public String extractTextDoc()
	{
		String text = null;
		//IndexReader reader = IndexReader.open(FSDirectory.open(new File("c:\\WikipediaIndex-vectors\\")));
		//System.out.println(reader.maxDoc());
		return text;
	}
	public String extractTextXml()
	{
		String text = null;
		//IndexReader reader = IndexReader.open(FSDirectory.open(new File("c:\\WikipediaIndex-vectors\\")));
		//System.out.println(reader.maxDoc());
		return text;
	}
	
	public String getFileFormat(String fileName)
	{
		String format = "html";
		return format;
	}
	
	public void search(String courpus, String wiki_index)
	{
		
	}
	
	private static void indexCorpus(String corpus)
	{
		try
		{
			
		FileInputStream fstream = new FileInputStream(corpus);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
			while ((strLine = br.readLine()) != null)  
			{
				System.out.println(strLine);
			}
		}
		catch (IOException e)
		{
			System.out.println(" caught a " + e.getClass() +
				      "\n with message: " + e.getMessage());
		}
		
	}
	public static void main(String[] args) throws Exception
	{
		String text;
		IndexWriter writer = null;
		IndexReader rd = null;
		String corpus_index = "c:\\temp\\";
		String corpus_path = "c:\\dataset\\";
		indexCorpus(corpus_path);
		try 
	    {
			Directory dir = FSDirectory.open(new File(corpus_index));
  	  		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
  	  		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);
  	  		iwc.setOpenMode(OpenMode.CREATE);
  	  		iwc.setRAMBufferSizeMB(1048);
  	  		writer = new IndexWriter(dir, iwc);
  	  		rd = IndexReader.open(FSDirectory.open(new File("c:\\WikipediaIndex-vectors\\")));
	    }
		catch (IOException e)
		{
			System.out.println(" caught a " + e.getClass() +
				      "\n with message: " + e.getMessage());
		}
		
		//IndexReader 
		//DocumentSimilarity ds = new DocumentSimilarity(rd,"");
		
		/*text = extractTextHtml("http://www.cambridgesemantics.com/technology/semantic-data-collaboration");
		System.out.println(text);
		Document doc = new Document();
		Field content = new Field("contents",text, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES);
 	    doc.add(content);
 	    writer.addDocument(doc);
 	    
 	    text = extractTextHtml("http://www.semagix.com/what-is-semantic-data.htm");
		System.out.println(text);
	    doc = new Document();
		content = new Field("contents",text, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES);
	    doc.add(content);
	    writer.addDocument(doc);
	    
 	    text = extractTextHtml("http://infomesh.net/2001/swintro");
		System.out.println(text);
	    doc = new Document();
		content = new Field("contents",text, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES);
	    doc.add(content);
	    writer.addDocument(doc);
	    
	    text = extractTextHtml("http://www.w3.org/standards/semanticweb/");
		System.out.println(text);
	    doc = new Document();
		content = new Field("contents",text, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES);
	    doc.add(content);
	    writer.addDocument(doc);
	    */
	    //writer.close();
 	    //ds.testSimilarity();
		TermFreqVector vector = rd.getTermFreqVector(20, "contents");
		
		for (String vecTerm : vector.getTerms()) {
			System.out.println(vecTerm);
		}
		
		System.out.println(rd.document(20).get("contents"));
		System.out.println(rd.document(20).get("title"));    		
		System.out.println(vector.toString());
		
	}	
}
