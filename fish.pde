import processing.pdf.*;

Table table;
HashMap<String, Family> fishFamilies = new HashMap<String, Family>();
int year = 2009;
int translate = 0;
color[] colours = {0xfffdc355, 0xfff9a148, 0xfff4733b, 0xffef5236, 
                   0xff73ad70, 0xff9fd07f, 0xff278874};

public void setup() {
  table = loadTable("fish.csv","csv");
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
  
  size(1900, 1000);
  beginRecord(PDF, "fish_species.pdf");
  textFont(createFont("Helvetica", 24));
  drawOnce();
  endRecord();
}

public void drawOnce() {
  // Draw for 
  fill(100);
  background(0);
  colorMode(RGB, 20);
  translate(5, 70);
  strokeWeight(1);

  pushMatrix();
  while(year < 2018) {
    text(year, 20, -20);
    
    int cIndex = 0;
    for(Family currFam: fishFamilies.values()) {
      stroke(colours[cIndex]);
      //stroke(random(150));
      
      // Translate for current Class.
      translate(10, 0);
      translate += 10;
      
      // Draw marine class.
      String myYear = Integer.toString(year);
      for(FishCount curr: currFam.species.values()) {
  
        
         int count = curr.getCount(myYear);
         String fishName = curr.getName();
      
         noFill();
         if (count != -1) {
           ellipse(0, 0, count, count);
         }
      }
      cIndex = (cIndex + 1) % 7;
    }
    translate(-translate, 200);
    translate = 0;
    
    year+=2;
  }
  popMatrix();
      
  textSize(10);
  translate(0, 800);
  for(Family currFam: fishFamilies.values()) {
      pushMatrix();
      rotate(HALF_PI);
      text(currFam.famName,0,0);
      popMatrix();
      translate(10, 0);
  }
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