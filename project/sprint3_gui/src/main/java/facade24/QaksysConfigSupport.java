package facade24;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;
import unibo.basicomm23.utils.CommUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QaksysConfigSupport {

    public static java.util.Map<String, Object> readConfig(String fName) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(fName)));
            return readTheContent(data);
        } catch (Exception e) {
            try {
                CommUtils.outmagenta("QaksysConfigSupport | readConfig ERROR:" + e.getMessage());
                String data = null;
                data = new String(Files.readAllBytes(Paths.get("../" + fName)));
                return readTheContent(data);
            } catch (Exception e1) {
                CommUtils.outred("QaksysConfigSupport | readConfig ERROR AGAIN:" + e.getMessage());
                // e1.printStackTrace();
            }
        }
        return null;
    }

    protected static java.util.Map<String, Object> readTheContent(String config) throws JSONException {
        CommUtils.outyellow("qaksysConfigSupport | readTheContent " + config);
        JSONObject jsonObject = new JSONObject(config);
        String host = jsonObject.get("host").toString();
        String port = jsonObject.get("port").toString();
        String context = jsonObject.get("context").toString();
        String actorfacade = jsonObject.get("facade").toString();
        String facadeport = jsonObject.get("facadeport").toString();
        String appName = jsonObject.get("appName").toString();
        JSONArray systemTheoriesArray = jsonObject.getJSONArray("systemTheories");

        List<String> systemTheories = new ArrayList<>();
        for (int i = 0; i < systemTheoriesArray.length(); i++) {
            systemTheories.add(systemTheoriesArray.getString(i));
        }

        java.util.Map<String, Object> configMap = new java.util.HashMap<>();
        configMap.put("appName", appName);
        configMap.put("host", host);
        configMap.put("port", port);
        configMap.put("context", context);
        configMap.put("facade", actorfacade);
        configMap.put("facadeport", facadeport);
        configMap.put("systemTheories", systemTheories);

        CommUtils.outyellow("qaksysConfigSupport | readTheContent " + configMap.toString());

        return configMap;
    }
}
