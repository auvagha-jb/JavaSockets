package examples.example2;

public class ServerController {

    public static final int NAME_SERVER_PORT = 1024;

    private static final String[] names = {"Willy", "Felix", "Carlsbad", "Habib"};
    private static final String[] adjectives = {"Witty", "Funny", "Charismatic", "Humble"};

    public static String getRandom() {
        String randomName = null;

        do {
            String name = names[(int) (Math.random() * names.length)];
            String adj = adjectives[(int) (Math.random() * adjectives.length)];
            randomName = String.format("%s %s", name, adj);
        } while (randomName == null);

        return randomName;
    }

}
