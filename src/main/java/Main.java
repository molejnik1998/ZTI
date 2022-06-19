import edu.stanford.nlp.ie.util.RelationTriple;
import lombok.SneakyThrows;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import java.util.ArrayList;
import java.util.Scanner;

//https://www.rosette.com/capability/relationship-extractor/#try-the-demo

public class Main {
    static String httpDBP = "http://dbpedia.org/sparql";
    static String timeOutValue = "1000";
    public static String flaga = " ";
    static int pom = 5;
    @SneakyThrows
    public static void main(String[] args) {

        System.out.println("Hello");

            //if(flaga.equals(' ')) {
                Scanner scanText = new Scanner(System.in);
                System.out.println("Enter your text:");
                String dataEntered = scanText.nextLine();

                Relation relation = new Relation(dataEntered);
                ArrayList<RelationTriple> relationsList = relation.getRelationsTriplesList();
                relation.printRelations();


                System.out.println("\n\n-------> Dbpedia <-------");
                if (relationsList.size() == 0) {
                    System.out.println("It's empty!");
                }
                for (int i = 0; i < relationsList.size(); i++) {
                    String object = relationsList.get(i).objectLink().replaceAll(" ", "_");
                    String subject = relationsList.get(i).subjectLink().replaceAll(" ", "_");

                    //CREATE OBJECT MODEL
                    String queryStrObject = "SELECT ?prop ?place WHERE { <http://dbpedia.org/resource/" + object + "> ?prop ?place .}";
                    Query queryObject = QueryFactory.create(queryStrObject);
                    QueryExecution queryExecObject = QueryExecutionFactory.sparqlService(httpDBP, queryObject);
                    ((QueryEngineHTTP) queryExecObject).addParam("timeout", timeOutValue);
                    Model modelObjectDBP = queryExecObject.execConstruct();

                    //CREATE SUBJECT MODEL
                    String queryStrSubject = "SELECT ?prop ?place WHERE { <http://dbpedia.org/resource/" + subject + "> ?prop ?place .}";
                    Query querySubject = QueryFactory.create(queryStrSubject);
                    QueryExecution queryExecSubject = QueryExecutionFactory.sparqlService(httpDBP, querySubject);
                    ((QueryEngineHTTP) queryExecSubject).addParam("timeout", timeOutValue);
                    Model modelSubjectDBP = queryExecSubject.execConstruct();

                    //SHOW DBPedia
                    System.out.println(modelObjectDBP);
                    System.out.println(modelSubjectDBP);

                }
                //Scanner decision = new Scanner(System.in);
                //System.out.println("If you want to enter another text, click 1 and click enter.\nTo finish, type 2 and click enter");
                //String valueDecision = decision.nextLine();
                //System.out.println(valueDecision);
                //if (valueDecision.equals(2)) {
                    //flaga = String.valueOf('X');
                    //System.out.println(flaga);
                  //  break;
                //}
            //}

    }
}