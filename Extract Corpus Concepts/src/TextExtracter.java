import net.htmlparser.jericho.*;
import java.net.*;
import java.util.*;

public class TextExtracter {
	
	private String url;
	private String title;
	private String description;
	private String keywords;
	private String text;
	
	//TextExtracter
	TextExtracter(String siteUrl)
	{
		url = siteUrl;
	}
	
	public String extractTitle(Source source)
	{
		Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
		if (titleElement==null) 
			return null;
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}
	 
	private String getMetaValue(Source source, String key) 
	{
		for (int pos=0; pos<source.length();)
		{
			StartTag startTag=source.getNextStartTag(pos,"name",key,false);
			if (startTag==null) return null;
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}

	
	public void extractText() throws Exception
	{
		if (url.indexOf(':')==-1) url="file:"+url;
			//MicrosoftTagTypes.register();
			PHPTagTypes.register();
			PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
			MasonTagTypes.register();
			Source source = null;
			System.out.println("URL " + url);
			try
			{
				source=new Source(new URL(url));
			}
			catch(Exception e){
				System.out.println("url not found: " + url);
				
			}
		// Call fullSequentialParse manually as most of the source will be parsed.
			source.fullSequentialParse();
			title=extractTitle(source);
			
			if(title != null)
				title = title.replaceAll("[^A-Z a-z \\s]+", "");
			//System.out.println("Title :" + title);
			description=getMetaValue(source,"description");
			keywords=getMetaValue(source,"keywords");
		
			//String sourceUrlString="data/test.html";
			//if (args.length==0)
			 // System.err.println("Using default argument of \""+sourceUrlString+'"');
			//else
			//	sourceUrlString=args[0];
			//if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
			//Source source=new Source(new URL(url));
			String renderedText=source.getRenderer().toString();
			//System.out.println("\nSimple rendering of the HTML document:\n");
			//System.out.println(renderedText);	
			//("/n.*/n *", " ")
			text = renderedText.replaceAll("<.*>*", "");
			text = text.replaceAll("[.*]*", "");
			text = text.replaceAll("-.*-*", "");
			//System.out.println(text);
	}
	public String getTitle()
	{
		//System.out.println(title);
		return title;
	}
	
	public String getText()
	{	
		//System.out.println(text);
		return text;
	}
	public static void main(String[] args){
		TextExtracter webpage = new TextExtracter("http://www.ncdc.noaa.gov/extreme-events");
		try{
			webpage.extractText();
			
			//webpage.extractText();
		System.out.println("Title:****" + webpage.getTitle());
		//	System.out.println("------");
			System.out.println("Text:----" + webpage.getText());
		}
		catch(Exception e){
        	e.printStackTrace();
        }
			

	}
}
