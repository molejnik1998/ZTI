import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.IOException;
import java.util.*;

public class Relation {
    private final String text;
    private CoreDocument coreDocument;
    private StanfordCoreNLP stanfordCoreNLP;
    private List<CoreLabel> coreLabelList;
    private List<CoreSentence> sentenceList;

    //Pipeline
    public static Properties properties;
    public static String propertiesName = "tokenize, ssplit, pos, lemma, parse, ner, sentiment, relation, kbp";
    public static StanfordCoreNLP stanfordCoreNLPPipeline;
    public String[][] positions = {
            {"CC", "Coordinating conjunction"},
            {"CD", "Cardinal number" },
            {"DT", "Determiner"},
            {"EX", "Existential there" },
            {"FW", "Foreign word" },
            {"IN", "Preposition or subordinating conjunction" },
            {"JJ", "Adjective" },
            {"JJR", "Adjective, comparative" },
            {"JJS", "Adjective, superlative" },
            {"LS", "List item marker" },
            {"MD", "Modal" },
            {"NN", "Noun, singular or mass" },
            {"NNS", "Noun, plural" },
            {"NNP", "Proper noun, singular" },
            {"NNPS", "Proper noun, plural" },
            {"PDT", "Predeterminer" },
            {"POS", "Possessive ending" },
            {"PRP", "Personal pronoun" },
            {"PRP$", "Possessive pronoun" },
            {"RB", "Adverb" },
            {"RBR", "Adverb, comparative" },
            {"RBS", "Adverb, superlative" },
            {"RP", "Particle" },
            {"SYM", "Symbol" },
            {"TO", "to" },
            {"UH", "Interjection" },
            {"VB", "Verb, base form" },
            {"VBD", "Verb, past tense" },
            {"VBG", "Verb, gerund or present participle" },
            {"VBN", "Verb, past participle" },
            {"VBP", "Verb, non -3 rd person singular present" },
            {"VBZ", "Verb, 3 rd person singular present" },
            {"WDT", "Wh -determiner" },
            {"WP", "Wh -pronoun" },
            {"WP$", "Possessive wh - pronoun" },
            {"WRB", "Wh -adverb" }
    };
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public Relation(String textRelation) {
        this.text = textRelation;
        this.stanfordCoreNLP = getPipeline();
        this.coreDocument = new CoreDocument(this.text);
        stanfordCoreNLP.annotate(coreDocument);
        this.sentenceList = coreDocument.sentences();
        this.coreLabelList = coreDocument.tokens();
    }

    public void printRelations() throws IOException {
        System.out.println("-------> Tokenization <-------");
        for(int i=0;i<coreLabelList.size();i++){
            //System.out.println("\t" + coreLabelList.get(i).originalText());
        }

        System.out.println("-------> Sentences and their sentiment <-------");
        List<CoreSentence> sentenceList = coreDocument.sentences();
        for(int i=0;i<sentenceList.size();i++){
            if(sentenceList.get(i).sentiment().equals("Negative") || sentenceList.get(i).sentiment().equals("Very Negative")) {
                System.out.println(ANSI_RED + sentenceList.get(i).sentiment() + ANSI_RESET + "\t" + sentenceList.get(i).toString());
            } else if(sentenceList.get(i).sentiment().equals("Positive")) {
                System.out.println(ANSI_GREEN + sentenceList.get(i).sentiment() + ANSI_RESET + "\t" + sentenceList.get(i).toString());
            } else {
                System.out.println(sentenceList.get(i).sentiment() + "\t" + sentenceList.get(i).toString());
            }
        }

        for(int i=0;i<coreLabelList.size();i++){
            System.out.println(   ANSI_CYAN     + "\n------------------------------------------------------------------\n" + ANSI_RESET
                                + ANSI_YELLOW   + "\t Original text: "              + ANSI_RESET    + coreLabelList.get(i).originalText() + "\n"
                                + ANSI_YELLOW   + "\t Position: "                   + ANSI_RESET    + getPositionDescription(coreLabelList.get(i).get(CoreAnnotations.PartOfSpeechAnnotation.class)) + "\n"
                                + ANSI_YELLOW   + "\t Lemmatisation: "              + ANSI_RESET    + coreLabelList.get(i).lemma() + "\n"
                                + ANSI_YELLOW   + "\t Named entity recognition: "   + ANSI_RESET    + coreLabelList.get(i).get(CoreAnnotations.NamedEntityTagAnnotation.class) + "\n"
                                + ANSI_PURPLE   + "------------------------------------------------------------------\n" + ANSI_RESET  );
        }

         for(int i=0;i<sentenceList.size();i++){
            List<RelationTriple> relations = sentenceList.get(i).relations();
            for(int j=0;j<relations.size();j++){
                System.out.println(relations.get(j));
            }
        }
    }

    public ArrayList<RelationTriple> getRelationsTriplesList() {
        ArrayList<RelationTriple> relationTriples = new ArrayList<>();
        try {
            for (int i=0;i<sentenceList.size();i++) {
                relationTriples.addAll(sentenceList.get(i).relations());
            }
        } catch (Exception exception){

        }
        return relationTriples;
    }
    public static StanfordCoreNLP getPipeline() {
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);
        if(stanfordCoreNLPPipeline == null) {
            stanfordCoreNLPPipeline = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLPPipeline;
    }

    public String getPositionDescription(String positionKey) {
        String val = null;
        for (int i=0;i<positions.length;i++){
            if(positions[i][0].equals(positionKey)){
              val = positions[i][1];
              break;
          }
        }
        return val;
    }
}
