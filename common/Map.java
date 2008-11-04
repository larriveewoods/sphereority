package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
	public static final String DEFAULT_MAP = "10 10\n"+
    "++++++++++\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "++++++++++\n"; 

	
    String name;       // map name
    String data;       // map raw data **pending for deletion (waste of space)**
    int y_size;        // map y size
    int x_size;        // map x size
    char[][] mapping;  // two dimensional array of char for mapping
    boolean[][] wall;  // two dimensional array of boolean for walls (true if wall exists on coordinates)

    
    /**
     * Default Map constructor
     * Creates an empty Map that is 10 by 10 bounded by x's 
     */
    public Map() {
        name = "default";
        data = DEFAULT_MAP;
        parseData();
    }
    
    /**
     * Map constructor which reads in a map name
     * @param mapname the name of the map to be read (default filepath is maps directory)
     */
    public Map(String mapname) {
        name = mapname.toString();
        data = "";
        try {
            File file = new File("maps/" + name + ".map"); //will this screw up in windows?
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data = data + scanner.nextLine() + "\n";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parseData();
    }
    
    /**
     * Convenient procedure to parse raw data in constructors.
     *
     */
    private void parseData() {
        // parse raw data here
        Scanner parser = new Scanner(data);
        String line;
        x_size = parser.nextInt();
        y_size = parser.nextInt();
        mapping = new char[y_size][x_size];
        wall = new boolean[y_size][x_size];
        if (parser.nextLine().toString().startsWith(" ")) {
            // do nothing
        }
        int y = 0;
        int x = 0;
        while (parser.hasNextLine()) {
            line = parser.nextLine().toString();
            mapping[y] = line.toCharArray();
            y++;
        }
        for (y=0; y<y_size; y++) {
            for (x=0; x<x_size; x++) {
                if (mapping[y][x] == '+') {
                    wall[y][x] = true;
                }
                else {
                    wall[y][x] = false;
                }
            }
        }
    }
    
    /**
     * 
     * @return the name of the map
     */
    public String getName() {
        return name;
    }
    
    /** 
     * 
     * @return the raw text data input
     */
    public String getData() {
        return data;
    }
    
    /**
     * 
     * @return the maximum x of the map
     */
    public int getXSize() {
        return x_size;
    }
    
    /**
     * 
     * @return the maximum y of the map
     */
    public int getYSize() {
        return y_size;
    }
    
    /**
     * Note: this method uses zero-based indexing.
     * @param   x the x-axis coordinate (origin is top-left corner)
     * @param   y the y-axis coordinate (origin is top-left corner)
     * @return  true if there exists a wall on the coordinate; false otherwise.
     */
    public boolean isWall(int x, int y) {
        return wall[y][x];
    }
    
    /**
     * Method to dump all variables contained within Map object.
     *
     */
    public void varDump() {
        System.out.println("String name: " + name);
        System.out.println("String data: " + data);
        System.out.println("int x_size: " + x_size);
        System.out.println("int y_size: " + y_size);
        
        // print char array
        System.out.print("char[][] mapping: ");
        int y = 0;
        int x = 0;
        for (y = 0; y < y_size; y++) {
            for (x =0; x < x_size; x++) {
                System.out.print(mapping[y][x]);
            }
            System.out.println();
        }
        
        // print boolean array
        System.out.print("boolean[][] wall: ");
        y = 0;
        x = 0;
        for (y = 0; y < y_size; y++) {
            for (x =0; x < x_size; x++) {
                if (wall[y][x]) {
                    System.out.print("t");
                }
                else {
                    System.out.print("f");
                }
            }
            System.out.print("\r\n");
        }        
        
    }
}
