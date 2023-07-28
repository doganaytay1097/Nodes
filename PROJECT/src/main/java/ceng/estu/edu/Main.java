package ceng.estu.edu;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import java.io.*;
import java.util.*;

class ProgramOptions{
    @Option(name = "-i", usage = "Input file's name", required = true)
    String inputFile;
}

public class Main {

    public static void main(String[] args) throws CmdLineException {
        ArrayList<Node> allNodes= new ArrayList<>();
        Set<String> nodesSet= new HashSet<>();
        Node right = null;

        ProgramOptions options= new ProgramOptions();
        CmdLineParser parser= new CmdLineParser(options);
        parser.parseArgument(args);

        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    options.inputFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String line:lines){
            if(!line.contains("->")){
                nodesSet.add(line);
            }
            else{
                String[] splitLines = line.split("->");
                String[] requests= splitLines[0].split(",");
                for(String req:requests){
                    nodesSet.add(req);
                }
                nodesSet.add(splitLines[1]);
            }
        }
        for(String nodeName:nodesSet){
            allNodes.add(new Node(nodeName));
        }
        for(String line:lines) {
            if (line.contains("->")) {
                String[] splitLines = line.split("->");
                String[] requests = splitLines[0].split(",");
                for (Node rightNode : allNodes) {
                    if (Objects.equals(rightNode.name, splitLines[1])) {
                        right = rightNode;
                        break;
                    }
                }
                for (Node n : allNodes) {
                    if (splitLines[0].contains(n.name)) {
                        right.predecessors.add(n);
                        n.successors.add(right);
                    }
                }
            }
        }

        for(Node node: allNodes){
            node.numPredecessors=node.predecessors.size();
            node.start();
        }
    }
}