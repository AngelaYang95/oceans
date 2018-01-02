import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class fish_species extends PApplet {


// Globals
Table table;
HashMap<String, Family> fishFamilies = new HashMap<String, Family>();
int year = 2009;
int translate = 0;
//HashMap<String, color> colorMap = new HashMap<String, color>();

int c2011 = 0xfffdc355;
int c2012 = 0xfff9a148;
int c2013 = 0xfff4733b;
int c2014 = 0xffef5236;
int c2015 = 0xff73ad70;
int c2016 = 0xff9fd07f;
int c2017 = 0xff278874;

public void setup() {
  
  table = loadTable("fish.csv","csv"); 
  
  String testYear = "0";
  for(TableRow row : table.rows()) {
    String year = row.getString(0).substring(0, 4);
    String family = row.getString(3);
    String fish = row.getString(4);
    
    if (!fishFamilies.containsKey(family)) {
       Family newFam = new Family(family);
       fishFamilies.put(family, newFam);
    } 
    
    // Increase count of species.
    Family curr = fishFamilies.get(family);
    curr.addFish(fish, year);
  }
  frameRate(1);
  background(1);
  drawOnce();
}

public void drawOnce() {

  // Draw for 
  background(0);
  translate(0, 50);
  strokeWeight(1);
  colorMode(RGB, 20);
  stroke(20, 20);
  textSize(24);

  pushMatrix();
  while(year < 2018) {
    text(year, 20, -20);
    int prevNumSpecies, numSpecies = 0;
    String myYear = Integer.toString(year);
    for(Family currFam: fishFamilies.values()) {
      // Translate for current Class.
      translate(10, 0);
      translate += 10;
      
      // Draw marine class.
      for(FishCount curr: currFam.species.values()) {
         int count = curr.getCount(myYear);
         String fishName = curr.getName();
      
         noFill();
         if (count != -1) {
           ellipse(0, 0, count, count);
         }
      }
    }
    translate(-translate, 200);
    translate = 0;
    
    year+=2;
  }
  popMatrix();
      
  textSize(10);
  for(Family currFam: fishFamilies.values()) {
      pushMatrix();
      rotate(HALF_PI);
      text(currFam.famName,0,0);
      popMatrix();
      translate(10, 0);
  }
}

public void draw2() {
  if (year > 2018) {
    return;
  }
  // Draw for 
  background(0);
  translate(0, 50);
  strokeWeight(1);
  colorMode(RGB, 20);
  stroke(20, 20);
  textSize(24);

  text(year, 700, 700);
  int prevNumSpecies, numSpecies = 0;
  String myYear = Integer.toString(year);
  for(Family currFam: fishFamilies.values()) {
    prevNumSpecies = numSpecies;
    numSpecies = currFam.species.size();
    int offset = -5 * numSpecies / 2;
    
    // Translate for current Class.
    translate((numSpecies / 2 + prevNumSpecies / 2 )*5, 0);
    translate += (numSpecies / 2 + prevNumSpecies / 2 )*5;
    
    // Class Details.
    ellipse(0, -5, 8, 8);
    //text(currFam.famName, 0, -10);
    line(0, 0, 0, 10);
    
    // Draw marine class.
    pushMatrix();
    for(FishCount curr: currFam.species.values()) {
       int count = curr.getCount(myYear);
       String fishName = curr.getName();
    
       noFill();
       //bezier(0, 0, 0, 30, offset, 0,offset, 30);
       if (count != -1) {
         //line(offset, 30, offset, 30 + count*5);
         //ellipse(offset,30 + count*5 + 4, 8, 8);
         ellipse(offset, 30, count*2, count*2);
       }
       offset += 5;
    }
    popMatrix();
    
    if (translate >= 1000) {
      translate(-translate, 200);
      translate = 0;
    }
  }
  year++;
  //if (year == 2018) {
  //  year = 2011;
  //}
}

class Family {
  HashMap<String, FishCount> species = new HashMap<String, FishCount>();
  String famName;
  Family(String name) {
    this.famName = name;
  }
  
  public void addFish(String fishName, String year) {
    if(!species.containsKey(fishName)) {
      FishCount fishCounter = new FishCount(fishName);
      species.put(fishName, fishCounter);
    }
    species.get(fishName).increaseCount(year);
  }
  
  public HashMap<String, FishCount> getSpecies() {
    return species;
  }
}

// Class for species of fish and the sitings of them.
class FishCount {
  IntDict yearCount = new IntDict();
  String name;
  
  FishCount(String name) {
    this.name = name;
  }
  
  public void increaseCount(String year) {
     if (!yearCount.hasKey(year)) {
       yearCount.set(year, 1);
     } else {
        yearCount.add(year, 1); 
     }
  }
  
  public int getCount(String year) {
    return yearCount.hasKey(year) ? yearCount.get(year) : -1;
  }
  
  public String[] getKeys() {
    return yearCount.keyArray();
  }
  
  public String getName() {
     return name; 
  }
}
   
  public void settings() {  size(1500, 1500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "fish_species" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}