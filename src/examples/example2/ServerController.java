package examples.example2;

public class ServerController {
    private static final String[] names = {"Willy", "Felix", "Carlsbad", "Habib"};
    private static final String[] adjectives = {"Witty", "Funny", "Charismatic", "Humble"};

    public static String getRandom() {
        String name = names[(int) (Math.random() * names.length)];
        String adj = adjectives[(int) (Math.random() * adjectives.length)];
        return String.format("%s %s",name, adj);
    }

}
