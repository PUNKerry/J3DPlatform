package com.company;

import java.util.*;

public class ObjReader {

    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static Model read(String fileContent) throws Exception {
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
            if (Objects.equals(token, OBJ_TEXTURE_TOKEN) || Objects.equals(token, OBJ_NORMAL_TOKEN) || Objects.equals(token, OBJ_VERTEX_TOKEN)){
                if (wordsInLine.size() > 3) {
                    throw new ObjReaderException("Too many numbers.", lineInd);
                }
            }
            ++lineInd;
            switch (token) {
                case OBJ_VERTEX_TOKEN -> {
                    result.addNewVertex(parseVertex(wordsInLine, lineInd));
                    quantityVertex++;
                }
                case OBJ_TEXTURE_TOKEN -> {
                    result.addNewTextureVertex(parseTextureVertex(wordsInLine, lineInd));
                    quantityTexture++;
                }
                case OBJ_NORMAL_TOKEN -> {
                    result.addNewNormal(parseNormal(wordsInLine, lineInd));
                    quantityNormal++;
                }
                case OBJ_FACE_TOKEN -> result.addNewPolygon(parseFace(
                        wordsInLine,
                        lineInd,
                        quantityTexture,
                        quantityNormal,
                        quantityVertex));
                default -> {}
            }
        }
        if(quantityVertex != quantityNormal && quantityNormal != 0){
            throw new ObjReaderException("the quantity of vertices and normals does not match");
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

    public static Polygon parseFace(
            final ArrayList<String> wordsInLineWithoutToken,
            int lineInd,
            int quantityTexture,
            int quantityNormal,
            int quantityVertex) {
        Polygon result = new Polygon();

        for (String s : wordsInLineWithoutToken) {
            parseFaceWord(s, lineInd,quantityTexture, quantityNormal, quantityVertex, result);
        }

        return result;
    }

    public static void parseFaceWord(
            String wordInLine,
            int lineInd,
            int quantityTexture,
            int quantityNormal,
            int quantityVertex,
            Polygon result) {
        try {
            String[] wordIndices = wordInLine.split("/");
            if(Integer.parseInt(wordIndices[0]) - 1 > quantityVertex){
                throw new ObjReaderException("the index of the vertex exceeding their number is specified",lineInd);
            }
            switch (wordIndices.length) {
                case 1 -> result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, -1, -1);
                case 2 -> {
                    if(quantityTexture == 0){
                        throw new ObjReaderException("no texture.",lineInd);
                    }
                    result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, Integer.parseInt(wordIndices[1]) - 1, -1);
                    if(Integer.parseInt(wordIndices[1]) - 1 > quantityTexture){
                        throw new ObjReaderException("the index of the T vertex exceeding their number is specified",lineInd);
                    }
                }
                case 3 -> {
                    if (wordIndices[1].equals("")) {
                        result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, -1, Integer.parseInt(wordIndices[2]) - 1);
                    }else {
                        result.addNewVertex(Integer.parseInt(wordIndices[0]) - 1, Integer.parseInt(wordIndices[1]) - 1, Integer.parseInt(wordIndices[2]) - 1);
                    }
                    if(Integer.parseInt(wordIndices[1]) - 1 > quantityTexture)
                        throw new ObjReaderException("the index of the T vertex exceeding their number is specified", lineInd);
                    if(Integer.parseInt(wordIndices[2]) - 1 > quantityNormal){
                        throw new ObjReaderException("the index of the N vertex exceeding their number is specified",lineInd);
                    }
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

