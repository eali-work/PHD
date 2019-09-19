package categorization;

import net.htmlparser.jericho.*;
import java.net.*;

/**
 * @purpose:     To extract text from web page and title and keywords from the META tag of the web page
 * @requirement: jericho html parser
 * @author:      Tejas Nomulwar
 */

public final class TextExtraction {
	private String title;
	private String description;
	private String keywords;
	private String text;
	
	public String getDescription(){
		return description;
	}
	
	public String getKeywords(){
		return keywords;
	}
	
	public String getTitle(){
		return title;
	}

	public String getText(){
		return text;
	}
	
	public void extractText(String sourceUrlString) throws Exception {
		if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
			MicrosoftTagTypes.register();
			PHPTagTypes.register();
			PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
			MasonTagTypes.register();
			Source source = null;
			
			try{
				source=new Source(new URL(sourceUrlString));
			}
			catch(Exception e){
				System.out.println("url not found: " + sourceUrlString);
				return;
			}
		// Call fullSequentialParse manually as most of the source will be parsed.
			source.fullSequentialParse();

			title=getTitle(source);
		
			if(title != null)
				title = title.replaceAll("[^A-Z a-z \\s]+", "");
		
			description=getMetaValue(source,"description");
			keywords=getMetaValue(source,"keywords");
		
			text = source.getTextExtractor().setIncludeAttributes(true).toString();
			text = text.replaceAll("[^A-Z a-z \\s]+", " ");
	}

	private static String getTitle(Source source) {
		Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
		if (titleElement==null) return null;
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}
	
	private String getMetaValue(Source source, String key) {
		for (int pos=0; pos<source.length();) {
			StartTag startTag=source.getNextStartTag(pos,"name",key,false);
	
			if (startTag==null) return null;
			
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}


/*	public static void main(String[] args){
		TextExtraction webpage = new TextExtraction();
		try{
			webpage.extractText("http://www.huawei.com/publications/view.do?id=6192&cid=11570&pid=10664");
			TermFrequencyCalculation freq = new TermFrequencyCalculation("stopwords.txt");
			String keywords = freq.calculateTermFrequency(webpage.getText(), webpage.getKeywords(), webpage.getTitle());

			WikiIndexSearcher search = new WikiIndexSearcher();
			String page = search.searchWiki(keywords);
			
			DomainConcept dc = new DomainConcept("localhost", "wiki_jwpl","root","mysql");
			String category = dc.rankPageCategory(page);
			
			System.out.println(page + " : " + category);
			
		}
		catch(Exception e){
        	e.printStackTrace();
        }
	}*/
}


