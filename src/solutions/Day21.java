package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

public class Day21 extends DayTemplate {

    List<Coordinate> numPad = new ArrayList<>();
    List<Coordinate> dirPad = new ArrayList<>();
    Map<Character, Integer> dirMap;
    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        String[] inputs = new String[5];
        inputs[0] = in.nextLine();
        inputs[1] = in.nextLine();
        inputs[2] = in.nextLine();
        inputs[3] = in.nextLine();
        inputs[4] = in.nextLine();
        numPad.add(new Coordinate(3,1));
        numPad.add(new Coordinate(2,0));
        numPad.add(new Coordinate(2,1));
        numPad.add(new Coordinate(2,2));
        numPad.add(new Coordinate(1,0));
        numPad.add(new Coordinate(1,1));
        numPad.add(new Coordinate(1,2));
        numPad.add(new Coordinate(0,0));
        numPad.add(new Coordinate(0,1));
        numPad.add(new Coordinate(0,2));
        numPad.add(new Coordinate(3,2));
        dirMap = Map.of('<', 0, 'v', 1, '>', 2, '^',3,'A',4);
        dirPad.add(new Coordinate(1,0));
        dirPad.add(new Coordinate(1,1));
        dirPad.add(new Coordinate(1,2));
        dirPad.add(new Coordinate(0,1));
        dirPad.add(new Coordinate(0,2));

