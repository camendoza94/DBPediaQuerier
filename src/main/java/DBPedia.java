import org.apache.jena.query.*;

import java.io.*;
import java.util.ArrayList;

public class DBPedia {
    private static final String URI = "<http://www.grupo1.semanticweb.uniandes.edu.co/curso/arte#";

    private static void queryArtists(ArrayList<String[]> linking) {
        String[][] data = {{"dbo:field", "field"}, {"dbo:movement", "movement"}, {"dbo:birthPlace", "birthPlace"}, {"dbo:deathPlace", "deathPlace"}, {"dbo:nationality", "nationality"}};
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
                printLinking(uri[0], ont, link, true);
            }
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
                printLinking(link, ont, uri[0], false);
            }
        }
    }

    private static void printLinking(String instance, String relation, String link, boolean dataFirst) {
        try (FileWriter fw = new FileWriter("linkageRelacion.ttl", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            if (dataFirst)
                out.println(instance + " " + URI + relation + "> <" + link + "> .");
            else
                out.println("<" + instance + "> " + URI + relation + "> " + link + " .");
        } catch (IOException ignored) {
        }
    }

    private static ArrayList<String[]> readOutput() {
        ArrayList<String[]> linking = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("linkageIdentidad.ttl"))) {
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                String[] uris = new String[2];
                String ont = line.split("<http://www.w3.org/2002/07/owl#sameAs>")[1].trim();
                uris[1] = ont.substring(0, ont.length() - 1);
                uris[0] = line.split("<http://www.w3.org/2002/07/owl#sameAs>")[0].trim();
                linking.add(uris);
                line = br.readLine();
            }
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