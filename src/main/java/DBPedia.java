import org.apache.jena.query.*;

import java.io.*;
import java.util.ArrayList;

public class DBPedia {
    private static final String URI = "<http://www.grupo1.semanticweb.uniandes.edu.co/curso/arte#";

    private static void queryArtists(ArrayList<String[]> linking) {
        String[][] data = {{"dbo:field", "field"}, {"dbo:movement", "movement"}, {"dbo:birthDate", "birthDate"}, {"dbo:birthPlace", "birthPlace"}, {"dbo:deathDate", "deathDate"}, {"dbo:deathPlace", "deathPlace"}, {"dbo:birthName", "birtName"}, {"dbo:nationality", "nationality"}};
        for (String[] pair : data) {
            query(linking, pair[0], pair[1]);
        }
        String[][] data2 = {{"dbo:influenced", "influenced"}, {"dbo:influencedBy", "influencedBy"}, {"dbo:author", "author"}};
        for (String[] pair : data2) {
            query2(linking, pair[0], pair[1]);
        }
    }

    private static void query(ArrayList<String[]> linking, String dataset, String ont) {
        for (String[] uri : linking) {
            String qs = "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX dbo:     <http://dbpedia.org/ontology/> \n"
                    + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                    + "select * where {\n"
                    + "?uri " + dataset + " ?" + ont + ".\n"
                    + "FILTER (?uri = " + uri[1] + ")\n"
                    + "}";

            QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", QueryFactory.create(qs));

            ResultSet results = exec.execSelect();

            while (results.hasNext()) {
                QuerySolution next = results.next();
                String link = next.get(ont).toString().split("\\^")[0];
                printLinking(uri[0], ont, link);
            }

            ResultSetFormatter.out(results);
        }
    }

    private static void query2(ArrayList<String[]> linking, String dataset, String ont) {
        for (String[] uri : linking) {
            String qs = "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX dbo:     <http://dbpedia.org/ontology/> \n"
                    + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                    + "select * where {\n"
                    + "?" + ont + " " + dataset + " ?uri.\n"
                    + "FILTER (?uri = " + uri[1] + ")\n"
                    + "}";

            QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", QueryFactory.create(qs));

            ResultSet results = exec.execSelect();

            while (results.hasNext()) {
                QuerySolution next = results.next();
                String link = next.get(ont).toString().split("\\^")[0];
                printLinking(link, ont, uri[0]);
            }

            ResultSetFormatter.out(results);
        }
    }

    private static void printLinking(String instance, String relation, String link) {
        try (FileWriter fw = new FileWriter("output", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(instance + " " + URI + relation + "> " + link + " .\n");
        } catch (IOException e) {
        }
    }

    private static ArrayList<String[]> readOutput() {
        ArrayList<String[]> linking = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("output"))) {
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                String[] uris = new String[2];
                uris[1] = line.split("<http://www.w3.org/2002/07/owl#sameAs>")[1].trim().split(".")[0].trim();
                uris[0] = line.split("<http://www.w3.org/2002/07/owl#sameAs>")[0].trim();
                linking.add(uris);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linking;
    }

    public static void main(String[] args) {
        // read linking results from output file
        DBPedia.queryArtists(DBPedia.readOutput());
    }
}