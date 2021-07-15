import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String identifier, displayName, cooldownDur;

        // Get Identifier
        System.out.println("Identifier: ");
        identifier = scan.nextLine();
        String[] id = identifier.split(":");
        String idName = id[1];
        String idNamespace = id[0];

        // Get Display Name
        System.out.println("Display Name: ");
        displayName = scan.nextLine();

        // Get Cooldown Duration
        System.out.println("Cooldown Duration: ");
        cooldownDur = scan.nextLine();
        scan.close();

        String json = createItemJSON(identifier, cooldownDur, idName, idNamespace);

        try {
            Path itemPath = Paths.get(String.format("output/%s", idName));
            Files.createDirectory(itemPath);

            FileWriter itemMainFile = new FileWriter(String.format("output/%1$s" + "/%1$s" + ".json", idName));
            itemMainFile.write(json);
            itemMainFile.close();

            FileWriter itemLangFile = new FileWriter(String.format("output/%s" + "/en_US.lang", idName));
            itemLangFile.write(String.format("%1$s" + "." + "%2$s" + "=" + "%3$s", idNamespace, idName, displayName));
            itemLangFile.close();
        } catch (IOException eve) {
            eve.printStackTrace();
        }
    }

    private static String createItemJSON(String identifier, String cooldownDur, String idName, String idNamespace) {
        // Item Main
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("format_version", "1.16.100");
        Map<String, Object> itemMain = new LinkedHashMap<>();
        item.put("minecraft:item", itemMain);

        // description of Item
        Map<String, String> itemDescription = new LinkedHashMap<>();
        itemMain.put("description", itemDescription);
        itemDescription.put("identifier", identifier);
        itemDescription.put("category", "Items");

        // Components of Item
        Map<String, Object> itemComp = new LinkedHashMap<>();
        itemMain.put("components", itemComp);
        Map<String, Object> itemCompIcon = new LinkedHashMap<>();
        itemComp.put("minecraft:icon", itemCompIcon);
        itemCompIcon.put("texture", String.format("%s" + ".texture", idName));
        Map<String, Object> itemCompDisName = new LinkedHashMap<>();
        itemComp.put("minecraft:display_name", itemCompDisName);
        itemCompDisName.put("value", String.format("%1$s" + "." + "%2$s", idNamespace, idName));
        itemComp.put("minecraft:max_stack_size", 1);
        itemComp.put("minecraft:use_duration", 999999999);
        itemComp.put("minecraft:stacked_by_data", true);
        itemComp.put("minecraft:hand_equipped", true);
        Map<String, Object> itemCompCC = new LinkedHashMap<>();
        itemComp.put("minecraft:creative_category", itemCompCC);
        itemCompCC.put("parent", "itemGroup.name.tools");
        Map<String, Object> itemCompFood = new LinkedHashMap<>();
        itemComp.put("minecraft:food", itemCompFood);
        itemCompFood.put("can_always_eat", true);
        Map<String, Object> itemCompCool = new LinkedHashMap<>();
        itemComp.put("minecraft:cooldown", itemCompCool);
        itemCompCool.put("category", idNamespace);
        itemCompCool.put("duration", Float.parseFloat(cooldownDur));
        Map<String, Object> itemCompRenOff = new LinkedHashMap<>();
        itemComp.put("minecraft:render_offsets", itemCompRenOff);
        Map<String, Object> itemCompRenOffMain = new LinkedHashMap<>();
        itemCompRenOff.put("main_hand", itemCompRenOffMain);
        Map<String, Object> itemCompRenOffMainFP = new LinkedHashMap<>();
        Map<String, Object> itemCompRenOffMainTP = new LinkedHashMap<>();
        itemCompRenOffMain.put("first_person", itemCompRenOffMainFP);
        List<Integer> itemCompRenOffMainFPScale = new ArrayList<>();
        itemCompRenOffMainFP.put("scale", itemCompRenOffMainFPScale);
        itemCompRenOffMainFPScale.add(0);
        itemCompRenOffMainFPScale.add(0);
        itemCompRenOffMainFPScale.add(0);
        itemCompRenOffMain.put("third_person", itemCompRenOffMainTP);
        List<Integer> itemCompRenOffMainTPScale = new ArrayList<>();
        itemCompRenOffMainTP.put("scale", itemCompRenOffMainTPScale);
        itemCompRenOffMainTPScale.add(0);
        itemCompRenOffMainTPScale.add(0);
        itemCompRenOffMainTPScale.add(0);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(item);
        return json;
    }
}