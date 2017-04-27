import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import java.util.ArrayList;

public class Virtuoso {
    public static ArrayList<Artist> queryArtists() {
        ArrayList<Artist> artists = new ArrayList<Artist>();
        String qs = "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                + "prefix : <http://www.grupo1.semanticweb.uniandes.edu.co/curso/arte#> \n"
                + "select ?name ?birthyear ?nationality where {\n"
                + "?resource a :Artist.\n"
                + "?resource :name ?name.\n"
                + "?resource :birthYear ?birthyear.\n"
                + "?resource :nationality ?nationality.\n"
                + "}";

        QueryExecution exec = QueryExecutionFactory.sparqlService("http://172.24.42.79:8890/sparql", QueryFactory.create(qs));

        ResultSet results = exec.execSelect();

        //ResultSetFormatter.out(results);
        while(results.hasNext()) {
            QuerySolution next = results.next();
            String name = next.get("name").toString().split("\\^")[0];
            String nationality = next.get("nationality").toString().split("\\^")[0];
            int birthYear = Integer.parseInt(next.get("birthyear").toString().split("\\^")[0]);
            artists.add(new Artist(name, nationality, birthYear));
        }

        return artists;
    }
    public static void main(String[] args) {
        ArrayList<Artist> artistas = Virtuoso.queryArtists();
        System.out.println(artistas.size());

    }
}
