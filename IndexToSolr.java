/**
 * 
 */
package indexDocs;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */
public class IndexToSolr {

	/**
	 * 
	 */
	public IndexToSolr() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public static void main(String[] args) throws SolrServerException, IOException {
		
		
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/#/Docs1").build();

		
		File file = new File("C:\\FAA2\\data\\text1.txt");
		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("id", file.getCanonicalPath());
		doc.addField("OriginalFileAddress", file.getAbsolutePath());
		doc.addField("Content", "get from Tika");
		
		UpdateResponse updateResponse = client.add("Docs1", doc);
		// Indexed documents must be committed
		client.commit("Docs1");
		
		String queryString = "*.*";
		SolrQuery query = new SolrQuery(queryString);
		
		QueryResponse response = client.query(query);
		SolrDocumentList list = response.getResults();
		

		
		
		    // Crude way to get known meta-data fields. Also
		    // possible to write a simple loop to examine all the
		    // metadata returned and selectively index it and/or just
		    // get a list of them.
		    // One can also use the LucidWorks field mapping to
		    // accomplish much the same thing.
		// String author = metadata.get("Author");
//		 if (author != null) {
//		   doc.addField("author", author);
//		 }
//		 doc.addField("text", textHandler.toString());
//		 _docs.add(doc);
		
//		SolrServer server = new HttpSolrServer("");
//		
//		
//		final SolrClient client = getSolrClient();
//
//		final SolrInputDocument doc = new SolrInputDocument();
//		doc.addField("id", UUID.randomUUID().toString());
//		doc.addField("name", "Amazon Kindle Paperwhite");

//		final UpdateResponse updateResponse = client.add("techproducts", doc);
//		// Indexed documents must be committed
//		client.commit("techproducts");

	}

}