        for(String input: inputs){
            long a = shortest(input, part1);
            long b = Integer.parseInt(input.substring(0, input.length() - 1));
            answer +=  a * b;
        }
        return answer + "";
    }

    private long shortest(String input, boolean part1){
        Set<String> currentLayer = shortestNumeric(input);
        for(int i = 0; i < 1; i++){
            Set<String> newLayer = new HashSet<>();
            for(String s: currentLayer){
                newLayer.addAll(shortestDirectional(s));
            }
            currentLayer = newLayer;
        }
        long result = -1;
        for(String s: currentLayer){
            Map<String, Long> parts = iterate(s, (part1?1:24));
            long interimResult = 0;
            for(String key: parts.keySet()){
                interimResult += parts.get(key) * (key.length() + 1);
            }
            if(result == -1 || result > interimResult){
                result = interimResult;
            }
        }
        return result;

    }

    private Map<String, Long> iterate(String s, int iterations){
        Map <String, Long> map = convert(s);
        while(iterations-- > 0){
            map = iterateOne(map);
        }
        return map;
    }

    private Map<String, Long> convert(String s){
        Map<String, Long> result = new HashMap<>();
        String[] parts = s.split("A");
        for(String part: parts){
            result.put(part, result.getOrDefault(part, 0L) + 1);
        }
        return result;
    }

    private Map<String, Long> iterateOne(Map<String, Long> map){
        Map<String, Long> result = new HashMap<>();
        for(String s: map.keySet()) {
            long val = map.get(s);
            long first;
            long second;
            long third;
            long fourth;
            switch (s) {
                case "":
                    first = result.getOrDefault("", 0L);
                    result.put("", first + val);
                    break;
                case ">":
                    first = result.getOrDefault("v", 0L);
                    result.put("v", first + val);
                    second = result.getOrDefault("^", 0L);
                    result.put("^", second + val);
                    break;
                case ">>":
                    first = result.getOrDefault("v", 0L);
                    result.put("v", first + val);
                    second = result.getOrDefault("", 0L);
                    result.put("", second + val);
                    third = result.getOrDefault("^", 0L);
                    result.put("^", third + val);
                    break;
                case "<":
                    first = result.getOrDefault("v<<", 0L);
                    result.put("v<<", first + val);
                    second = result.getOrDefault(">>^", 0L);
                    result.put(">>^", second + val);
                    break;
                case "<<":
                    first = result.getOrDefault("v<<", 0L);
                    result.put("v<<", first + val);
                    second = result.getOrDefault("", 0L);
                    result.put("", second + val);
                    third = result.getOrDefault(">>^", 0L);
                    result.put(">>^", third + val);
                    break;
                case "^":
                    first = result.getOrDefault("<", 0L);
                    result.put("<", first + val);
                    second = result.getOrDefault(">", 0L);
                    result.put(">", second + val);
                    break;
                case "v":
                    first = result.getOrDefault("<v", 0L);
                    result.put("<v", first + val);
                    second = result.getOrDefault("^>", 0L);
                    result.put("^>", second + val);
                    break;
                case "v<<":
                    //v<A<AA>>^A
                    first = result.getOrDefault("<v", 0L);
                    result.put("<v", first + val);
                    second = result.getOrDefault("<", 0L);
                    result.put("<", second + val);
                    third = result.getOrDefault("", 0L);
                    result.put("", third + val);
                    fourth = result.getOrDefault(">>^", 0L);
                    result.put(">>^", fourth + val);
                    break;
                case ">>^":
                    //vAA<^A>A
                    first = result.getOrDefault("v", 0L);
                    result.put("v", first + val);
                    second = result.getOrDefault("", 0L);
                    result.put("", second + val);
                    third = result.getOrDefault("<^", 0L);
                    result.put("<^", third + val);
                    fourth = result.getOrDefault(">", 0L);
                    result.put(">", fourth + val);
                    break;
                case "v>":
                    //v<A>A^A
                    first = result.getOrDefault("<v", 0L);
                    result.put("<v", first + val);
                    second = result.getOrDefault(">", 0L);
                    result.put(">", second + val);
                    third = result.getOrDefault("^", 0L);
                    result.put("^", third + val);
                    break;
                case "^>":
                    //<A>vA^A
                    first = result.getOrDefault("<", 0L);
                    result.put("<", first + val); //2 things 6 char
                    second = result.getOrDefault("v>", 0L);
                    result.put("v>", second + val); //3 things, 4 char
                    third = result.getOrDefault("^", 0L);
                    result.put("^", third + val); //2 things, 2 char
                    break;
                case ">^":
                    //vA<^A>A
                    first = result.getOrDefault("v", 0L);
                    result.put("v", first + val); //2 things 4 char
                    second = result.getOrDefault("<^", 0L);
                    result.put("<^", second + val); //3 things 6 char
                    third = result.getOrDefault(">", 0L);
                    result.put(">", third + val); //2 things 2 char
                    break;
                case "v<":
                    first = result.getOrDefault("<v", 0L);
                    result.put("<v", first + val);
                    second = result.getOrDefault("<", 0L);
                    result.put("<", second + val);
                    third = result.getOrDefault(">>^", 0L);
                    result.put(">>^", third + val);
                    break;
                case "<v":
                    //v<<A>A>^A
                    first = result.getOrDefault("v<<", 0L);
                    result.put("v<<", first + val);
                    second = result.getOrDefault(">", 0L);
                    result.put(">", second + val);
                    third = result.getOrDefault("^>", 0L);
                    result.put("^>", third + val);
                    break;
                case "<^":
                    //v<<A>^A>A
                    first = result.getOrDefault("v<<", 0L);
                    result.put("v<<", first + val);
                    second = result.getOrDefault(">^", 0L);
                    result.put(">^", second + val);
                    third = result.getOrDefault(">", 0L);
                    result.put(">", third + val);
                    break;
                default:
                    System.out.println(s);
            }
        }

        return result;
    }

    private Set<String> shortestNumeric(String input){
        int size = -1;
        Set<String> paths = new HashSet<>();
        paths.add("");
        Set<String> newPaths = new HashSet<>();
        for(String path: paths){
            for(String addition: shortestNumericHelper(numPad.get(10), numPad.get(input.charAt(0) - '0'))){
                newPaths.add(path + addition + "A");
            }
        }
        paths= newPaths;

        for(int i = 0; i < input.length() - 1; i++){
            int beginning = (input.charAt(i) == 'A')?10:(input.charAt(i) - '0');
            int end = (input.charAt(i + 1) == 'A')?10:(input.charAt(i + 1) - '0');
            newPaths = new HashSet<>();
            for(String path: paths){
                for(String addition: shortestNumericHelper(numPad.get(beginning), numPad.get(end))){
                    newPaths.add(path + addition + "A");
                }
            }
            paths= newPaths;
        }
        for(String path: paths){
            if(size == -1 || path.length() < size){
                size = path.length();
            }
        }

        Set<String> result = new HashSet<>();
        for(String path: paths){
            if(size == path.length()){
                result.add(path);
            }
        }

        return result;
    }

    private Set<String> shortestNumericHelper(Coordinate a, Coordinate b){
        Set<String> results = new HashSet<>();
        if(a.x == 3 && a.y == 1 && b.x == 2 && b.y == 0){
            results.add("^<");
            return results;
        }
        if(a.x == 2 && a.y == 0 && b.x == 3 && b.y == 1){
            results.add(">v");
            return results;
        }
        if(a.x == b.x){
            if(a.y == b.y){
                results.add("");
                return results;
            }
            if(a.y > b.y){
                String possibility = "<";
                for(String remaining: shortestNumericHelper(new Coordinate(a.x, a.y - 1), b)){
                    results.add(possibility + remaining);
                }
                return results;
            }
            if(a.y < b.y){
                String possibility = ">";
                for(String remaining: shortestNumericHelper(new Coordinate(a.x, a.y + 1), b)){
                    results.add(possibility + remaining);
                }
                return results;
            }
        }
        if(a.y == b.y){
            if(a.x > b.x){
                String possibility = "^";
                for(String remaining: shortestNumericHelper(new Coordinate(a.x - 1, a.y), b)){
                    results.add(possibility + remaining);
                }
                return results;
            }
            if(a.x < b.x){
                String possibility = "v";
                for(String remaining: shortestNumericHelper(new Coordinate(a.x + 1, a.y), b)){
                    results.add(possibility + remaining);
                }
                return results;
            }
        }
        if(a.x > b.x && a.y > b.y){
            String possibility1 = "^";
            for(String remaining: shortestNumericHelper(new Coordinate(a.x - 1, a.y), b)){
                results.add(possibility1 + remaining);
            }
            if(!(a.x == 3 ) || !(a.y == 1)){
                String possibility2 = "<";
                for(String remaining: shortestNumericHelper(new Coordinate(a.x, a.y - 1), b)){
                    results.add(possibility2 + remaining);
                }
            }
        }
        if(a.x < b.x && a.y > b.y){
            String possibility1 = "v";
            for(String remaining: shortestNumericHelper(new Coordinate(a.x + 1, a.y), b)){
                results.add(possibility1 + remaining);
            }
            String possibility2 = "<";
            for(String remaining: shortestNumericHelper(new Coordinate(a.x, a.y - 1), b)){
                results.add(possibility2 + remaining);
            }
        }
        if(a.x > b.x && a.y < b.y){
            String possibility1 = "^";
            for(String remaining: shortestNumericHelper(new Coordinate(a.x - 1, a.y), b)){
                results.add(possibility1 + remaining);
            }
            String possibility2 = ">";
            for(String remaining: shortestNumericHelper(new Coordinate(a.x, a.y + 1), b)){
                results.add(possibility2 + remaining);
            }
        }
        if(a.x < b.x && a.y < b.y){
            if(!(a.x == 2 ) || !(a.y == 0)) {
                String possibility1 = "v";
                for (String remaining : shortestNumericHelper(new Coordinate(a.x + 1, a.y), b)) {
                    results.add(possibility1 + remaining);
                }
            }
            String possibility2 = ">";
            for(String remaining: shortestNumericHelper(new Coordinate(a.x, a.y + 1), b)){
                results.add(possibility2 + remaining);
            }
        }


        return results;
    }

    private Set<String> shortestDirectional(String input){
        Set<String> paths = new HashSet<>();
        paths.add("");
        Set<String> newPaths = new HashSet<>();
        for(String path: paths){
            for(String addition: shortestDirectionalHelper(dirPad.get(dirMap.get('A')), dirPad.get(dirMap.get(input.charAt(0))))){
                newPaths.add(path + addition + "A");
            }
        }
        paths = newPaths;

        for(int i = 0; i < input.length() - 1; i++){
            int beginning = dirMap.get(input.charAt(i));
            int end = dirMap.get(input.charAt(i + 1));
            newPaths = new HashSet<>();
            for(String path: paths){
                for(String addition: shortestDirectionalHelper(dirPad.get(beginning), dirPad.get(end))){
                    newPaths.add(path + addition + "A");
                }
            }
            paths= newPaths;
        }
        int size = -1;
        for(String path: paths){
            if(size == -1 || path.length() < size){
                size = path.length();
            }
        }

        Set<String> result = new HashSet<>();
        for(String path: paths){
            if(size == path.length()){
                result.add(path);
            }
        }

        return result;
    }

    private Set<String> shortestDirectionalHelper(Coordinate a, Coordinate b){
        Set<String> results = new HashSet<>();
        if(a.x == 1 && a.y == 0 && b.x == 0 && b.y == 1){
            results.add(">^");
            return results;
        }
        if(a.x == 0 && a.y == 1 && b.x == 1 && b.y == 0){
            results.add("v<");
            return results;
        }
        if(a.x == 0 && a.y == 2 && b.x == 1 && b.y == 0){
            results.add("v<<");
            return results;
        }
        if(a.x == 0 && a.y == 2 && b.x == 1 && b.y == 1){
            results.add("<v");
            return results;
        }
        if(a.x == 1 && a.y == 2 && b.x == 0 && b.y == 1){
            results.add("<^");
            return results;
        }
        if(a.x == 1 && a.y == 1 && b.x == 0 && b.y == 2){
            results.add("^>");
            return results;
        }
        if(a.x == 1 && a.y == 0 && b.x == 0 && b.y == 2){
            results.add(">>^");
            return results;
        }
        if(a.x == 0 && a.y == 1 && b.x == 1 && b.y == 2){
            results.add("v>");
            return results;
        }
        if(a.x == b.x){
            if(a.y == b.y){
                results.add("");
                return results;
            }
            if(a.y > b.y){
                String possibility = "";
                for(int i = 0; i < a.y - b.y; i++){
                    possibility += "<";
                }
                results.add(possibility);
                return results;
            }
            if(a.y < b.y){
                String possibility = "";
                for(int i = 0; i < b.y - a.y; i++){
                    possibility += ">";
                }
                results.add(possibility);
                return results;
            }
        }
        if(a.y == b.y){
            if(a.x > b.x){
                String possibility = "";
                for(int i = 0; i < a.x - b.x; i++){
                    possibility += "^";
                }
                results.add(possibility);
                return results;
            }
            if(a.x < b.x){
                String possibility = "";
                for(int i = 0; i < b.x - a.x; i++){
                    possibility += "v";
                }
                results.add(possibility);
                return results;
            }
        }

        return results;
    }
}