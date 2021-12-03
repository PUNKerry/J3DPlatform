package com.company.files.obj;

import com.company.*;
import com.company.base.Model;
import com.company.base.Polygon;
import com.company.exceptions.ObjReaderException;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ObjReader {

    private static int maxVertexIndex;
    private static int maxTextureIndex;
    private static int maxNormalIndex;

    public static Model readFromFile(Path fileName) throws Exception {
        String fileContent = Files.readString(fileName);
        return read(fileContent);
    }

    public static Model read(String fileContent) throws Exception {
        maxTextureIndex = -1;
        maxVertexIndex = -1;
        maxNormalIndex = -1;
        Model result = new Model();

        int quantityVertex = 0;
        int quantityTexture = 0;
        int quantityNormal = 0;

        int lineInd = 0;
        Scanner scanner = new Scanner(fileContent);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            ArrayList<String> wordsInLine = new ArrayList<>(Arrays.asList(line.split("\\s+")));
            if (wordsInLine.isEmpty())
                continue;

            final String token = wordsInLine.get(0);
            wordsInLine.remove(0);
            if (Objects.equals(token, ObjTokens.TEXTURE) || Objects.equals(token, ObjTokens.NORMAL) || Objects.equals(token, ObjTokens.VERTEX)){
                if (wordsInLine.size() > 3) {
                    throw new ObjReaderException("Too many numbers.", lineInd);
                }
            }
            ++lineInd;
            switch (token) {
                case ObjTokens.VERTEX -> {
                    result.addNewVertex(parseVertex(wordsInLine, lineInd));
                    quantityVertex++;
                }
                case ObjTokens.TEXTURE -> {
                    result.addNewTextureVertex(parseTextureVertex(wordsInLine, lineInd));
                    quantityTexture++;
                }
                case ObjTokens.NORMAL -> {
                    result.addNewNormal(parseNormal(wordsInLine, lineInd));
                    quantityNormal++;
                }
                case ObjTokens.POLYGON -> result.addNewPolygon(parseFace(
                        wordsInLine,
                        lineInd));
                default -> {}
            }
        }
        if(quantityVertex != quantityNormal && quantityNormal != 0){
            throw new ObjReaderException("the quantity of vertices and normals does not match");
        }
        if(maxVertexIndex > quantityVertex) {
            throw new ObjReaderException("A vertex index is specified that exceeds the number of vertices");
        }
        if(maxNormalIndex > quantityNormal) {
            throw new ObjReaderException("A normal index is specified that exceeds the number of vertices");
        }
        if(maxTextureIndex > quantityTexture) {
            throw new ObjReaderException("A texture index is specified that exceeds the number of vertices");
        }

        return result;
    }

    public static Vector3f parseVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector3f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1)),
                    Float.parseFloat(wordsInLineWithoutToken.get(2)));

        } catch(NumberFormatException e) {
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few vertex arguments.", lineInd);
        }
    }

    public static Vector2f parseTextureVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector2f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1)));

        } catch(NumberFormatException e) {
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few texture vertex arguments.", lineInd);
        }
    }

    public static Vector3f parseNormal(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector3f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1)),
                    Float.parseFloat(wordsInLineWithoutToken.get(2)));

        } catch(NumberFormatException e) {
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few normal arguments.", lineInd);
        }
    }

    public static Polygon parseFace(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        Polygon result = new Polygon();
        for (String s : wordsInLineWithoutToken) {
            parseFaceWord(s, lineInd, result);
        }
        return result;
    }

    public static void parseFaceWord(
            String wordInLine,
            int lineInd,
            Polygon result) {
        try {
            String[] wordIndices = wordInLine.split("/");
            maxVertexIndex = Math.max(maxVertexIndex, Integer.parseInt(wordIndices[0]) - 1);
            switch (wordIndices.length) {
                case 1 -> result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, -1, -1);
                case 2 -> {
                    result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, Integer.parseInt(wordIndices[1]) - 1, -1);
                }
                case 3 -> {
                    if (wordIndices[1].equals("")) {
                        result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, -1, Integer.parseInt(wordIndices[2]) - 1);
                    }else {
                        result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, Integer.parseInt(wordIndices[1]) - 1, Integer.parseInt(wordIndices[2]) - 1);
                        maxTextureIndex = Math.max(maxTextureIndex, Integer.parseInt(wordIndices[1]) - 1);
                    }
                    maxNormalIndex = Math.max(maxNormalIndex, Integer.parseInt(wordIndices[2]) - 1);
                }
                default -> throw new ObjReaderException("Invalid element size.", lineInd);
            }

        } catch(NumberFormatException e) {
            throw new ObjReaderException("Failed to parse int value.", lineInd);
        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few arguments.", lineInd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}