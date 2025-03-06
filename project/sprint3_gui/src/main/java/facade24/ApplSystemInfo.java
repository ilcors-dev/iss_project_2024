package facade24;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import unibo.basicomm23.utils.CommUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
readConfig invocato da CustomContainer

setup() e getActorNamesInApplCtx() invocati da
  ApplguiCore create da FacadeBuilder
 */
public class ApplSystemInfo {
    public static String qakSysHost;
    public static String qakSysCtx;
    public static String applActorName;
    public static String ctxportStr;
    public static int ctxport;
    public static String facadeportStr;
    public static int facadeport;
    public static String appName;
    public static List<String> systemTheories;

    private static Prolog pengine;

    /*
     * facadeConfig.json
     */
    public static void readConfig() {
        Map<String, Object> config = QaksysConfigSupport.readConfig("facadeConfig.json");
        if (config != null) {
            // qakSysHost = config.get(0);
            // ctxportStr = config.get(1);
            // qakSysCtx = config.get(2);
            // applActorName = config.get(3);
            // facadeportStr = config.get(4);
            // appName = config.get(5);
            qakSysHost = (String) config.get("host");
            ctxportStr = (String) config.get("port");
            qakSysCtx = (String) config.get("context");
            applActorName = (String) config.get("facade");
            facadeportStr = (String) config.get("facadeport");
            appName = (String) config.get("appName");

            Object theoriesObj = config.get("systemTheories");
            systemTheories = new ArrayList<>();
            if (theoriesObj instanceof List<?>) {
                for (Object item : (List<?>) theoriesObj) {
                    if (item instanceof String) {
                        systemTheories.add((String) item);
                    }
                }
            }

            ctxport = Integer.parseInt(ctxportStr);
            facadeport = Integer.parseInt(facadeportStr);
        }

        // setup();
        // getActorNamesInApplCtx( );
    }

    public static List<String> getActorNamesInApplCtx() {
        // CommUtils.outcyan( "ApplSystemInfo | getActorNames ctx=" + ctx );
        List<String> actors = getAllActorNames(qakSysCtx);
        CommUtils.outcyan("ApplSystemInfo ACTORS ON THE localhost  ");
        actors.forEach(a -> CommUtils.outcyan(a));

        return actors;
    }

    public static List<String> getAllActorNames(String ctxName) {
        try {
            SolveInfo actorNamesSol = pengine.solve("getActorNames(A," + ctxName + ").");
            String actorNames = actorNamesSol.getVarValue("A").toString();
            return Arrays.asList(actorNames.replace("[", "")
                    .replace("]", "").split(","));
        } catch (Exception e) {
            CommUtils.outred("ApplSystemInfo | getAllActorNames");
            return new ArrayList<String>();
        }
    }

    public static void setup() {
        try {
            pengine = new Prolog();

            for (String systemTheory : systemTheories) {
                Theory systemTh = new Theory(new FileInputStream(systemTheory));
                pengine.addTheory(systemTh);
                CommUtils.outblue("ApplSystemInfo | setup theories:\n" + systemTh);
            }

            Theory rulesTh = new Theory(new FileInputStream("sysRules.pl"));
            pengine.addTheory(rulesTh);
            CommUtils.outblue("ApplSystemInfo | setup theories:\n" + rulesTh);
        } catch (Exception e) {
            e.printStackTrace();
            CommUtils.outred("ApplSystemInfo | setup ERROR:" + e.getMessage());
        }
    }
}
