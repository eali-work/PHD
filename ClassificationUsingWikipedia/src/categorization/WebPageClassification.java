package categorization;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;

/**
 * Servlet implementation class WebPageClassification
 */
public class WebPageClassification extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public WebPageClassification() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getParameter("url");
		
		TextExtraction webpage = new TextExtraction();
		try{
			webpage.extractText(url);
			String stopPath = getServletContext().getRealPath("/");
			System.out.println(stopPath);
			TermFrequencyCalculation freq = new TermFrequencyCalculation(stopPath + "/WEB-INF/stopwords.txt");
			//TermFrequencyCalculation freq = new TermFrequencyCalculation("D:/Users/tejas/workspace/ClassificationUsingWikipedia/stopwords.txt");
			String keywords = freq.calculateTermFrequency(webpage.getText(), webpage.getKeywords(), webpage.getTitle());

			String page = WikiIndexSearcher.searchWiki(keywords);
			
			DomainConcept dc = new DomainConcept((Wikipedia)getServletContext().getAttribute("wiki"));
			String category = dc.rankPageCategory(page);
			
			System.out.println(page + " : " + category);
			
			request.setAttribute("wikiPage", page);
			request.setAttribute("wikiCategory", category);
			
			RequestDispatcher view = request.getRequestDispatcher("index.jsp");
			view.forward(request, response);

			
		}
		catch(Exception e){
        	e.printStackTrace();
        }
		
	}

}
