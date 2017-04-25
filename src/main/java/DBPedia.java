import org.apache.jena.query.*;

public class DBPedia {
    DBPedia() {
        String qs = "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/> \n"
                + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                + "select distinct ?resource ?birth ?death ?nacionality where {\n"
                + "  ?resource rdfs:label 'Fernando Botero'@en.\n"
                + "  ?resource dbo:nationality ?nacionality.\n"
                + "  ?resource dbo:birthDate ?b.\n"
                + "  OPTIONAL {\n"
                + "  ?resource dbo:deathDate ?d. \n"
                + "}\n"
                + "  BIND(year(xsd:date(?b)) as ?birth) .\n"
                + "}";

        QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", QueryFactory.create(qs));

        ResultSet results = exec.execSelect();

//        while (results.hasNext()) {
//
//            System.out.println(results.next().get("").toString());
//        }

        ResultSetFormatter.out(results);
    }
    public static void main(String[] args) {
       DBPedia main = new DBPedia();
    }
}