package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.StringHelper;
import uwu.nyaa.owo.finalproject.data.models.FullTag;
import uwu.nyaa.owo.finalproject.data.models.TagFileCount;

public class TableTag
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_tag";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_tag("
            + "tag_id serial PRIMARY KEY, "
            + "subtag_id serial NOT NULL,"
            + "namespace_id serial NOT NULL,"
            + "UNIQUE (subtag_id, namespace_id),"
            + "CONSTRAINT fk_subtag_id FOREIGN KEY(subtag_id) REFERENCES tbl_subtag(subtag_id),"
            + "CONSTRAINT fk_namespace_id FOREIGN KEY(namespace_id) REFERENCES tbl_namespace(namespace_id)"
            + ");";


    /**
     * Inserts the given tag and returns it's new id, returns -1 if error / exists
     * @param fullTag The tag to insert
     * @return The new tag id or -1
     * @throws NullPointerException if fulltag or db connection is null
     */
    public static int insertTag(String fullTag) throws NullPointerException
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return insertTag(fullTag, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, String.format("Error inserting tag with value %s", fullTag));
        }

        return -1;
    }

    /**
     * Inserts the given tag and returns it's new id, returns -1 if error / exists
     * @param fullTag The tag to insert
     * @param c The database connection
     * @return The new tag id or -1
     * @throws NullPointerException if fulltag or db connection is null
     */
    public static int insertTag(String fullTag, Connection c) throws SQLException, NullPointerException
    {
        String[] tag = StringHelper.partitionTag(fullTag);

        int namespace_id = TableNamespace.insertOrSelectByNamespace(tag[0], c);
        int subtag_id = TableSubtag.insertOrSelectBySubtag(tag[1], c);

        final String SQL = "INSERT INTO tbl_tag(namespace_id, subtag_id) VALUES (?, ?) RETURNING tag_id";

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, namespace_id);
            pstmt.setInt(2, subtag_id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }


    public static int insertOrSelectTag(String fullTag)
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return insertOrSelectTag(fullTag, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, String.format("Error selecting or inserting tag with value %s", fullTag));
        }

        return -1;
    }
    public static int insertOrSelectTag(String fullTag, Connection c) throws NullPointerException, SQLException
    {
        int tag_id = getTagId(fullTag, c);
        
        if(tag_id != -1)
            return tag_id;
        
        return insertTag(fullTag, c);
    }
    /**
     * gets the id for the given tag or -1
     * @param fulltag The tag to search
     * @return The tag id or -1
     * @throws NullPointerException if the tag is null
     */
    public static int getTagId(String fulltag) throws NullPointerException
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return getTagId(fulltag, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, String.format("SQL Exception getting tag id for %s", fulltag));
        }

        return -1;
    }

    /**
     * Gets the tag id of the given tag or -1
     * @param fulltag The tag to search
     * @param c The database connection
     * @return The tag id or -1
     * @throws SQLException
     * @throws NullPointerException If the tag or db connection is null
     */
    public static int getTagId(String fulltag, Connection c) throws SQLException, NullPointerException
    {
        // TODO: make this use 1 query instead of 3

        String[] tag = StringHelper.partitionTag(fulltag);

        int namespaceID = TableNamespace.getNamespaceID(tag[0], c);
        int subtagID = TableSubtag.getSubtagID(tag[1], c);

        if(namespaceID == -1 || subtagID == -1)
            return -1;

        final String SQL = "SELECT tag_id FROM tbl_tag WHERE namespace_id = ? AND subtag_id = ?";

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, namespaceID);
            pstmt.setInt(2, subtagID);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }


    /**
     * Gets as many tags as the limit allows
     * @param limit The number of tags to get
     * @return A list of tags or en empty list
     */
    public static List<FullTag> getTags(int limit)
    {
        LinkedList<FullTag> items = new LinkedList<>();

        final String SQL = "SELECT tbl_tag.tag_id, tbl_tag.namespace_id, tbl_tag.subtag_id, tbl_namespace.namespace, tbl_subtag.subtag " +
                "FROM tbl_tag " +
                "JOIN tbl_namespace ON tbl_tag.namespace_id = tbl_namespace.namespace_id " +
                "JOIN tbl_subtag ON tbl_tag.subtag_id = tbl_subtag.subtag_id " +
                "LIMIT ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement pstmt = c.prepareStatement(SQL))
        {

            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next())
            {
                FullTag a = new FullTag();

                a.tag_id = rs.getInt(1);
                a.namespace_id = rs.getInt(2);
                a.subtag_id = rs.getInt(3);
                a.namespace = rs.getString(4);
                a.subtag = rs.getString(5);

                items.add(a);
            }
            return items;
        }
        catch (SQLException e)
        {
            Logger.warn(e, "Error searching for files");
        }

        return items;
    }
    
    
    /**
     * gets the number of files with the given tag
     * @param tag_id The tag_id to search
     * @return the number of files with that tag or -1 if there is an error
     */
    public static int getFileCount(int tag_id)
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return getFileCount(tag_id, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, "Error searching counting tag {}", tag_id);
        }

        return -1;
    }
    
    
    /**
     * gets the number of files with the given tag
     * @param tag_id The tag_id to search
     * @return the number of files with that tag or -1 if there is an error
     */
    public static int getFileCount(int tag_id, Connection c)
    {
        final String SQL = "SELECT COUNT(hash_id) FROM tbl_hash_tag WHERE tag_id = ?";
        
        try (PreparedStatement pstmt = c.prepareStatement(SQL))
           {
               pstmt.setInt(1, tag_id);
               
               ResultSet rs = pstmt.executeQuery();

               if(rs.next())
               {
                   return rs.getInt(1);
               }
           }
           catch (SQLException e)
           {
               Logger.warn(e, "Error searching counting tag {}", tag_id);
           }
        
        return -1;
    }
    
    public static List<TagFileCount> getFileCount(List<Integer> tag_id)
    {
        LinkedList<TagFileCount> items = new LinkedList<>();
        
        try(Connection c = DatabaseConnection.getConnection())
        {
            for(int i : tag_id)
            {
                TagFileCount tfc = new TagFileCount();
                tfc.count =getFileCount(i, c);
                tfc.tag_id = i;
                items.add(tfc);
            }
        }
        catch (SQLException e) 
        {
            Logger.warn(e, "Error searching counting tags {}", tag_id);
        }
        
        return items;
    }
    public static List<Integer> getFileCount(int[] tag_id, Connection c)
    {
        LinkedList<Integer> items = new LinkedList<>();
        
        for(int i = 0; i < tag_id.length; i++)
        {
            items.add(getFileCount(tag_id[i], c));
        }
        
        return items;
    }
    
    
    public static void addPredefinedTags(int amount)
    {
        String[] tags = {
                "dark",    "him", "outside", "square"
                ,"accident",    "day", "hint",    "own", "stairway"
                ,"acid",    "decide",  "his", "oxygen",  "stand"
                ,"across",  "decided", "history", "page",    "stars"
                ,"act", "decimal", "hold",    "paint",   "start"
                ,"add", "deep",    "hole",    "pair",    "state"
                ,"admission",   "delivery",    "home",    "pants",   "statement"
                ,"Africa",  "dentist", "hope",    "paper",   "stay"
                ,"after",   "deposit", "horse",   "paragraph",   "step"
                ,"again",   "describe",    "hospital",    "parents", "stick"
                ,"against", "desert",  "hot", "park",    "still"
                ,"age", "design",  "hotel",   "part",    "stone"
                ,"ago", "desk",    "hours",   "party",   "stood"
                ,"agree",   "destination", "house",   "passed",  "stop"
                ,"aide",    "developed",   "how", "passengers",  "store"
                ,"air", "diary",   "however", "password",    "storm"
                ,"alarm",   "dictionary",  "huge",    "past",    "story"
                ,"all", "did", "human",   "pattern", "stove"
                ,"almost",  "didn",  "hundred" 
                ,"alone",   "died",    "husband", "payment", "strange"
                ,"already", "difference",  "pedestrians", "street"
                ,"also",    "different" 
                ,"although",    "digital", "ice", "pencil",  "students"
                ,"always",  "diner",   "idea",    "people",  "study"
                ,"am",  "dinner",  "if",  "per", "subject"
                ,"ambulance",   "direct",  "important",   "period",  "subtract"
                ,"America", "direction",   "in",  "perishable",  "subway"
                ,"amount",  "directions",  "inches",  "person",  "such"
                ,"an",  "discount",    "include", "pet", "suddenly"
                ,"and", "discovered",  "increase",    "phone",   "sum"
                ,"angle",   "distance",    "India",   "pick",    "summer"
                ,"angry",   "divide",  "information", "picture", "sun"
                ,"animal",  "do",  "inside",  "piece",   "supermarket"
                ,"another", "doctor",  "instead", "pipe",    "supper"
                ,"answer",  "does",    "instruments", "place",   "supplies"
                ,"any", "dog", "interested",  "plains",  "suppose"
                ,"anything",    "doll",    "internet",    "plan",    "sure"
                ,"apartment",   
                "appear",  "done",    "into",    "planet",  "surprise"
                ,"are", "door",    "iron",    "plant",   "switch"
                ,"area",    "down",    "is",  "plate",   "symbols"
                ,"arms",    "download",    "island",  "play",    "system"
                ,"around",  "draw",    "it",  "please",  "table"
                ,"art", "drawing", "it"
                ,"as",  "dress",   "its", "point",   "take"
                ,"Asia",    "drive",   "job", "poison",  "talk"
                ,"ask", "driver",  "join",    "police",  "tall"
                ,"at",  "drop",    "jumped",  "poor",    "taste"
                ,"ATM", "drugs",   "just",    "pose",    "tax"
                ,"aunt",    "dry", "keep",    "possible",    "teach"
                ,"Australia",   "dryer",   "kept",    "pounds",  "teacher"
                ,"away",    "due", "key", "power",   "team"
                ,"baby",    "during",  "keyboard",    "practice",    "television"
                ,"back",    "each",    "killed",  "present", "tell"
                ,"bad", "early",   "king",    "president",   "temperature"
                ,"bakery",  "ears",    "know",    "press",   "ten"
                ,"ball",    "earth",   "ladies",  "price",   "test"
                ,"bank",    "east",    "lady",    "private", "text"
                ,"base",    "easy",    "lake",    "probably",    "than"
                ,"be",  "eat", "land",    "problem", "that"
                ,"bear",    "edge",    "language",    "produce", "the"
                ,"beat",    "effect",  "laptop",  "product", "their"
                ,"beautiful",   "eggs",    "large",   "promise", "them"
                ,"beauty",  "eight",   "last",    "proof",   "themselves"
                ,"became",  "either",  "later",   "prove",   "then"
                ,"because", "electric",    "laugh",   "pull",    "there"
                ,"become",  "elevator",    "laundry", "push",    "these"
                ,"bed", "else",    "law", "put", "they"
                ,"been",    "email",   "lay", "question",    "thing"
                ,"before",  "emergency",   "learn",   "quickly", "think"
                ,"begin",   "employer",    "lease",   "quiet",   "third"
                ,"behind",  "empty",   "least",   "quite",   "this"
                ,"being",   "end", "leave",   "rabbit",  "those"
                ,"believe", "energy",  "left",    "race",    "though"
                ,"below",   "engine",  "legs",    "radio",   "thought"
                ,"beside",  "England", "length",  "railroad",    "thousand"
                ,"best",    "English", "less",    "rain",    "three"
                ,"better",  "enough",  "let", "raise",   "through"
                ,"between", "enter" 
                ,"bicycle", "entrance",    "letter",  "reached", "time"
                ,"big", "equal",   "life",    "read",    "tiny"
                ,"bill",    "equation",    "light",   "ready",   "to"
                ,"bird",    "Europe",  "like",    "real",    "today"
                ,"bit", "even",    "line",    "really",  "together"
                ,"black",   "ever",    "link",    "reason",  "toll"
                ,"blue",    "every",   "list",    "receipt", "too"
                ,"boat",    "everyone",    "listen",  "received",    "took"
                ,"body",    "everything",  "little",  "receptionist",    "top"
                ,"book",    "exactly", "live",    "record",  "total"
                ,"both",    "example", "local",   "red", "touch"
                ,"bottom",  "exercise",    "long",    "refund",  "tow"
                ,"bowl",    "exists",  "look",    "region",  "toward"
                ,"box", "experiment",  "lost",    "remain",  "town"
                ,"boy", "explain", "lot", "remember",    "train"
                ,"break",   "explosives",  "love",    "rent",    "training"
                ,"breakfast",   "express", "low", "report",  "travel"
                ,"bright",  "eye", "lunch",   "represent",   "tree"
                ,"bring",   "face",    "machine", "reservation", "trip"
                ,"broken",  "fact",    "mad", "reservations",    "trouble"
                ,"brother", "fall",    "made",    "reserved",    "truck"
                ,"brought", "family",  "main",    "rest",    "true"
                ,"brown",   "far", "make",    "restaurant",  "try"
                ,"browse",  "farm",    "male",    "result",  "turn"
                ,"build",   "farmer",  "man", "return",  "two"
                ,"building",    "fast",    "manager", "ride",    "type"
                ,"built",   "father",  "many",    "right",   "uncle"
                ,"burning", "feel",    "map", "rinse",   "under"
                ,"bus", "feeling", "mark",    "rise",    "understand"
                ,"business",    "feet",    "married", "river",   "until"
                ,"busy",    "felt",    "material",    "road",    "up"
                ,"but", "female",  "math",    "rock",    "upset"
                ,"buy", "few", "may", "roll",    "us"
                ,"by",  "field",   "maybe",   "room",    "use"
                ,"cab", "fight",   "me",  "roots",   "usually"
                ,"cabinet", "figure",  "mean",    "round",   "valley"
                ,"cable",   "file",    "measure", "rule",    "very"
                ,"cafeteria",   "filled",  "mechanic",    "run", "video"
                ,"call",    "finally", "medicine",    "said",    "village"
                ,"came",    "find",    "meet",    "sail",    "virus"
                ,"can", "fine",    "meeting", "sale",    "visit"
                ,"can","t",   "fingers", "member",  "same"    
                ,"cancel",  "finished",    "men", "save",    "vote"
                ,"cannot",  "fire",    "message", "say", "wait"
                ,"captain", "firefighter", "metal",   "scale",   "walk"
                ,"car", "first",   "method",  "scared",  "wall"
                ,"care",    "fish",    "middle",  "schedule",    "want"
                ,"careful", "five",    "might",   "school",  "war"
                ,"carry",   "flammable",   "mile",    "science", "warm"
                ,"case",    "floor",   "milk",    "scientist",   "warning"
                ,"cash",    "flowers", "million", "sea", "was"
                ,"cashier", "fly", "mind",    "search",  "wash"
                ,"cat", "follow",  "minutes", "second",  "watch"
                ,"catch",   "food",    "miss",    "secret",  "water"
                ,"caught",  "foot",    "mobile",  "section", "way"
                ,"cause",   "for", "moment",  "see", "we"
                ,"caution", "force",   "money",   "seeds",   "wear"
                ,"cell",    "forest",  "monitor", "seem",    "weather"
                ,"cells",   "forget",  "month",   "self",    "web"
                ,"center",  "fork",    "moon",    "sent",    "website"
                ,"cents",   "form",    "more",    "sentence",    "week"
                ,"century", "found",   "morning", "separate",    "weekday"
                ,"certain", "four",    "most",    "serve",   "weekend"
                ,"chair",   "fraction",    "mother",  "set", "weigh"
                ,"change",  "fragile", "mountain",    "seven",   "well"
                ,"charge",  "free",    "mouse",   "several", "went"
                ,"chat",    "French",  "mouth",   "shape",   "were"
                ,"check",   "friend",  "move",    "she", "west"
                ,"chest",   "from"    
                ,"child",   "front"   
                ,"children",    "fuel"   
                ,"China",   "full",    "much",    "short",   "when"
                ,"choose",  "game",    "multiply",    "should",  "where"
                ,"church",  "garage",  "music",   "shouted", "whether"
                ,"cigarettes",  "garden",  "must",    "show",    "which"
                ,"circle",  "gas", "my",  "sick",    "while"
                ,"city",    "gave",    "name",    "side",    "white"
                ,"class",   "gentlemen",   "nation",  "sign",    "who"
                ,"clean",   "get", "natural", "simple",  "whole"
                ,"cleaners",    "girl",    "near",    "since",   "whose"
                ,"clear",   "give",    "need",    "sing",    "why"
                ,"climbed", "glass",   "network", "single",  "wide"
                ,"close",   "go",  "never",   "sister",  "wife"
                ,"closed",  "God", "new", "sit", "wild"
                ,"closet",  "gold",    "newspapers",  "site",    "will"
                ,"clothes", "gone",    "next",    "six", "wind"
                ,"cloud",   "good",    "night",   "size",    "window"
                ,"coast",   "got", "nine",    "skateboard",  "winter"
                ,"cold",    "government",  "no",  "skills",  "wire"
                ,"collect", "grandparents",    "none",    "skin",    "wireless"
                ,"college", "grass",   "north",   "sky", "wish"
                ,"color",   "great",   "northbound",  "sleep",   "with"
                ,"combustible", "green",   "not", "slowly",  "withdrawal"
                ,"come",    "ground",  "notebook",    "small",   "within"
                ,"common",  "group",   "notes",   "smile",   "without"
                ,"complete",    "grow",    "nothing", "snow",    "woman"
                ,"computer",    "had", "notice",  "so",  "wonder"
                ,"confidential",    "hair",    "now", "sofa",    "wood"
                ,"contains",    "half",    "number",  "soft",    "word"
                ,"continued",   "hand",    "nurse",   "soil",    "work"
                ,"control", "happened",    "object",  "soldier", "worker"
                ,"controls",    "happy",   "ocean",   "solve",   "world"
                ,"cool",    "hard",    "of",  "some",    "worse"
                ,"copy",    "hardware",    "off", "someone", "worst"
                ,"correct", "has", "office",  "something",   "would"
                ,"cost",    "hat", "often",   "sometimes",   "write"
                ,"could",   "have",    "oh",  "son", "writer"
                ,"couldn",    "he",  "old", "song"   
                ,"count",   "head",    "on",  "soon",    "yard"
                ,"country", "hear",    "once",    "sound",   "year"
                ,"couple",  "heart",   "one", "south",   "yell"
                ,"coupon",  "heat",    "online",  "southbound",  "yes"
                ,"course",  "heavy",   "only",    "space",   "yet"
                ,"covered", "height",  "open",    "speak",   "you"
                ,"cross",   "held",    "opinion", "special", "you"
                ,"cry", "help",    "or",  "speed",   "young"
                ,"cup", "her", "order",   "spell",   "your"
                ,"cut", "here",    "other",   "spoon",   "yourself"
        };
        
        for(int i = 0; i < amount; i++)
        {
            long shittyRandom1 = ((long)System.currentTimeMillis()/(1 + i) % 200) + (int)(System.nanoTime() * (1+i) * (1+i)) / 3;
            long shittyRandom2 = ((long)System.nanoTime()/(1 + i) % 200) + (int)(System.currentTimeMillis() * (1+i) * (1+i)) / 3;
            
            if(shittyRandom1 < 0)
                shittyRandom1 = - shittyRandom1;
            
            if(shittyRandom2 < 0)
                shittyRandom2 = - shittyRandom2;
     
            int namespaceIndex = (int)shittyRandom1 % (tags.length/3);
            int subtagIndex = (int)shittyRandom2 %tags.length;
            
            
            
            String tag;
            if(namespaceIndex > tags.length)
            {
                tag = tags[subtagIndex];
                
                insertTag(tag);
            }
            else 
            {
                tag = tags[namespaceIndex] + ":" + tags[subtagIndex];
                
                insertTag(tag);
            }
            
        }
        
        
        
    }
}
