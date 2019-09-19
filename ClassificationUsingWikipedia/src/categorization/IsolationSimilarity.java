package categorization;

import org.apache.lucene.search.DefaultSimilarity;

class IsolationSimilarity extends DefaultSimilarity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IsolationSimilarity(){}

	public float idf(int docFreq, int numDocs) {
		return(float)1.0;
	}

	/*public float coord(int overlap, int maxOverlap) {
		return 1.0f;
	}

	public float lengthNorm(String fieldName, int numTerms) {
		return 1.0f;
	}*/
}