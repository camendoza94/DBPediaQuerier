import org.apache.jena.query.*;

import java.util.ArrayList;

public class DBPedia {
    public static void queryArtists(ArrayList<Artist> artistsVirtuoso) {
        for (Artist artist: artistsVirtuoso) {
            String qs = "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX dbo:     <http://dbpedia.org/ontology/> \n"
                    + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                    + "select distinct ?name ?birthYear ?nationality where {\n"
                    + "  ?resource rdfs:label '" + artist.getName() + "' @en.\n"
                    + "  ?resource dbo:nationality '" + artist.getNacionality() + "'.\n"
                    + "  ?resource dbo:birthDate '" + artist.getBirthYear() + "'.\n"
                    + "  OPTIONAL {\n"
                    + "  ?resource dbo:deathDate ?d. \n"
                    + "}\n"
                    //+ "  BIND(year(xsd:date(?b)) as ?birth) .\n"
                    + "}";

            QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", QueryFactory.create(qs));

            ResultSet results = exec.execSelect();

            while (results.hasNext()) {
                QuerySolution next = results.next();
                String name = next.get("name").toString().split("\\^")[0];
                String nationality = next.get("nationality").toString().split("\\^")[0];
                int birthYear = Integer.parseInt(next.get("birthyear").toString().split("\\^")[0]);
                Artist dbpediaArtist = new Artist(name, nationality, birthYear);
            }

            ResultSetFormatter.out(results);
        }
    }

    /*public static void main(String[] args) {
        DBPedia main = new DBPedia();
    }*/
}